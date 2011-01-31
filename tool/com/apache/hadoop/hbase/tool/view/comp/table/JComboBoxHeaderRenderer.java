package com.apache.hadoop.hbase.tool.view.comp.table;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.apache.hadoop.hbase.tool.view.comp.HDataType;

public class JComboBoxHeaderRenderer extends JComboBox implements TableCellRenderer{
	
	public JComboBoxHeaderRenderer(HDataType[] list){
		super(list);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setSelectedItem(value);		
		return this;
	}
}
