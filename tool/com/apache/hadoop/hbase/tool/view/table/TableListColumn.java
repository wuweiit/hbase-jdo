package com.apache.hadoop.hbase.tool.view.table;

import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;


public class TableListColumn extends HTableColumn{
	public static TableListColumn NAME = new TableListColumn(0,"name","Table Name",150);
	public static TableListColumn FAMILY_COUNT = new TableListColumn(1,"family_count","Family Count",100);
	public static TableListColumn MEMSTORE_FLUSH_SIZE = new TableListColumn(2,"memStoreFlushSize","MemStore Flush Size",200);
	public static TableListColumn MAX_FILE_SIZE = new TableListColumn(3,"maxFileSize","Max File Size",200);

	public TableListColumn(int index,String id, String columnName, int width) {
		super(index,id, columnName, width);
	}
	
	public static HTableColumn[] getColumns(){
		return new HTableColumn[]{NAME,FAMILY_COUNT, MEMSTORE_FLUSH_SIZE, MAX_FILE_SIZE};
	}
	

}
