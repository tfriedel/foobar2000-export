package de.cygn.foobar2000.database;

public class TrackQuery {

	/**
	 * @return the maxBPM
	 */
	public float getMaxBPM() {
		return maxBPM;
	}

	/**
	 * @param maxBPM the maxBPM to set
	 */
	public void setMaxBPM(float maxBPM) {
		this.maxBPM = maxBPM;
	}

	/**
	 * @return the minBPM
	 */
	public float getMinBPM() {
		return minBPM;
	}

	/**
	 * @param minBPM the minBPM to set
	 */
	public void setMinBPM(float minBPM) {
		this.minBPM = minBPM;
	}

	public enum LastSearchModeType {

		FULLTEXT, PLAYLIST
	};
	public LastSearchModeType lastSearchMode = LastSearchModeType.FULLTEXT;
	private float maxBPM;
	private float minBPM;
	private String base_query;
	private String fulltext_query;
	private String query = "";

	public void updateTable() {
	}

	public String loadPlaylist(int playlist_nr) {
		lastSearchMode = LastSearchModeType.PLAYLIST;
		query = String.format("FROM PLAYLISTTOTRACKMAPPING P, TRACKS T WHERE P.PLAYLIST_NR = %d AND P.TRACK_ID = T.ID AND T.BPM <= %d AND T.BPM >= %d",
				playlist_nr, (int) getMaxBPM(), (int) getMinBPM())
				+ " LIMIT %d OFFSET %d";
		return query;
	}

	public String searchFulltext(String text) {
		fulltext_query = text;
		lastSearchMode = LastSearchModeType.FULLTEXT;
		// 2nd and 3rd parameter of FTL_SEARCH_DATA are limit and offset
		query = "FROM FTL_SEARCH_DATA('" + fulltext_query + "', " + "%d, %d) FTL, TRACKS T WHERE FTL.TABLE='TRACKS' AND T.ID=FTL.KEYS[0]"
				+ String.format(" AND T.BPM <= %d AND T.BPM >= %d",
				(int) getMaxBPM(), (int) getMinBPM());
		return query;
	}

	public String getQuery() {
		return query;
	}

	public boolean fullTextMode() {
		return (lastSearchMode == LastSearchModeType.FULLTEXT);
	}

	public boolean PlaylistMode() {
		return (lastSearchMode == LastSearchModeType.PLAYLIST);
	}
}
