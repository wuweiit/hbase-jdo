package com.apache.hadoop.hbase.tool.view.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class HBaseTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private HTableInfo info = null;
	
	public HBaseTableModel(){
		info = new HTableInfo();
	}
	
	public void setData(HTableInfo statInfo) {
		statInfo.clear();
		if(statInfo!=null) this.info.copy(statInfo);
	}
	@Override
	public int getColumnCount() {		
		return info.getColumns().size();
	}

	@Override
	public int getRowCount() {
		return info.getValues().size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		List<List<String>> values = info.getValues();
		List<String> data = values.get(r);
		String value = data.get(c);
		return value;
	}
	
	@Override
	public String getColumnName(int index) {
		if(info.getColumns().size()<index) return "";
		return info.getColumns().get(index);
	}

	public List<String> getData(int row) {
		List<List<String>> values = info.getValues();
		List<String> data = values.get(row);
		return data;
	}

	public String getRowString(int row){
		List<String> list = getData(row);
		StringBuffer sb = new StringBuffer();
		int i=0;
		for(String s:list){
			sb.append(getColumnName(i)).append("=").append(s).append("\n");
			i++;
		}
		return sb.toString();
	}

	public HTableInfo getData() {
		return info;
	}
}
