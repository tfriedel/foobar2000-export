package de.cygn.foobar2000;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

/**
 *
 * @author Thomas Friedel
 */
public class Database {
	//private String databaseUrl = "jdbc:h2:tcp://localhost/~/test";
	private String databaseUrl = "jdbc:h2:tcp://localhost/I:/database/fb2k";
	private ConnectionSource connectionSource;
	private String username = "sa";
	private String password = "";
	private Dao<Track, String> trackDao;
	
	public Database() throws SQLException {
		System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
		connectionSource = new JdbcConnectionSource(databaseUrl, username, password);
	}

	public ConnectionSource getConnectionSource() throws SQLException {
		if (!connectionSource.isOpen())  {
			connectionSource = new JdbcConnectionSource(databaseUrl, username, password);
		}
		return connectionSource;
	}
	
	public Dao<Track, String> getTrackDao () throws SQLException {
		if (trackDao == null) 
			trackDao = DaoManager.createDao(connectionSource, Track.class);
		return trackDao;
	}

	public void close() throws SQLException {
		if (connectionSource.isOpen())
			connectionSource.close();
		trackDao = null;
	}

}
