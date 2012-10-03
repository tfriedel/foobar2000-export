package de.cygn.foobar2000;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Friedel
 */
public class Playlist {

	public enum PlaylistType {NORMAL, AUTO};
	String filename;
	String title;
	PlaylistType type = PlaylistType.NORMAL;
	int id;
	
	private ArrayList<Track> tracks;
	
	public ArrayList<Track> getTracks() {
		if (tracks != null)
			return tracks;
		else {
			File fpl_file = new File(filename);
			try {
				tracks = FPLPlaylist.readPlaylist(fpl_file);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
			}
			return tracks;
		}
	}

	public void setTracks(ArrayList<Track> tracks) {
		this.tracks = tracks;
	}

	public int getNr() {
			String playlist_filename = new File(filename).getName();
			return Integer.valueOf(playlist_filename.substring(0, playlist_filename.length() - 4));
	}

	@Override
	public String toString() {
		String result = title;
		if (type==PlaylistType.AUTO)
			result += " (auto)";
		return result;
	}
}
