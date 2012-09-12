package de.cygn.foobar2000;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class StringStore {

	private MappedByteBuffer mb;

	public String get(int i) throws IOException {
		int old_position = mb.position();
		mb.position(i);
		String result = Utils.readNullTerminatedString(mb);
		mb.position(old_position);
		return result;
	}

	public void put(int i, String s) {
	}

	public StringStore(MappedByteBuffer mb) {
		this.mb = mb;
	}
}

public class FPLPlaylist {
	
	
	private static Logger logger = Logger.getLogger(FPLPlaylist.class.getName());
	private static int[] export_fpl_magic = {-1852006175, 1115110648, -868537211, -221052652};
	private static int[] intern_fpl_magic = {-614910208, 1314278724, 717632640, -1180268102};
	private static int[] database_fpl_magic = {-1854960391, 1262355587, -545234278, -2121477978};

	public static void saveM3U(ArrayList<Track> playlist, File output) throws FileNotFoundException, IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(output));
		out.write("#EXTM3U\n");
		for (Track track : playlist) {
			String title = "";
			String artist = "";
			if (track.getProperties().containsKey("title")) {
				title = track.getProperties().get("title").get(0);
			}
			if (track.getProperties().containsKey("artist")) {
				artist = track.getProperties().get("artist").get(0);
			}
			out.write("#EXTINF:");
			out.write((int) track.getDuration());
			out.write(",");
			out.write(artist);
			out.write(" - ");
			out.write(title);
			out.write("\n");
			out.write(track.getFilename());
			out.write("\n");
			//String line = String.format("#EXTINF:%d,%s - %s\n%s\n", (int)track.duration, artist, title, track.filename );
			//out.write(line);
		}
		out.flush();
		out.close();
	}

	public static void decodePlaylist(ByteBuffer mb, int number_of_songs, Map<Integer, String> string_store, int string_table_offset, ArrayList<Track> tracklist, int start_track) throws IOException {
		//logger.setLevel(Level.ALL);
		//logger.getParent().getHandlers()[0].setLevel(Level.ALL);
		for (int i = 0; i < number_of_songs; i++) {
			int fileExists = mb.getInt(); //  4 byte integer, if this is 0, the file doesn't exist

			Track track;
			if (start_track >= 0) {  // database mode
				track = tracklist.get(start_track + i);
				mb.getInt();
			} else {  // playlist mode
				// read filename 
				track = new Track(Utils.cleanFoldername(string_store.get(mb.getInt() + string_table_offset)));
				tracklist.add(track);
			}

			int unknown1 = mb.getInt(); //  4 bytes, always 0?
			if (fileExists != 0) {
				track.setFilesize(mb.getInt());
				int unknown2 = mb.getInt(); //  4 bytes, always 0?
				double unknown3 = mb.getDouble(); // 8 bytes, unknown meaning
				track.setDuration(mb.getDouble()); //  8 byte floating point value representing duration in seconds
				track.setAlbumReplayGain(mb.getFloat()); // 4 bytes, probably floating point album replaygain value (7AC4 = -1.00 if none)
				track.setTrackReplayGain(mb.getFloat()); // 4 byte floating point representing track replaygain value (7AC4 = -1.00 if none)
				track.setAlbumReplayPeak(mb.getFloat()); //4 bytes, probably representing an album peak value (80BF = 1.00 if none)
				track.setTrackReplayPeak(mb.getFloat());

				// key/value store starts here. read the number of items
				int total_keys = mb.getInt();
				int nr_of_non_interleaved_keys = mb.getInt();
				int nr_of_interleaved_keys = mb.getInt();
				total_keys = nr_of_interleaved_keys + nr_of_non_interleaved_keys;
				int unknown4 = mb.getInt(); // unknown ?

				// we will establish a mapping key -> <list of values>
				ArrayList<String> keys = new ArrayList<>();
				ArrayList<ArrayList<String>> values = new ArrayList<>();
				for (int j = 0; j < total_keys; j++) {
					values.add(new ArrayList<String>());
				}
				// in the playlist file, there is first a part of keys, then a
				// part with values
				// like this: 
				// keyA, keyB, keyC, keyD, valueA, valueB1, valueB2, valueC,...
				// 
				// these are referenced by pointers, which is handled here
				// the value_positions list is a list of offsets pointing to the values
				// like this:
				// 1,2,4,5
				// we want to know how many values a key has, so we calculate
				// a list called number_of_values from this which looks like this:
				// 1,2,1,1
				ArrayList<Integer> value_positions = new ArrayList<>();
				ArrayList<Integer> number_of_values = new ArrayList<>();

				value_positions.add(mb.getInt());
				for (int j = 0; j < nr_of_non_interleaved_keys; j++) {
					keys.add(string_store.get(string_table_offset + mb.getInt()));
					value_positions.add(Integer.valueOf(mb.getInt()));
				}
				for (int j = 1; j < value_positions.size(); j++) {
					number_of_values.add(Integer.valueOf(value_positions.get(j) - value_positions.get(j - 1)));
				}

				for (int j = 0; j < nr_of_non_interleaved_keys; j++) {
					for (int k = 0; k < number_of_values.get(j); k++) {
						values.get(j).add(string_store.get(string_table_offset + mb.getInt()));
					}
				}

				// after the part were keys and values were stored separately comes
				// a part where we have lots of key/value pairs following each other
				for (int j = 0; j < nr_of_interleaved_keys; j++) {
					keys.add(string_store.get(string_table_offset + mb.getInt()));
					values.get(nr_of_non_interleaved_keys + j).add(string_store.get(string_table_offset + mb.getInt()));
					number_of_values.add(Integer.valueOf(1));
				}

				for (int k = 0; k < keys.size(); k++) {
					track.getProperties().put(keys.get(k).toLowerCase(), values.get(k));
				}
			}
			track.updateFields();
		}
	}

	public static void walkTree(File base_folder, ByteBuffer mb, ArrayList<Track> database, boolean dontReadFileTime) throws IOException {
		int number_of_songs_in_folder = mb.getInt();
		for (int i = 0; i < number_of_songs_in_folder; i++) {
			String filename = Utils.getString(mb);
			FileTime filetime = null;
			if (!dontReadFileTime) {
				filetime = FileTime.fromMillis(Utils.fileTimeToMillis(mb.getLong()));
			}
			int subtracks = mb.getInt(); // usually 1, for non-music files 0, for .cue files > 1

			for (int j = 0; j < subtracks; j++) {
				database.add(new Track(new File(base_folder, filename).getPath(), filetime));
				mb.getInt();
				if (database.size() % 500 == 0) {
					System.out.print(".");
				}
			}
			int sub_folders = mb.getInt();
			int dummy = mb.getInt();
			int dummy2;
			for (int j = 0; j < sub_folders; j++) {
				String folder_name = Utils.getString(mb);
				String track_name = Utils.getString(mb);
				database.add(new Track(new File(new File(base_folder, filename), track_name).getPath(), filetime));
				dummy2 = mb.getInt(); // subtracks?
				for (int k = 0; k < dummy2; k++) {
					mb.getInt();
					database.add(new Track(Utils.getString(mb), filetime));
				}
			}
		}
		int sub_folders = mb.getInt();
		for (int j = 0; j < sub_folders; j++) {
			String folder_name = Utils.getString(mb);
			walkTree(new File(base_folder, folder_name), mb, database, dontReadFileTime);
		}
	}

	public static void handleDatabase(ByteBuffer mb, int header_length, ArrayList<Track> database) throws IOException {
		int start_track = 0;
		// the database file consists of multiple playlists for each directory
		// being watched. each loop iteration handles one of those playlists.
		while (mb.remaining() > 20) {
			mb.position(header_length - 4);
			String folder_name = Utils.getString(mb);

			mb.position(header_length + folder_name.length() + 56);
			String base_folder = Utils.getString(mb);

			Map<Integer, String> string_store = new HashMap<>();
			//Map<Integer, String> string_store = new TreeMap<>();

			walkTree(new File(Utils.cleanFoldername(base_folder)), mb, database, false);

			header_length = mb.position() + 40;
			mb.position(header_length - 4);
			int len = mb.getInt();
			int counter = 0;
			System.out.println();
			while (mb.position() < header_length + len) {
				string_store.put(mb.position(), Utils.readNullTerminatedString(mb));
				counter++;
				if (counter % 3000 == 0) {
					System.out.print("x");
				}
			}
			int number_of_songs = mb.getInt();
			decodePlaylist(mb, number_of_songs, string_store, header_length, database, start_track);
			header_length = mb.position() + 20;
			start_track += number_of_songs;
		}
	}

	public static void handlePlaylist(ByteBuffer mb, int header_length, ArrayList<Track> tracklist) throws IOException {
		Map<Integer, String> string_store = new HashMap<>();
		mb.position(header_length - 4);
		int len = mb.getInt();

		while (mb.position() < header_length + len) {
			string_store.put(mb.position(), Utils.readNullTerminatedString(mb));
		}
		int number_of_songs = mb.getInt();
		decodePlaylist(mb, number_of_songs, string_store, header_length, tracklist, -1);
	}

	public static ArrayList<Track> readPlaylist(File fpl_filename) throws FileNotFoundException, IOException {
		ArrayList<Track> tracklist = new ArrayList<>();
		FileInputStream f = new FileInputStream(fpl_filename);
		FileChannel ch = f.getChannel();
		MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
		mb.order(ByteOrder.LITTLE_ENDIAN);
		int[] magic = new int[4];
		for (int i = 0; i < 4; i++) {
			magic[i] = mb.getInt();
		}
		if (Arrays.equals(magic, export_fpl_magic)) {
			handlePlaylist(mb, 20, tracklist);
		} else if (Arrays.equals(magic, intern_fpl_magic)) {
			handlePlaylist(mb, 64, tracklist);
		} else if (Arrays.equals(magic, database_fpl_magic)) {
			handleDatabase(mb, 24, tracklist);
		} else {
			throw new Error("wrong FPL version!");
			// @todo: make a guess by finding first string
		}
		return tracklist;
	}

	public static void main(String argc[]) throws FileNotFoundException, IOException {
		if (argc.length < 2) {
			System.out.println("usage: foobarConverter input.fpl output.m3u");
			System.exit(-1);
		}
		String fpl_filename = argc[0];
		String m3u_filename = argc[1];
		System.out.println("converting " + fpl_filename + " to " + m3u_filename);
		ArrayList<Track> tracklist = readPlaylist(new File(fpl_filename));
		//saveM3U(tracklist, new File(m3u_filename));
	}

	public static void main0(String[] args) throws FileNotFoundException, IOException, SQLException {
		String foobarDatabase = "c:\\users\\thomas\\dropbox\\portableapps\\foobar2000\\database.dat";
		ArrayList<Track> tracklist = readPlaylist(new File(foobarDatabase));
		saveDatabase(tracklist);
	}
	
	private static void saveDatabase(final ArrayList<Track> tracklist) throws SQLException {
		String databaseUrl = "jdbc:h2:mem:account";
		databaseUrl = "jdbc:h2:tcp://localhost/~/test";
		System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
		// create a connection source to our database
		ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl, "sa", "");
		//Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa","");
		// instantiate the dao
		final Dao<Track, String> trackDao =
				DaoManager.createDao(connectionSource, Track.class);

		TableUtils.dropTable(connectionSource, Track.class, true);
		TableUtils.createTable(connectionSource, Track.class);
		try {
			trackDao.callBatchTasks(new Callable<String>() {
				public String call() throws Exception {
					for (Track track : tracklist) {
						trackDao.create(track);
					}
					return "";
				}
			});
			/*
			for (Track track : tracklist) {
				trackDao.create(track);
			}
			*/
		} catch (Exception ex) {
			Logger.getLogger(FPLPlaylist.class.getName()).log(Level.SEVERE, null, ex);
		}

		// close the connection source
		connectionSource.close();
	}
}
