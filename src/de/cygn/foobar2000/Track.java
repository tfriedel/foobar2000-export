package de.cygn.foobar2000;

import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Track implements Serializable{

	String filename;
	long filetime_in_millis;
	/** duration in seconds */
	double duration;
	int filesize;
	float albumReplayGain;
	float trackReplayGain;
	float albumReplayPeak;
	float trackReplayPeak;
	HashMap<String, ArrayList<String>> properties;

	public String toString() {
		return String.format("%s, %s", filename, properties);
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
}
