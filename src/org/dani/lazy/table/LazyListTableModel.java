package org.dani.lazy.table;

import org.dani.lazy.util.IndexInterval;
import org.dani.lazy.util.LazyList;
import org.dani.lazy.util.OnLoadEvent;
import org.dani.lazy.util.OnLoadListener;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public abstract class LazyListTableModel<T> extends AbstractTableModel {
    private final LazyList<T> lazyList;
    private final List<JTable> dependentTables;

    public LazyListTableModel(LazyList<T> lazyList, JTable table) {
        this(lazyList);
        addDependentTable(table);
    }

    public LazyListTableModel(LazyList<T> lazyList) {
        this.lazyList = lazyList;
        dependentTables = new ArrayList<JTable>();
        addOnLoadListener();
    }

    protected void addOnLoadListener() {
        getLazyList().addOnLoadListener(createOnLoadListener());
    }

    protected OnLoadListener createOnLoadListener() {
        return new OnLoadListener() {
            public void elementLoaded(final OnLoadEvent event) {
                System.out.println("Work...");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        for (TableModelListener listener : getTableModelListeners()) {
                            listener.tableChanged(new TableModelEvent(
                                    LazyListTableModel.this, event
                                            .getIndexInterval().getStart(),
                                    event.getIndexInterval().getEnd()-1));
                        }
                        loadAllVisibleRows();
                    }
                });
            }
        };
    }

    protected LazyList<T> getLazyList() {
        return lazyList;
    }

    public void addDependentTable(JTable table) {
        dependentTables.add(table);
    }

    public void removeDependentTable(JTable table) {
        dependentTables.remove(table);
    }

    protected void loadAllVisibleRows() {
        for (JTable table : dependentTables) {
            loadAllVisibleRowsForTable(table);
        }
    }

    protected void loadAllVisibleRowsForTable(JTable table) {
        IndexInterval startEnd = getVisibleRows(table);
        for (int i = startEnd.getStart(); i <= startEnd.getEnd(); i++) {
            getLazyList().getAsynchronous(i);
        }
    }

    protected IndexInterval getVisibleRows(JTable table) {
        Rectangle rect = table.getVisibleRect();
        Point location = rect.getLocation();
        int startRow = table.rowAtPoint(location);
        location.y += rect.getHeight() - 1;
        int endRow = table.rowAtPoint(location);
        return new IndexInterval(startRow, endRow);
    }

    public int getRowCount() {
        return getLazyList().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (getLazyList().isLoaded(rowIndex)) {
            return getColumnValue(columnIndex, getLazyList().get(rowIndex));
        } else {
            getLazyList().getAsynchronous(rowIndex);
            return getDummyValueAt(rowIndex, columnIndex);
        }
    }

    public Object getDummyValueAt(int rowIndex, int columnIndex) {
        return "";
    }

    public abstract Object getColumnValue(int columnIndex, T listElement);
}
