package com.apache.hadoop.hbase.tool.view.setting;

import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;


public class TableConfigColumn extends HTableColumn{
	public static TableConfigColumn NAME = new TableConfigColumn(0,"name","name",300,false);
	public static TableConfigColumn VALUE = new TableConfigColumn(1,"value","value",400,true);

	protected TableConfigColumn(int index, String id, String columnName, int width, boolean isEditable) {
		super(index, id, columnName, width, isEditable);
	}
	
	public static HTableColumn[] getColumns(){
		return new HTableColumn[]{NAME,VALUE};
	}
}
