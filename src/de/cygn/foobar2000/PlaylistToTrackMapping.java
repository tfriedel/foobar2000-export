package de.cygn.foobar2000;

import com.j256.ormlite.field.DatabaseField;

/**
 *
 * @author Thomas Friedel
 */
public class PlaylistToTrackMapping {
	@DatabaseField
	public int playlist_nr;
	@DatabaseField
	public int track_id;
	@DatabaseField
	public int track_nr;
	
	PlaylistToTrackMapping() {
		
	}
	
	PlaylistToTrackMapping(int playlist_nr, int track_id, int track_nr) {
		this.playlist_nr = playlist_nr;
		this.track_id = track_id;
		this.track_nr = track_nr;
	}
}
