package com.apache.hadoop.hbase.tool.view.comp.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.apache.hadoop.hbase.tool.view.comp.HDataType;

public class ComboBoxTypeTableModel extends AbstractTableModel {
	private String[] cols = {"Column", "Type"};
	protected List<HTableColumn> fdata = new ArrayList<HTableColumn>();
	protected List<HDataType> sdata = new ArrayList<HDataType>();
	
	public ComboBoxTypeTableModel(){
		
	}
	
	public void setData(List<HTableColumn> list){
		this.fdata.clear();
		this.sdata.clear();
		
		this.fdata.addAll(list);
		int i=0;
		for(HTableColumn col:fdata){
			sdata.add(i,HDataType.STRING);
			i++;
		}
	}
	@Override
	public int getRowCount() {
		return fdata.size();
	}
	@Override
	public int getColumnCount() {
		return cols.length;
	}
	@Override
	public Object getValueAt(int row, int column) {
		HTableColumn hc = fdata.get(row);
		if(column==0){
			return hc.getColumnName();
		}else{
			return sdata.get(row);
		}
	}

	@Override
	public String getColumnName(int column) {
		return cols[column];
	}

	public boolean isCellEditable(int row, int column) {
		return column == 1;
	}
	@Override
	public void setValueAt(Object value, int row, int column) {
		this.sdata.remove(row);
		this.sdata.add(row,(HDataType)value);
		fireTableRowsUpdated(row, row);
	}
}
