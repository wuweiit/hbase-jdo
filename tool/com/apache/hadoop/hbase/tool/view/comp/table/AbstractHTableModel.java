package com.apache.hadoop.hbase.tool.view.comp.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractHTableModel<T> extends AbstractTableModel{
	protected List<HTableColumn> columns = new ArrayList<HTableColumn>();
	protected List<T> values;
	
	public AbstractHTableModel(List<T> values, HTableColumn[] columns){
		this.values = values;
		if(columns!=null) {
			for(HTableColumn hc:columns){
				this.columns.add(hc);
			}
		}
	}
	
	public T getLastRow(){
		if(values.size()==0) return null;
		return values.get(values.size()-1);
	}
	
	public T getFirstRow(){
		if(values.size()==0) return null;
		return values.get(0);
	}

	public void addColumn(HTableColumn col){
		this.columns.add(col);
	}
	
	public List<HTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<HTableColumn> columns) {
		this.columns = columns;
	}

	public void setValues(List<T> values) {
		this.values = values;
	}

	public void clear(){
		this.values.clear();
	}
	
	public void setData(List<T> values){
		clear();
		if(values==null) return;
		
		this.values.addAll(values);
		resetColumns(values);
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		HTableColumn col = columns.get(columnIndex);
		return col.isEditable();
	}
	protected void resetColumns(List<T> values2) {
		
	}

	@Override
	public int getColumnCount() {		
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return values.size();
	}
	
	@Override
	public String getColumnName(int index) {
		if(columns.size()<index) return "";
		return (columns.toArray()[index]).toString();
	}
	
	public List<T> getValues() {
		return values;
	}

	public T getData(int row) {
		return values.get(row);
	}
	
	@Override
	public abstract Object getValueAt(int row, int column);
	
	public abstract void loadTestData(int count);
}
