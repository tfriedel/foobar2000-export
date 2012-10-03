package de.cygn.foobar2000;

import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.RawRowMapperImpl;
import java.sql.SQLException;

/**
 * for mapping SQL results (string arrays) to Track objects
 */
public class TrackRawRowMapper implements RawRowMapper<Track> {

	public static String select_fields =
			"SELECT T.ID, T.FILENAME, T.FILETIME_IN_MILLIS, T.ARTIST, T.ALBUM_ARTIST, T.TITLE, T.TRACKNUMBER, T.BITRATE, T.ALBUM, T.DATE, T.COMMENT, T.CATNR, T.CODEC, T.PUBLISHER, T.DISCOGS_RELEASE_ID, T.STYLE, T.GENRE, T.BPM, T.RATING, T.KEY_START, T.DURATION, T.FILESIZE, T.ALBUMREPLAYGAIN, T.TRACKREPLAYGAIN, T.ALBUMREPLAYPEAK, T.TRACKREPLAYPEAK, T.INDATABASE ";

	@Override
	public Track mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
		Track t = new Track();
		String[] r = resultColumns;
		t.setId(Integer.valueOf(r[0]));
		t.setFilename(r[1]);
		t.setFiletime_in_millis(Long.valueOf(r[2]));
		t.setArtist(r[3]);
		t.setAlbum_artist(r[4]);
		t.setTitle(r[5]);
		t.setTracknumber(r[6]);
		t.setBitrate(r[7]);
		t.setAlbum(r[8]);
		t.setDate(r[9]);
		t.setComment(r[10]);
		t.setCatnr(r[11]);
		t.setCodec(r[12]);
		t.setPublisher(r[13]);
		t.setDiscogs_release_id(r[14]);
		t.setStyle(r[15]);
		t.setGenre(r[16]);
		t.setBpm(Float.valueOf(r[17]));
		t.setRating(Integer.valueOf(r[18]));
		t.setKey_start(r[19]);
		t.setDuration(Double.valueOf(r[20]));
		t.setFilesize(Integer.valueOf(r[21]));
		t.setAlbumReplayGain(Float.valueOf(r[22]));
		t.setTrackReplayGain(Float.valueOf(r[23]));
		t.setAlbumReplayPeak(Float.valueOf(r[24]));
		t.setTrackReplayPeak(Float.valueOf(r[25]));
		t.setInDatabase(Boolean.valueOf(r[26]));
		return t;
	}
}
