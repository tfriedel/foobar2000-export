package de.cygn.foobar2000;

import com.j256.ormlite.field.DatabaseField;

/**
 *
 * @author Thomas Friedel
 */
public class LiteTrack {
	@DatabaseField
	public String filename;
	@DatabaseField
	public String tracknumber;
	@DatabaseField
	public int playlist_nr;
	@DatabaseField
	public int position;
	
	public LiteTrack () {
	}
	public LiteTrack(String filename, String tracknumber, int playlist_nr, int position) {
		this.filename = filename;
		this.tracknumber = tracknumber;
		this.playlist_nr = playlist_nr;
		this.position = position;
	}
}
