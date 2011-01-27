package com.apache.hadoop.hbase.tool.view.table;

import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;


public class TableColumnInfoColumn extends HTableColumn{
	public static TableColumnInfoColumn NAME = new TableColumnInfoColumn(0,"name","Family Name",150);

	public TableColumnInfoColumn(int index,String id, String columnName, int width) {
		super(index,id, columnName, width);
	}
	
	public static HTableColumn[] getColumns(){
		return new TableColumnInfoColumn[]{NAME};
	}
	

}
