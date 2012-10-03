package de.cygn.foobar2000;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dani.lazy.table.demo.Demo;
import org.dani.lazy.util.LazyListService;

public class LazyTrackList implements LazyListService<Object[]> {

	Dao<Track, String> trackDao;
	String query;
	int columns;
	int rows;
	Object[] empty = {Integer.valueOf(0), "", "", "", "", "", "", Integer.valueOf(0), "", Float.valueOf(0), Integer.valueOf(0), "", "", ""};
	private final String[] columnIdentifiers;
	private String ordering;

	public LazyTrackList(String[] columnIdentifiers) {
		try {
			Database db = new Database();
			// @todo close database when destroyed
			this.trackDao = db.getTrackDao();
			this.columnIdentifiers = columnIdentifiers;
			this.columns = columnIdentifiers.length;
			rows = 0;
			ordering = "";
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object[][] getData(int startElement, int endElement) {
		Object[][] result = new Object[endElement - startElement ][];
		if (Utils.isEmpty(query)) {
			return new Object[endElement - startElement][columns];
		}
		try {
			GenericRawResults<Track> rawResults =
					trackDao.queryRaw(
					TrackRawRowMapper.select_fields
					+ String.format(query, endElement - startElement, startElement) + " " + ordering,
					new TrackRawRowMapper());
			int i = 0;
			List<Track> tracks = rawResults.getResults();
			for (Track track : tracks) {
				Object[] a = {Integer.valueOf(track.getId()), track.getArtist(), track.getTitle(), track.getAlbum(), track.getCatnr(), track.getPublisher(), track.getDate(), Integer.valueOf((int) track.getDuration()), track.getKey_start(), Float.valueOf(track.getBpm()), Integer.valueOf(track.getRating()), track.getBitrate(), track.getCodec(), track};
				result[i++] = a;
			}
		} catch (SQLException ex) {
			Logger.getLogger(LazyTrackList.class.getName()).log(Level.SEVERE, null, ex);
			result = new Object[endElement - startElement][columns];
		}
		return result;
	}

	public int getSize() {
		// @todo fix this, get size from db and cache it maybe
		return rows;
	}

	public void setQuery(String query) {
		this.query = query;
		GenericRawResults<String[]> rawResults;
		try {
			rawResults = trackDao.queryRaw(
					"select count(*) " + String.format(query, 1000000, 0));
			// there should be 1 result
			List<String[]> results = rawResults.getResults();
			// the results array should have 1 value
			String[] resultArray = results.get(0);
			rows = Integer.valueOf(resultArray[0]);
		} catch (SQLException ex) {
			Logger.getLogger(LazyTrackList.class.getName()).log(Level.SEVERE, null, ex);
			rows = 0;
		}
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	@Override
	public void set(int position, Object[] element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void add(int position, Object[] element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void remove(int position) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
