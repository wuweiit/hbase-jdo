package com.apache.hadoop.hbase.tool.view.table;

import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;


public class TableFamilyColumn extends HTableColumn{
	public static TableFamilyColumn NAME = new TableFamilyColumn(0,"name","Family Name",150);

	public TableFamilyColumn(int index,String id, String columnName, int width) {
		super(index,id, columnName, width);
	}
	
	public static HTableColumn[] getColumns(){
		return new TableFamilyColumn[]{NAME};
	}
	

}
