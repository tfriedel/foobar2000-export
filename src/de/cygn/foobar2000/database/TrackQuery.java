package de.cygn.foobar2000.database;

import de.cygn.foobar2000.TrackRawRowMapper;

public class TrackQuery {

	public boolean filterCompatible = false;
	public String[] columnDatabaseIdentifiers;

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

	/**
	 * @return the currentKey
	 */
	public String getCurrentKey() {
		return currentKey;
	}

	/**
	 * @param currentKey the currentKey to set
	 */
	public void setCurrentKey(String currentKey) {
		this.currentKey = currentKey;
	}

	/**
	 * @return the currentBPM
	 */
	public float getCurrentBPM() {
		return currentBPM;
	}

	/**
	 * @param currentBPM the currentBPM to set
	 */
	public void setCurrentBPM(float currentBPM) {
		this.currentBPM = currentBPM;
	}

	public enum LastSearchModeType {

		FULLTEXT, PLAYLIST
	};
	public LastSearchModeType lastSearchMode = LastSearchModeType.FULLTEXT;
	private float maxBPM;
	private float minBPM;
	public int startElement;
	public int endElement;
	private String base_query;
	private String fulltext_query;
	private String query = "";
	private String ordering = "";
	private float currentBPM = 0;
	private String currentKey = null;

	public void updateTable() {
	}

	public void loadPlaylist(int playlist_nr) {
		lastSearchMode = LastSearchModeType.PLAYLIST;
		base_query = String.format("FROM PLAYLISTTOTRACKMAPPING P, TRACKS T WHERE P.PLAYLIST_NR = %d AND P.TRACK_ID = T.ID AND T.BPM <= %d AND T.BPM >= %d",
				playlist_nr, (int) getMaxBPM(), (int) getMinBPM());
	}

	public void searchFulltext(String text) {
		fulltext_query = text;
		lastSearchMode = LastSearchModeType.FULLTEXT;
		// 2nd and 3rd parameter of FTL_SEARCH_DATA are limit and offset
		base_query = "FROM FTL_SEARCH_DATA('" + fulltext_query + "', " + "%d, %d) FTL, TRACKS T WHERE FTL.TABLE='TRACKS' AND T.ID=FTL.KEYS[0]"
				+ String.format(" AND T.BPM <= %d AND T.BPM >= %d",
				(int) getMaxBPM(), (int) getMinBPM());
	}

	public String getCountQuery() {
		String countQuery = "select count(*) " + String.format(getBaseQuery(),10000000,0);
		return countQuery;
	}

	public void sortColumn(int column, boolean isSortAsc) {
		if (columnDatabaseIdentifiers[column].equals("DURATION") ||
		    columnDatabaseIdentifiers[column].equals("BPM") || 
		    columnDatabaseIdentifiers[column].equals("ID") || 
		    columnDatabaseIdentifiers[column].equals("RATING") 
			)
			ordering = String.format(" ORDER BY %s %s", columnDatabaseIdentifiers[column], isSortAsc ? "ASC" : "DESC");
		else
			ordering = String.format(" ORDER BY LOWER(%s) %s", columnDatabaseIdentifiers[column], isSortAsc ? "ASC" : "DESC");
	}

	private String getBaseQuery() {
		String sql_filterCompatible = "";
		if (filterCompatible && currentKey != null) {
			sql_filterCompatible = String.format(" AND KEY_COMPATIBLE('%s', T.KEY_START, %s, T.BPM)", currentKey, Float.toString(currentBPM));
		}
		return base_query + sql_filterCompatible;
	}

	public void buildQuery() {
		if (lastSearchMode == LastSearchModeType.PLAYLIST)
			query = String.format(TrackRawRowMapper.select_fields, (currentKey == null) ? "NULL" : "'" + currentKey + "'", Float.toString(currentBPM))
					+ getBaseQuery() + ordering + String.format(" LIMIT %d OFFSET %d", endElement - startElement, startElement);
		else
			query = String.format(TrackRawRowMapper.select_fields, (currentKey == null) ? "NULL" : "'" + currentKey + "'", Float.toString(currentBPM))
					+ String.format(getBaseQuery(),endElement - startElement, startElement) + ordering;

	}

	public String getQuery() {
		buildQuery();
		return query;
	}

	public boolean fullTextMode() {
		return (lastSearchMode == LastSearchModeType.FULLTEXT);
	}

	public boolean PlaylistMode() {
		return (lastSearchMode == LastSearchModeType.PLAYLIST);
	}
}
