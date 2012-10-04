package de.cygn.foobar2000;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import de.cygn.foobar2000.DatabaseViewer.ColumnListener;
import de.cygn.foobar2000.database.TrackQuery;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.dani.lazy.util.SimpleLazyList;

/**
 *
 * @author Thomas
 */
public class DatabaseViewer extends javax.swing.JFrame {

	private LazyTrackList listService;
	private TrackQuery trackQuery;
	private String[] columnDatabaseIdentifiers;

	/**
	 * Creates new form DatabaseViewer
	 */
	public DatabaseViewer() {
		initComponents();
		customInitComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        playlistTable = new javax.swing.JTable();
        queryField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        playlistTree = new javax.swing.JTree();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        currentTrackTextField = new javax.swing.JTextField();
        filterField = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        filterLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        minBPM = new javax.swing.JSpinner();
        maxBPM = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        importPlaylistsMenuItem = new javax.swing.JMenuItem();
        importFB2kMenuItem = new javax.swing.JMenuItem();
        importFB2kIncMenuItem = new javax.swing.JMenuItem();
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

        playlistTable.setToolTipText("");
        playlistTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane1.setViewportView(playlistTable);

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
        playlistTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        playlistTree.setRootVisible(false);
        playlistTree.setShowsRootHandles(true);
        playlistTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playlistTreeMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(playlistTree);

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

        currentTrackTextField.setBackground(new java.awt.Color(204, 255, 204));

        jCheckBox1.setText("hide other");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        filterLabel.setText("Filter:");

        jLabel2.setText("current Track:");

        jLabel3.setText("Min BPM");

        minBPM.setModel(new javax.swing.SpinnerNumberModel(0, 0, 250, 5));

        maxBPM.setModel(new javax.swing.SpinnerNumberModel(200, 0, 250, 5));

        jLabel4.setText("Max BPM");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(minBPM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel4)
                .add(1, 1, 1)
                .add(maxBPM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(minBPM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(maxBPM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(jLabel4))
                .addContainerGap())
        );

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

        importPlaylistsMenuItem.setText("Import playlists");
        importPlaylistsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playlistsImportActionPerformed(evt);
            }
        });
        fileMenu.add(importPlaylistsMenuItem);

        importFB2kMenuItem.setText("Import Fb2k Database");
        importFB2kMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFB2kMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importFB2kMenuItem);

        importFB2kIncMenuItem.setText("Import Fb2k Database incrementally");
        importFB2kIncMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFB2kIncMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importFB2kIncMenuItem);

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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(currentTrackTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 286, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(42, 42, 42)
                                .add(filterLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(filterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jCheckBox1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 51, Short.MAX_VALUE)
                                .add(jButton1)
                                .add(252, 252, 252))
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel1)
                                    .add(queryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))))
            .add(statusPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(queryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1)
                    .add(currentTrackTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(filterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2)
                    .add(filterLabel)
                    .add(jCheckBox1)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
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
		searchDatabase(queryField.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void playlistTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playlistTreeMouseClicked
		loadSelectedPlaylist();
    }//GEN-LAST:event_playlistTreeMouseClicked

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
		copySelectedTracksToClipboard();
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void queryFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryFieldActionPerformed
		searchDatabase(queryField.getText());
    }//GEN-LAST:event_queryFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		// highlight tracks which are in compatible key
		searchCompatibleKey();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void playlistsImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playlistsImportActionPerformed
		playlistsImport();
    }//GEN-LAST:event_playlistsImportActionPerformed

    private void importFB2kMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFB2kMenuItemActionPerformed
		FPLPlaylist.fullImport();
    }//GEN-LAST:event_importFB2kMenuItemActionPerformed

    private void importFB2kIncMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFB2kIncMenuItemActionPerformed
		FPLPlaylist.incrementalImport();
    }//GEN-LAST:event_importFB2kIncMenuItemActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
		trackQuery.filterCompatible = jCheckBox1.isSelected();
		updateTable();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

	private void playlistTableMouseDoubleClicked(java.awt.event.MouseEvent evt) {
		updateCurrentTrack(playlistTable.getSelectedRow());
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
    private javax.swing.JTextField filterField;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importFB2kIncMenuItem;
    private javax.swing.JMenuItem importFB2kMenuItem;
    private javax.swing.JMenuItem importPlaylistsMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner maxBPM;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JSpinner minBPM;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JTable playlistTable;
    private javax.swing.JTree playlistTree;
    private javax.swing.JTextField queryField;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
	private int tableWindowSize = 2000;
	private HashMap<String, Integer> columnNr = new HashMap<>();
	private LazyTableModel tableModel;
	private String[] columnIdentifiers;
	private Set<Integer> highlightedRows;
	private int pathColumnNr;
	private Track currentTrack;
	private int currentRow;
	private SimpleLazyList<Object[]> lazyList;
	private TableRowSorter<TableModel> sorter;
	private RowFilter<TableModel, Object> rowfilter;
	private RowFilter<TableModel, Object> stringMatchRowFilter;
	private RowFilter<TableModel, Object> inKeyRowFilter;

	protected int sortCol = 0;
	protected boolean isSortAsc = true;
	ArrayList<RowFilter<TableModel, Object>> filters;

	private void searchCompatibleKey() {
		if (currentTrack != null) {
			int selectedRow = playlistTable.getSelectedRow();
			trackQuery.setCurrentKey(currentTrack.getKey_start());
			trackQuery.setCurrentBPM(currentTrack.getBpm());
			updateTable();
			playlistTable.clearSelection();
			playlistTable.addRowSelectionInterval(selectedRow, selectedRow);
			//@todo: highlight current track somehow
			//sorter.sort();
			//playlistTable.repaint();
		}
	}

	private void updateTable() {
		tableModel.setQuery(trackQuery.getQuery());
		tableModel.fireTableStructureChanged();
		sorter.sort();
		updateStatusBar(Integer.toString(tableModel.getRowCount()) + " tracks found.");
	}

	private void loadSelectedPlaylist() {
		if (playlistTree.getSelectionCount() > 0) {
			highlightedRows.clear();
			TreePath tp = playlistTree.getSelectionPath();
			Object object = tp.getLastPathComponent();
			if (object instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
				if (node.getUserObject() instanceof Playlist) {
					Playlist playlist = (Playlist) node.getUserObject();
					int playlist_nr;
					try {

						playlist_nr = playlist.getNr();
						updateStatusBar(playlist.toString());
						// @todo check if clearing table model necessary
						//tableModel.clear();

						//tableModel.setQuery(query, "ORDER BY P.TRACK_NR");
						trackQuery.setMinBPM((Integer) minBPM.getModel().getValue());
						trackQuery.setMaxBPM((Integer) maxBPM.getModel().getValue());

						trackQuery.loadPlaylist(playlist_nr);
						updateTable();

					} catch (NumberFormatException e) {
					}
				}
			}
		}
	}

	private void updateStatusBar(String text) {
		statusLabel.setText(text);
	}

	private void configurePlaylistTable() {
		sorter = new TableRowSorter<TableModel>(tableModel);
		playlistTable.setModel(tableModel);
		//playlistTable.setRowSorter(sorter);
		int[] preferredWidths = new int[]{35, 108, 151, 140, 47, 84, 40, 51, 37, 40, 52, 32, 40, 359};
		//tableModel.setColumnIdentifiers(columnIdentifiers);
		for (int i = 0; i < preferredWidths.length; i++) {
			playlistTable.getColumnModel().getColumn(i).setPreferredWidth(preferredWidths[i]);
		}
		TableCellRenderer renderer = new ColoredTableCellRenderer(highlightedRows);
		Enumeration<TableColumn> columns = playlistTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn col = columns.nextElement();
			col.setCellRenderer(renderer);
		}
		playlistTable.getColumnModel().getColumn(columnNr.get("Rating")).setCellRenderer(new ColoredTableCellRenderer(highlightedRows) {
			String[] stars = new String[]{"\u25CC\u25CC\u25CC\u25CC\u25CC", "\u25CF\u25CC\u25CC\u25CC\u25CC", "\u25CF\u25CF\u25CC\u25CC\u25CC", "\u25CF\u25CF\u25CF\u25CC\u25CC", "\u25CF\u25CF\u25CF\u25CF\u25CC", "\u25CF\u25CF\u25CF\u25CF\u25CF"};

			public void setValue(Object value) {
				if (value instanceof Integer) {
					int rating = (Integer) value;
					rating = Math.min(5, rating);
					rating = Math.max(0, rating);
					assert 0 <= rating && rating <= 5;
					setText(stars[rating]);
				}
			}
		});
		playlistTable.getColumnModel().getColumn(columnNr.get("Length")).setCellRenderer(new ColoredTableCellRenderer(highlightedRows) {
			public void setValue(Object value) {
				if (value instanceof Integer) {
					int duration = (Integer) value;
					setText(Utils.formatIntoHHMMSS(duration));
				}
			}
		});

		playlistTable.removeColumn(playlistTable.getColumnModel().getColumn(columnNr.get("Compatible")));
		playlistTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					playlistTableMouseDoubleClicked(evt);
				}
			}
		});
		filterField.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						newFilter();
					}

					public void insertUpdate(DocumentEvent e) {
						newFilter();
					}

					public void removeUpdate(DocumentEvent e) {
						newFilter();
					}
				});
		queryField.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						queryFieldChanged();
					}

					public void insertUpdate(DocumentEvent e) {
						queryFieldChanged();
					}

					public void removeUpdate(DocumentEvent e) {
						queryFieldChanged();
					}
				});

		inKeyRowFilter = new RowFilter<TableModel, Object>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Object> entry) {
				if (jCheckBox1.isSelected()) {
					Object obj = entry.getValue(tableModel.compatible_col);
					if (obj instanceof Boolean) {
						if ((Boolean)entry.getValue(tableModel.compatible_col)) {
							return true;
						} else {
							return false;
						}
					} else
						return true;
				} else {
					return true;
				}
			}
		};

		stringMatchRowFilter = RowFilter.regexFilter("");
		filters = new ArrayList<>();
		//filters.add(inKeyRowFilter);
		filters.add(stringMatchRowFilter);
		rowfilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowfilter);

		JTableHeader header = playlistTable.getTableHeader();
		header.addMouseListener(new ColumnListener(playlistTable));
		header.setReorderingAllowed(false);
	}

	private void customInitComponents() {
		String iconPath = "/de/cygn/foobar2000/res/icon.png";
		this.setIconImage(new ImageIcon(getClass().getResource(iconPath)).getImage());
		highlightedRows = new TreeSet<Integer>();
		columnIdentifiers = new String[]{"#", "Artist", "Title", "Album", "CatNr", "Label", "Year", "Length", "Key", "BPM", "Rating", "kbit", "Codec", "Path", "Compatible"};
		columnDatabaseIdentifiers = new String[]{Track.ID_FIELD_NAME, Track.ARTIST_FIELD_NAME, Track.TITLE_FIELD_NAME, Track.ALBUM_FIELD_NAME, Track.CATNR_FIELD_NAME, Track.PUBLISHER_FIELD_NAME, Track.DATE_FIELD_NAME, Track.DURATION_FIELD_NAME, Track.KEY_START_FIELD_NAME, Track.BPM_FIELD_NAME, Track.RATING_FIELD_NAME, Track.BITRATE_FIELD_NAME, Track.CODEC_FIELD_NAME, Track.FILENAME_FIELD_NAME, ""};
		for (int i = 0; i < columnIdentifiers.length; i++) {
			columnNr.put(columnIdentifiers[i], i);
		}
		pathColumnNr = columnNr.get("Path");

		trackQuery = new TrackQuery();
		trackQuery.columnDatabaseIdentifiers = columnDatabaseIdentifiers;
		//tableModel = new javax.swing.table.DefaultTableModel();
		listService = new LazyTrackList(columnIdentifiers, trackQuery);
		lazyList = new SimpleLazyList<Object[]>(tableWindowSize, listService);
		tableModel = new LazyTableModel(lazyList, playlistTable, listService, columnIdentifiers);
//        tableModel = new LazyTableModel(lazyList, playlistTable, listService);
		playlistTable.setModel(tableModel);
		configurePlaylistTable();

		// build playlist tree

		TreeModel playlistTreeModel = Playlists.readPlaylists(new File(FPLPlaylist.fb2kDir));
		playlistTree.setModel(playlistTreeModel);

		// @todo check resizing properties of table
	}

	private void searchDatabase(String query_text) {
		trackQuery.setMinBPM((Integer) minBPM.getModel().getValue());
		trackQuery.setMaxBPM((Integer) maxBPM.getModel().getValue());

		trackQuery.searchFulltext(query_text);
		updateTable();
	}

	private Track getTrackAtRow(int row) {
		row = playlistTable.convertRowIndexToModel(row);
		return (Track) tableModel.getValueAt(row, pathColumnNr);
	}

	private void updateCurrentTrack(int row) {
		currentTrack = getTrackAtRow(row);
		currentRow = playlistTable.convertRowIndexToModel(row);
		StringBuilder sb = new StringBuilder();
		sb.append(currentTrack.getArtist());
		sb.append(" - ");
		sb.append(currentTrack.getTitle());
		sb.append(" [");
		sb.append(currentTrack.getKey_start());
		sb.append(", ");
		sb.append(Float.toString(currentTrack.getBpm()));;
		sb.append("]");
		currentTrackTextField.setText(sb.toString());
		searchCompatibleKey();
	}

	private void copySelectedTracksToClipboard() {
		int[] rows = playlistTable.getSelectedRows();
		if (rows.length == 0) {
			return;
		}
		final ArrayList<File> files = new ArrayList<File>();
		Track track = null;
		for (int row : rows) {
			row = playlistTable.convertRowIndexToModel(row);
			track = ((Track) tableModel.getValueAt(row, columnNr.get("Path")));
			files.add(new File(track.getFilename()));
		}
		if (files.size() == 1) {
			updateStatusBar(String.format("%s - %s added to clipboard.", track.getArtist(), track.getTitle()));
		} else {
			updateStatusBar(String.format("%d tracks added to clipboard.", files.size()));
		}
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

	private void newFilter() {
		final String whitespaceChars = " \t\n\r\f+\"*%&/()=?'!,.;:-_#@|^~`{}[]<>\\";
		// cut out trailing whitespacecharacters
		String s = filterField.getText();
		int i = s.length() - 1;
		while (i >= 0) {
			if (!whitespaceChars.contains("" + s.charAt(i))) {
				break;
			} else {
				i--;
			}
		}
		s = s.substring(0, i + 1);
		if (!Utils.isEmpty(s)) {
			if (trackQuery.fullTextMode()) {
				searchDatabase(queryField.getText() + " " + s + "*");
			} else if (trackQuery.PlaylistMode()) {
				//@todo fix this
				//loadSelectedPlaylist(s);
			}
		}
		/*
		 RowFilter<TableModel, Object> rf = null;
		 //If current expression doesn't parse, don't update.
		
		 try {
		 rf = RowFilter.regexFilter("(?i)" + filterField.getText());
		 filters.set(1, rf);
		 //sorter.sort();
		 rowfilter = RowFilter.andFilter(filters);
		 sorter.setRowFilter(rowfilter);
		 } catch (java.util.regex.PatternSyntaxException e) {
		 return;
		 }
		 */

	}

	private void queryFieldChanged() {
//		final String whitespaceChars = " \t\n\r\f+\"*%&/()=?'!,.;:-_#@|^~`{}[]<>\\";
//
//		if (queryField.getText().length() > 2 ) {
//			String query = queryField.getText();
//			char c = query.charAt(query.length() - 1);
//			String s = "" + c;
//			if (!whitespaceChars.contains(s)) {
//				query = query + "*";
//			}
//			searchDatabase(query, 100);
//		}
	}

	private ArrayList<Playlist> getLeafs(JTree tree) {
		TreeModel model = tree.getModel();
		ArrayList<Playlist> list = new ArrayList<>();

		if (model != null) {
			Object root = model.getRoot();
			walk(model, root, list);
		}
		return list;
	}

	private void walk(TreeModel model, Object o, ArrayList<Playlist> list) {
		int cc;
		cc = model.getChildCount(o);
		for (int i = 0; i < cc; i++) {
			Object child = model.getChild(o, i);
			if (model.isLeaf(child)) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) child;
				if (node.getUserObject() instanceof Playlist) {
					Playlist playlist = (Playlist) node.getUserObject();
					list.add(playlist);
				}
			} else {
				// folder
				walk(model, child, list);
			}
		}
	}

	private void playlistsImport() {
		try {
			Database db = new Database();
			Dao<Track, String> trackDao = db.getTrackDao();
			Dao<PlaylistToTrackMapping, String> plToTrackDao = DaoManager.createDao(db.getConnectionSource(), PlaylistToTrackMapping.class);
			Dao<LiteTrack, String> liteTrackDao = DaoManager.createDao(db.getConnectionSource(), LiteTrack.class);
			ArrayList<Playlist> playlists = getLeafs(playlistTree);
			TableUtils.dropTable(db.getConnectionSource(), PlaylistToTrackMapping.class, true);
			TableUtils.createTable(db.getConnectionSource(), PlaylistToTrackMapping.class);
			TableUtils.dropTable(db.getConnectionSource(), LiteTrack.class, true);
			TableUtils.createTable(db.getConnectionSource(), LiteTrack.class);
			for (Playlist playlist : playlists) {
				int playlist_nr;
				try {
					playlist_nr = playlist.getNr();
				} catch (NumberFormatException e) {
					throw e;
				}

				System.out.printf("Playlist nr: %d %s\n", playlist_nr, playlist.title);

				int i = 0;
				Track queryTrack = new Track();
				for (Track track : playlist.getTracks()) {
					LiteTrack liteTrack = new LiteTrack(track.getFilename(), track.getTracknumber(), playlist_nr, i);
					liteTrackDao.create(liteTrack);

					// SELECT  LITETRACk.FILENAME, ID, PLAYLIST_NR FROM LITETRACK LEFT OUTER JOIN TRACKS
					// ON LITETRACK.FILENAME = TRACKS.FILENAME AND LITETRACK.TRACKNUMBER = TRACKS.TRACKNUMBER
//SELECT  L.TRACKNUMBER, T.TRACKNUMBER, L.FILENAME, ID, PLAYLIST_NR FROM LITETRACK L LEFT OUTER JOIN TRACKS T
//ON L.FILENAME = T.FILENAME AND (L.TRACKNUMBER=T.TRACKNUMBER OR (L.TRACKNUMBER IS NULL AND T.TRACKNUMBER IS NULL))

					/*
					 try {
					 queryTrack.setFilename(track.getFilename());
					 queryTrack.setTracknumber(track.getTracknumber());
					 List<Track> matchingTracks = trackDao.queryForMatchingArgs(queryTrack);
					 //@todo maybe use joins
					 if (null != matchingTracks && matchingTracks.size() > 0) {
					 // add track
					 track = matchingTracks.get(0);
					 } else {
					 // add track to database but mark it as not in database because it is not in foobar database
					 track.setInDatabase(false);
					 trackDao.create(track);
					 }
					 PlaylistToTrackMapping mapping = new PlaylistToTrackMapping(playlist_nr, track.getId(), i);
					 if (plToTrackDao.update(mapping) == 0) {
					 plToTrackDao.create(mapping);
					 }
					 } catch (SQLException ex) {
					 Logger.getLogger(DatabaseViewer.class.getName()).log(Level.SEVERE, null, ex);
					 }
					 */
					i++;
				}
			}
			DatabaseConnection conn = db.getConnectionSource().getReadWriteConnection();
			conn.update(
					"INSERT INTO PLAYLISTTOTRACKMAPPING(PLAYLIST_NR, TRACK_ID, TRACK_NR) SELECT L.PLAYLIST_NR, T.ID, L.POSITION FROM LITETRACK L INNER JOIN TRACKS T ON L.FILENAME = T.FILENAME AND (L.TRACKNUMBER=T.TRACKNUMBER OR (L.TRACKNUMBER IS NULL AND T.TRACKNUMBER IS NULL))", null, null);
			conn.close();
			db.close();
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseViewer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	class ColumnListener extends MouseAdapter {

		protected JTable table;
		public ColumnListener(JTable t) {
			table = t;
		}

		public void mouseClicked(MouseEvent e) {
			TableColumnModel colModel = table.getColumnModel();
			int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
			int modelIndex = colModel.getColumn(columnModelIndex)
					.getModelIndex();

			if (modelIndex < 0) {
				return;
			}
			if (sortCol == modelIndex) {
				isSortAsc = !isSortAsc;
			} else {
				sortCol = modelIndex;
			}

			for (int i = 0; i < table.getColumnCount(); i++) {
				TableColumn column = colModel.getColumn(i);
				column.setHeaderValue(tableModel.getColumnName(column.getModelIndex()));
			}
			trackQuery.sortColumn(columnModelIndex, isSortAsc);
			updateTable();
			table.getTableHeader().repaint();
		}
	}
}
