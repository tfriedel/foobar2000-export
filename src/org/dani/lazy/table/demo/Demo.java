package org.dani.lazy.table.demo;

import org.dani.lazy.table.LazyListTableModel;
import org.dani.lazy.util.LazyList;
import org.dani.lazy.util.LazyListService;
import org.dani.lazy.util.SimpleLazyList;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Demo {
    public static void main(String[] args) {
        new Demo();
    }

    public Demo() {
        LazyList<String[]> lazyList = new SimpleLazyList<String[]>(10, new DemoLazyListPeer(80000, 2));
        JTable table = new JTable();
        table.setModel(new DemoLazyListTableModel(lazyList, table));
        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Pure Swing Lazy Table Demo");
        frame.getContentPane().add(scrollPane);
        frame.setSize(200, 500);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @SuppressWarnings("serial")
	protected static class DemoLazyListTableModel extends LazyListTableModel<String[]> {
        public DemoLazyListTableModel(LazyList<String[]> lazyList, JTable table) {
            super(lazyList, table);
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getColumnValue(int column, String[] listElement) {
            return listElement[column];
        }
    }

    protected static class DemoLazyListPeer implements LazyListService<String[]> {
        private final String[][] data;
		private final Connection connection;

        public DemoLazyListPeer(int rows, int columns) {
			try {
			String databaseUrl = "jdbc:h2:mem:account";
			databaseUrl = "jdbc:h2:tcp://localhost/~/test";
			Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(databaseUrl, "sa", "");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
            data = initData(rows, columns);
        }

        protected String[][] initData(int rows, int columns) {
            String[][] data = new String[rows][];

            for (int i = 0; i < data.length; i++) {
                data[i] = new String[columns];
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = i + " / " + j;
                }
            }

            return data;
        }

        public String[][] getData(int startElement, int endElement) {
			String[][] result = new String[endElement - startElement][];
			try {
				final java.sql.Statement stmt = connection.createStatement();
					//ResultSet executeQuery = stmt.executeQuery("SELECT ID,ARTIST,TITLE FROM TRACKS OFFSET " + j + " ROWS FETCH NEXT 1 ROWS ONLY");
				ResultSet executeQuery = stmt.executeQuery(String.format("SELECT ID,ARTIST,TITLE FROM TRACKS WHERE ID>=%d AND ID<%d",startElement+1,endElement+1) );
				int i=0;
				while (executeQuery.next()) {
					result[i++] = new String[] {
						(executeQuery.getString(2) == null) ? "-" : executeQuery.getString(2),
						(executeQuery.getString(3) == null) ? "-" : executeQuery.getString(3)};
				}
			} catch (SQLException ex) {
				Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
			}
			return result;
        }

        public int getSize() {
            return data.length;
        }

        public void add(int position, String[] element) {
            throw new UnsupportedOperationException();
        }

        public void set(int position, String[] element) {
            throw new UnsupportedOperationException();
        }

        public void remove(int position) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }
    }
}
