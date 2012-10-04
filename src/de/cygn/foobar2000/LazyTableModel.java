package de.cygn.foobar2000;

import java.util.HashMap;
import javax.swing.JTable;
import org.dani.lazy.table.LazyListTableModel;
import org.dani.lazy.util.SimpleLazyList;
import org.dani.lazy.util.IndexInterval;

/**
 *
 * @author Thomas Friedel
 */
class LazyTableModel extends LazyListTableModel<Object[]> {

	String[] columnIdentifiers;
	int columnCount;
	private HashMap<String, Integer> columnNr = new HashMap<>();
	SimpleLazyList<Object[]> lazyList;
	boolean clear = true;
	private final LazyTrackList lazyTrackList;
	private JTable table;
	public int compatible_col = -1;

	public LazyTableModel(SimpleLazyList<Object[]> lazyList, JTable table, LazyTrackList lazyTrackList, String[] columnIdentifiers) {
		super(lazyList, table);
		this.columnIdentifiers = columnIdentifiers;
		this.columnCount = columnIdentifiers.length;
		for (int i = 0; i < columnIdentifiers.length; i++) {
			columnNr.put(columnIdentifiers[i], i);
			if ("Compatible".equals(columnIdentifiers[i])) {
				compatible_col = i;
			}
		}
		assert compatible_col >= 0;
		this.lazyList = lazyList;
		this.lazyTrackList = lazyTrackList;
		this.table = table;
		this.fireTableStructureChanged();
	}

	/**
	 * Returns the column name.
	 *
	 * @return a name for this column using the string value of the appropriate
	 * member in <code>columnIdentifiers</code>.   * If <code>columnIdentifiers</code> does not have an entry for this
	 * index, returns the default name provided by the superclass.
	 */
	public String getColumnName(int column) {
		Object id = null;
		// This test is to cover the case when
		// getColumnCount has been subclassed by mistake ...
		if (column < columnIdentifiers.length && (column >= 0)) {
			id = columnIdentifiers[column];
		}
		return (id == null) ? super.getColumnName(column)
				: id.toString();
	}

	public void clear() {
		this.clear = true;
		setQuery("");
		lazyTrackList.setOrdering("");
	}

	;
	
	/**
	 * caches the whole table locally
	 */
	public void fetchAll() {
		lazyList.fetch(new IndexInterval(0, this.getRowCount()));
	}

	public boolean compatibleTrack(int row) {
		Object obj = this.getValueAt(row, compatible_col);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			return false;
		}
	}

	public void setQuery(String query) {
		lazyTrackList.setQuery(query);
		lazyList.updateLazyListService();
		table.setAutoCreateRowSorter(false);
		table.setAutoCreateColumnsFromModel(false);
		this.fireTableDataChanged();
		//this.fireTableStructureChanged();
	}

	public void setQuery(String query, String ordering) {
		lazyTrackList.setOrdering(ordering);
		setQuery(query);
	}

	@Override
	public boolean isCellEditable(int col, int row) {
		return false;
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public Object getColumnValue(int column, Object[] listElement) {
		return listElement[column];
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

}