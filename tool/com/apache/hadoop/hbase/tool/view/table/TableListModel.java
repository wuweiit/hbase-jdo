package com.apache.hadoop.hbase.tool.view.table;

import java.util.ArrayList;

import com.apache.hadoop.hbase.tool.view.comp.table.AbstractHTableModel;
import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;
import com.apache.hadoop.hbase.tool.view.processor.TableInfo;

public class TableListModel extends AbstractHTableModel<TableInfo>{
	public TableListModel() {
		super(new ArrayList<TableInfo>(), TableListColumn.getColumns());
	}

	@Override
	public Object getValueAt(int r, int c) {
		TableInfo info = this.values.get(r);
		HTableColumn col = TableListColumn.get(TableListColumn.getColumns(),c);
		if(col==TableListColumn.NAME) return info.getName();
		else if(col==TableListColumn.FAMILY_COUNT) return info.getFamilycount();
		else if(col==TableListColumn.MAX_FILE_SIZE) return info.getMaxFileSize();
		else if(col==TableListColumn.MEMSTORE_FLUSH_SIZE) return info.getMemStoreFlushSize();
		
		return "";
	}

	@Override
	public void loadTestData(int count) {
		for(int i=0;i<count;i++){
			TableInfo info = new TableInfo();
			info.setFamilycount(i);
			info.setMaxFileSize(i);
			info.setMemStoreFlushSize(i);
			info.setName("table-"+i);
			values.add(info);
		}
		fireTableDataChanged();
	}
}
