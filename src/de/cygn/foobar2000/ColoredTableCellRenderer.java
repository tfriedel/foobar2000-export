package de.cygn.foobar2000;

import java.awt.Color;
import java.awt.Component;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColoredTableCellRenderer extends DefaultTableCellRenderer {

	Set<Integer> highlightedRows;

	public ColoredTableCellRenderer() {
		super();
		highlightedRows = new TreeSet<>();
	}

	public ColoredTableCellRenderer(Set<Integer> highlightedRows) {
		super();
		this.highlightedRows = highlightedRows;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		if (((LazyTableModel) table.getModel()).compatibleTrack(table.convertRowIndexToModel(row))) {
			setBackground(Color.YELLOW);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		} else {
			setBackground(null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		}
		/*
		 if (highlightedRows.contains(table.convertRowIndexToModel(row))) {
		 row = table.convertRowIndexToModel(row);
		 setBackground(Color.YELLOW);
		 return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		 } else {
		 setBackground(null);
		 return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		 }
		 */
	}
}