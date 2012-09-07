package de.cygn.foobar2000;

import com.j256.ormlite.field.DataType;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tracks")
public class Track {

	@DatabaseField
	String filename;
	@DatabaseField
	long filetime_in_millis;
	@DatabaseField
	String artist;
	@DatabaseField
	String album_artist;
	@DatabaseField
	String title;
	@DatabaseField
	String tracknumber;
	@DatabaseField
	String bitrate;
	@DatabaseField
	String album;
	@DatabaseField
	String date;
	@DatabaseField(dataType=DataType.LONG_STRING)
	String comment;
	@DatabaseField
	String catnr;
	@DatabaseField
	String codec;
	@DatabaseField
	String publisher;
	@DatabaseField
	String discogs_release_id;
	@DatabaseField
	String style;
	@DatabaseField
	String genre;
	@DatabaseField
	float bpm;
	@DatabaseField
	int rating;
	@DatabaseField
	String key_start;
	/**
	 * duration in seconds
	 */
	@DatabaseField
	double duration;
	@DatabaseField
	int filesize;
	@DatabaseField
	float albumReplayGain;
	@DatabaseField
	float trackReplayGain;
	@DatabaseField
	float albumReplayPeak;
	@DatabaseField
	float trackReplayPeak;
	HashMap<String, ArrayList<String>> properties;

	public String toString() {
		return String.format("%s, %s", filename, properties);
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
	 * @param s
	 * @return 
	 */
	public static String makeFit(String s) {
		if (s.length()>255)
			s = s.substring(0,255);
		return s;
	}
	
	void updateFields() {
		if (properties.containsKey("artist")) {
			artist = makeFit(properties.get("artist").get(0));
		}
		if (properties.containsKey("title")) {
			title = makeFit(properties.get("title").get(0));
		}
		if (properties.containsKey("album")) {
			album = makeFit(properties.get("album").get(0));
		}
		if (properties.containsKey("tracknumber")) {
			tracknumber = makeFit(properties.get("tracknumber").get(0));
		}
		if (properties.containsKey("bpm")) {
			try {
				bpm = Float.valueOf(properties.get("bpm").get(0));
			} catch (NumberFormatException e) {
				String bpm_string = properties.get("bpm").get(0);
				bpm_string = bpm_string.replace(",", ".");
				try {
					bpm = Float.valueOf(bpm_string);
				} catch (NumberFormatException e2) {
					
				}
			}
		}
		if (properties.containsKey("key_start")) {
			key_start = makeFit(properties.get("key_start").get(0));
		}
		if (properties.containsKey("date")) {
			date = makeFit(properties.get("date").get(0));
		}
		if (properties.containsKey("CATNR")) {
			catnr = makeFit(properties.get("CATNR").get(0));
		}
		if (properties.containsKey("publisher")) {
			publisher = makeFit(properties.get("publisher").get(0));
		}
		if (properties.containsKey("DISCOGS_RELEASE_ID")) {
			discogs_release_id = makeFit(properties.get("DISCOGS_RELEASE_ID").get(0));
		}
		if (properties.containsKey("STYLE")) {
			style = makeFit(properties.get("STYLE").get(0));
		}
		if (properties.containsKey("bitrate")) {
			bitrate = makeFit(properties.get("bitrate").get(0));
		}
		if (properties.containsKey("comment")) {
			comment = makeFit(properties.get("comment").get(0));
		}
		if (properties.containsKey("codec")) {
			codec = makeFit(properties.get("codec").get(0));
		}
		if (properties.containsKey("genre")) {
			genre = makeFit(properties.get("genre").get(0));
		}
		if (properties.containsKey("ALBUM ARTIST")) {
			album_artist = makeFit(properties.get("ALBUM ARTIST").get(0));
		}
		if (properties.containsKey("RATING")) {
			try {
				rating = Integer.valueOf(properties.get("RATING").get(0)); // @todo make failsafe
			} catch (NumberFormatException e) {
				
			}
		}
	}
}
