/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cygn.foobar2000;

import java.util.List;
import java.sql.*;

/**
 *
 * @author Thomas
 */
public class DataBaseTest	{
	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa","");
		String query = "select * from TEST";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			System.out.println(rs.getString("NAME"));
			System.out.println(rs.getInt("ID"));
		}
		conn.close();
	}
	
}
