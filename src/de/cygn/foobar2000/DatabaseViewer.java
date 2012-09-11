/*
 */
package de.cygn.foobar2000;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Thomas
 */
public class DatabaseViewer extends javax.swing.JFrame {

	/**
	 * Creates new form DatabaseViewer
	 */
	public DatabaseViewer() {
		initComponents();
		highlightedRows = new TreeSet<Integer>();
		columnIdentifiers = new String[]{"#", "Artist", "Title", "Album", "CatNr", "Label", "Year", "Length", "Key", "BPM", "Rating", "kbit", "Codec", "Path"};
		for (int i = 0; i < columnIdentifiers.length; i++) {
			columnNr.put(columnIdentifiers[i], i);
		}

		//tableModel = new javax.swing.table.DefaultTableModel();
		tableModel = new javax.swing.table.DefaultTableModel() {
			@Override
			public boolean isCellEditable(int col, int row) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == columnNr.get("#")
						|| columnIndex == columnNr.get("Length")) {
					return Integer.class;
				} else {
					return Object.class;
				}
			}
		};

		jTable1.setModel(tableModel);
		int[] preferredWidths = new int[]{35, 108, 151, 140, 47, 84, 40, 51, 37, 40, 52, 32, 40, 359};
		tableModel.setColumnIdentifiers(columnIdentifiers);
		for (int i = 0; i < preferredWidths.length; i++) {
			jTable1.getColumnModel().getColumn(i).setPreferredWidth(preferredWidths[i]);
		}
		TableCellRenderer renderer = new ColoredTableCellRenderer(highlightedRows);
		//jTable1.setDefaultRenderer(Object.class, renderer);
		Enumeration<TableColumn> columns = jTable1.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn col = columns.nextElement();
			col.setCellRenderer(renderer);
		}
		jTable1.getColumnModel().getColumn(columnNr.get("Rating")).setCellRenderer(new ColoredTableCellRenderer(highlightedRows) {
			String[] stars = new String[]{"\u25CC\u25CC\u25CC\u25CC\u25CC", "\u25CF\u25CC\u25CC\u25CC\u25CC", "\u25CF\u25CF\u25CC\u25CC\u25CC", "\u25CF\u25CF\u25CF\u25CC\u25CC", "\u25CF\u25CF\u25CF\u25CF\u25CC", "\u25CF\u25CF\u25CF\u25CF\u25CF"};

			public void setValue(Object value) {
				int rating = (Integer) value;
				rating = Math.min(5, rating);
				rating = Math.max(0, rating);
				assert 0 <= rating && rating <= 5;
				setText(stars[rating]);
			}
		});
		jTable1.getColumnModel().getColumn(columnNr.get("Length")).setCellRenderer(new ColoredTableCellRenderer(highlightedRows) {
			public void setValue(Object value) {
				int duration = (Integer) value;
				setText(Utils.formatIntoHHMMSS(duration));
			}
		});

		// build playlist tree
		TreeModel playlistTreeModel = Playlists.readPlaylists(new File("c:\\Users\\Thomas\\Dropbox\\PortableApps\\foobar2000\\"));
		jTree1.setModel(playlistTreeModel);

		// @todo check resizing properties of table
	}

	private void searchDatabase() {
		try {
			String databaseUrl = "jdbc:h2:mem:account";
			databaseUrl = "jdbc:h2:tcp://localhost/~/test";
			System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
			// create a connection source to our database
			ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl, "sa", "");
			//Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa","");
			// instantiate the dao
			Dao<Track, String> trackDao =
					DaoManager.createDao(connectionSource, Track.class);

			int count = 1;
			Track queryTrack = new Track();
			QueryBuilder<Track, String> queryBuilder = trackDao.queryBuilder();
			String[] keywords = queryField.getText().toLowerCase().split(" ");
			Where<Track, String> where = queryBuilder.where();
			StringBuilder where_string_builder = new StringBuilder();
			for (String word : keywords) {
				where_string_builder.append(
						"(lower(`artist`) LIKE '%" + word + "%' OR lower(`title`) LIKE '%" + word + "%' )");
				where_string_builder.append(" AND ");
			}
			where_string_builder.delete(where_string_builder.length() - 5, where_string_builder.length() - 1);
			where.raw(where_string_builder.toString());
			System.out.println(queryBuilder.prepare());
			CloseableIterator<Track> iterator = trackDao.iterator(queryBuilder.prepare());
			tableModel.setRowCount(0);
			try {
				while (iterator.hasNext() && count++ <= 1000000) {
					Track track = iterator.next();
					addTrackToTable(track, count);
				}
			} finally {
				iterator.close();
			}

			// close the connection source
			connectionSource.close();
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseViewer.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableModel = new javax.swing.table.DefaultTableModel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        queryField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        currentTrackTextField = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(tableModel);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        queryField.setText("Jeff Mills");
        queryField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Query:");

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Playlists");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Default");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("bla");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setRootVisible(false);
        jTree1.setShowsRootHandles(true);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        statusPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPanel.setFocusable(false);
        statusPanel.setPreferredSize(new java.awt.Dimension(0, 16));
        statusPanel.setLayout(new javax.swing.BoxLayout(statusPanel, javax.swing.BoxLayout.LINE_AXIS));

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        statusPanel.add(statusLabel);

        jButton2.setText("Search in key");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(currentTrackTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 350, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 229, Short.MAX_VALUE)
                        .add(jButton2)
                        .add(38, 38, 38)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(jButton1)
                                .add(252, 252, 252))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel1)
                                    .add(queryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))
                    .add(jScrollPane1)))
            .add(statusPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(queryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1)
                    .add(jButton2)
                    .add(currentTrackTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		searchDatabase();
    }//GEN-LAST:event_jButton1ActionPerformed

	private void addTrackToTable(Track track, int count) {
		tableModel.addRow(new Object[]{Integer.valueOf(count), track.getArtist(), track.getTitle(), track.getAlbum(), track.getCatnr(), track.getPublisher(), track.getDate(), Integer.valueOf((int) track.getDuration()), track.getKey_start(), Float.valueOf(track.getBpm()), Integer.valueOf(track.getRating()), track.getBitrate(), track.getCodec(), track.getFilename()});
	}
    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
		loadSelectedPlaylist();
    }//GEN-LAST:event_jTree1MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
		jTable1.getSelectedRow();
    }//GEN-LAST:event_jTable1MouseClicked

	private void updateStatusBar(String text) {
		statusLabel.setText(text);
	}

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
		copySelectedTracksToClipboard();
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void queryFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryFieldActionPerformed
		searchDatabase();
    }//GEN-LAST:event_queryFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		// TODO add your handling code here:
		// highlight tracks which are in compatible key
		searchCompatibleKey();
    }//GEN-LAST:event_jButton2ActionPerformed

	private void copySelectedTracksToClipboard() {
		int[] rows = jTable1.getSelectedRows();
		final ArrayList<File> files = new ArrayList<File>();
		for (int row : rows) {
			row = jTable1.convertRowIndexToModel(row);
			String filename = (String) tableModel.getValueAt(row, columnNr.get("Path"));
			files.add(new File(filename));
		}
		// @todo handle case if no file is selected
		updateStatusBar(String.format("%d tracks added to clipboard.", files.size()));
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new Transferable() {
					@Override
					public DataFlavor[] getTransferDataFlavors() {
						return new DataFlavor[]{DataFlavor.javaFileListFlavor};
					}

					@Override
					public boolean isDataFlavorSupported(DataFlavor flavor) {
						return DataFlavor.javaFileListFlavor.equals(flavor);
					}

					@Override
					public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
						return files;
					}
				}, null);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			/*
			 for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			 if ("Nimbus".equals(info.getName())) {
			 javax.swing.UIManager.setLookAndFeel(info.getClassName());
			 break;
			 }
			 }
			 */
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DatabaseViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DatabaseViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DatabaseViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DatabaseViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new DatabaseViewer().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JTextField currentTrackTextField;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JTextField queryField;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.table.DefaultTableModel tableModel;
    // End of variables declaration//GEN-END:variables
	private HashMap<String, Integer> columnNr = new HashMap<>();
	private String[] columnIdentifiers;
	private Set<Integer> highlightedRows;

	private void searchCompatibleKey() {
		int row = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
		int key_column = columnNr.get("Key");
		int bpm_column = columnNr.get("BPM");
		String key = (String) tableModel.getValueAt(row, key_column);
		float bpm = (Float) tableModel.getValueAt(row, bpm_column);
		StringBuilder sb = new StringBuilder();
		sb.append((String) tableModel.getValueAt(row, columnNr.get("Artist")));
		sb.append(" - ");
		sb.append((String) tableModel.getValueAt(row, columnNr.get("Title")));
		sb.append(" [");
		sb.append(key);
		sb.append(", ");
		sb.append(Float.toString(bpm));;
		sb.append("]");
		currentTrackTextField.setText(sb.toString());
		highlightedRows.clear();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			String key_b = (String) tableModel.getValueAt(i, key_column);
			float bpm_b = (Float) tableModel.getValueAt(i, bpm_column);
			if (KeyCompatibility.compatible(key, key_b, bpm, bpm_b)) {
				highlightedRows.add(i);
			}
		}
		jTable1.repaint();
	}

	private void loadSelectedPlaylist() {
		if (jTree1.getSelectionCount() > 0) {
			highlightedRows.clear();
			TreePath tp = jTree1.getSelectionPath();
			Object object = tp.getLastPathComponent();
			if (object instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
				if (node.getUserObject() instanceof Playlist) {
					Playlist playlist = (Playlist) node.getUserObject();
					updateStatusBar(playlist.toString());
					tableModel.setRowCount(0);
					int count = 1;
					if (playlist.getTracks() != null) {
						for (Track track : playlist.getTracks()) {
							addTrackToTable(track, count);
							count++;
						}
					}
				}
			}
		}
	}
}
