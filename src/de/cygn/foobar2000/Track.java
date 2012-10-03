package de.cygn.foobar2000;

import com.j256.ormlite.field.DataType;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.File;

@DatabaseTable(tableName = "tracks")
public class Track {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(index = true, uniqueIndexName = "uniqueIdx")
	private String filename;
	@DatabaseField
	private long filetime_in_millis;
	@DatabaseField
	private String artist;
	@DatabaseField
	private String album_artist;
	@DatabaseField
	private String title;
	@DatabaseField(uniqueIndexName = "uniqueIdx")
	private String tracknumber;
	@DatabaseField
	private String bitrate;
	@DatabaseField
	private String album;
	@DatabaseField
	private String date;
	@DatabaseField(dataType = DataType.LONG_STRING)
	private String comment;
	@DatabaseField
	private String catnr;
	@DatabaseField
	private String codec;
	@DatabaseField
	private String publisher;
	@DatabaseField
	private String discogs_release_id;
	@DatabaseField
	private String style;
	@DatabaseField
	private String genre;
	@DatabaseField
	private float bpm;
	@DatabaseField
	private int rating;
	@DatabaseField
	private String key_start;
	/**
	 * duration in seconds
	 */
	@DatabaseField
	private double duration;
	@DatabaseField
	private int filesize;
	@DatabaseField
	private float albumReplayGain;
	@DatabaseField
	private float trackReplayGain;
	@DatabaseField
	private float albumReplayPeak;
	@DatabaseField
	private float trackReplayPeak;
	@DatabaseField
	private boolean inDatabase;
	private HashMap<String, ArrayList<String>> properties;

	public String toString() {
		return getFilename();
	}

	public Track() {
		properties = new HashMap<>();
	}

	public Track(String filename) {
		this.filename = filename;
		properties = new HashMap<>();
	}

	public Track(String filename, FileTime filetime) {
		this.filename = filename;
		this.filetime_in_millis = filetime.toMillis();
		properties = new HashMap<>();
	}

	/**
	 * returns a string of max. length 255, to make it fit into VARCHAR(255)
	 *
	 * @param s
	 * @return
	 */
	public static String makeFit(String s) {
		if (s.length() > 255) {
			s = s.substring(0, 255);
		}
		return s;
	}

	void updateFields() {
		if (getProperties().containsKey("artist")) {
			setArtist(makeFit(getProperties().get("artist").get(0)));
		}
		if (getProperties().containsKey("title")) {
			setTitle(makeFit(getProperties().get("title").get(0)));
		}
		if (getProperties().containsKey("album")) {
			setAlbum(makeFit(getProperties().get("album").get(0)));
		}
		if (getProperties().containsKey("tracknumber")) {
			setTracknumber(makeFit(getProperties().get("tracknumber").get(0)));
		}
		if (getProperties().containsKey("bpm")) {
			try {
				setBpm((float) Float.valueOf(getProperties().get("bpm").get(0)));
			} catch (NumberFormatException e) {
				String bpm_string = getProperties().get("bpm").get(0);
				bpm_string = bpm_string.replace(",", ".");
				try {
					setBpm((float) Float.valueOf(bpm_string));
				} catch (NumberFormatException e2) {
				}
			}
		}
		if (getProperties().containsKey("key_start")) {
			setKey_start(makeFit(getProperties().get("key_start").get(0)));
		}
		if (getProperties().containsKey("date")) {
			setDate(makeFit(getProperties().get("date").get(0)));
		}
		if (getProperties().containsKey("catnr")) {
			setCatnr(makeFit(getProperties().get("catnr").get(0)));
		}
		if (getProperties().containsKey("publisher")) {
			setPublisher(makeFit(getProperties().get("publisher").get(0)));
		}
		if (getProperties().containsKey("discogs_release_id")) {
			setDiscogs_release_id(makeFit(getProperties().get("discogs_release_id").get(0)));
		}
		if (getProperties().containsKey("style")) {
			setStyle(makeFit(getProperties().get("style").get(0)));
		}
		if (getProperties().containsKey("bitrate")) {
			setBitrate(makeFit(getProperties().get("bitrate").get(0)));
		}
		if (getProperties().containsKey("comment")) {
			setComment(makeFit(getProperties().get("comment").get(0)));
		}
		if (getProperties().containsKey("codec")) {
			setCodec(makeFit(getProperties().get("codec").get(0)));
		}
		if (getProperties().containsKey("genre")) {
			setGenre(makeFit(getProperties().get("genre").get(0)));
		}
		if (getProperties().containsKey("album artist")) {
			setAlbum_artist(makeFit(getProperties().get("album artist").get(0)));
		}
		if (getProperties().containsKey("rating")) {
			String rating = getProperties().get("rating").get(0);
			try {
				setRating((int) Integer.valueOf(getProperties().get("rating").get(0))); // @todo make failsafe
			} catch (NumberFormatException e) {
			}
		}
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the filetime_in_millis
	 */
	public long getFiletime_in_millis() {
		return filetime_in_millis;
	}

	/**
	 * @param filetime_in_millis the filetime_in_millis to set
	 */
	public void setFiletime_in_millis(long filetime_in_millis) {
		this.filetime_in_millis = filetime_in_millis;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		if (artist == null) {
			return "";
		} else {
			return artist;
		}
	}

	/**
	 * @param artist the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return the album_artist
	 */
	public String getAlbum_artist() {
		return album_artist;
	}

	/**
	 * @param album_artist the album_artist to set
	 */
	public void setAlbum_artist(String album_artist) {
		this.album_artist = album_artist;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		if (Utils.isEmpty(title)) {
			if (!(Utils.isEmpty(filename))) {
				return new File(filename).getName();
			}
		}
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the tracknumber
	 */
	public String getTracknumber() {
		if (tracknumber != null)
			return tracknumber;
		else
			return "";
	}

	/**
	 * @param tracknumber the tracknumber to set
	 */
	public void setTracknumber(String tracknumber) {
		this.tracknumber = tracknumber;
	}

	/**
	 * @return the bitrate
	 */
	public String getBitrate() {
		return bitrate;
	}

	/**
	 * @param bitrate the bitrate to set
	 */
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * @return the album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @param album the album to set
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the catnr
	 */
	public String getCatnr() {
		return catnr;
	}

	/**
	 * @param catnr the catnr to set
	 */
	public void setCatnr(String catnr) {
		this.catnr = catnr;
	}

	/**
	 * @return the codec
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * @param codec the codec to set
	 */
	public void setCodec(String codec) {
		this.codec = codec;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the discogs_release_id
	 */
	public String getDiscogs_release_id() {
		return discogs_release_id;
	}

	/**
	 * @param discogs_release_id the discogs_release_id to set
	 */
	public void setDiscogs_release_id(String discogs_release_id) {
		this.discogs_release_id = discogs_release_id;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the bpm
	 */
	public float getBpm() {
		return bpm;
	}

	/**
	 * @param bpm the bpm to set
	 */
	public void setBpm(float bpm) {
		this.bpm = bpm;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the key_start
	 */
	public String getKey_start() {
		return key_start;
	}

	/**
	 * @param key_start the key_start to set
	 */
	public void setKey_start(String key_start) {
		this.key_start = key_start;
		if (key_start != null && this.key_start.startsWith("0")) {
			this.key_start = this.key_start.substring(1);
		}
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * @return the filesize
	 */
	public int getFilesize() {
		return filesize;
	}

	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	/**
	 * @return the albumReplayGain
	 */
	public float getAlbumReplayGain() {
		return albumReplayGain;
	}

	/**
	 * @param albumReplayGain the albumReplayGain to set
	 */
	public void setAlbumReplayGain(float albumReplayGain) {
		this.albumReplayGain = albumReplayGain;
	}

	/**
	 * @return the trackReplayGain
	 */
	public float getTrackReplayGain() {
		return trackReplayGain;
	}

	/**
	 * @param trackReplayGain the trackReplayGain to set
	 */
	public void setTrackReplayGain(float trackReplayGain) {
		this.trackReplayGain = trackReplayGain;
	}

	/**
	 * @return the albumReplayPeak
	 */
	public float getAlbumReplayPeak() {
		return albumReplayPeak;
	}

	/**
	 * @param albumReplayPeak the albumReplayPeak to set
	 */
	public void setAlbumReplayPeak(float albumReplayPeak) {
		this.albumReplayPeak = albumReplayPeak;
	}

	/**
	 * @return the trackReplayPeak
	 */
	public float getTrackReplayPeak() {
		return trackReplayPeak;
	}

	/**
	 * @param trackReplayPeak the trackReplayPeak to set
	 */
	public void setTrackReplayPeak(float trackReplayPeak) {
		this.trackReplayPeak = trackReplayPeak;
	}

	/**
	 * @return the properties
	 */
	public HashMap<String, ArrayList<String>> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap<String, ArrayList<String>> properties) {
		this.properties = properties;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the inDatabase
	 */
	public boolean isInDatabase() {
		return inDatabase;
	}

	/**
	 * @param inDatabase the inDatabase to set
	 */
	public void setInDatabase(boolean inDatabase) {
		this.inDatabase = inDatabase;
	}

	public String asString() {
		StringBuffer sb = new StringBuffer();
		sb.append('#');
		sb.append(album);
		sb.append('#');
		sb.append(album_artist);
		sb.append('#');
		sb.append(artist);
		sb.append('#');
		sb.append(bitrate);
		sb.append('#');
		sb.append(bpm);
		sb.append('#');
		sb.append(catnr);
		sb.append('#');
		sb.append(codec);
		sb.append('#');
		sb.append(comment);
		sb.append('#');
		sb.append(date);
		sb.append('#');
		sb.append(discogs_release_id);
		sb.append('#');
		sb.append(duration);
		sb.append('#');
		sb.append(filename);
		sb.append('#');
		sb.append(filesize);
		sb.append('#');
		sb.append(filetime_in_millis);
		sb.append('#');
		sb.append(genre);
		sb.append('#');
		sb.append(key_start);
		sb.append('#');
		sb.append(publisher);
		sb.append('#');
		sb.append(rating);
		sb.append('#');
		sb.append(style);
		sb.append('#');
		sb.append(title);
		sb.append('#');
		sb.append(tracknumber);
		return sb.toString();
	}

	public int getKey() {
		StringBuilder sb = new StringBuilder();
			sb.append(getFilename());
			sb.append('#');
			sb.append(getTracknumber());
			return sb.toString().hashCode();
	}
	
	@Override
	public int hashCode() {
		return asString().hashCode();
	}

	@Override
	public boolean equals(Object aThat) {
		if (this == aThat) {
			return true;
		}
		if (!(aThat instanceof Track)) {
			return false;
		}
		Track that = (Track) aThat;
		return this.asString().equals(that.asString());
	}
}
