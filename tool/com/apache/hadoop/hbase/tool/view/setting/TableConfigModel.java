package com.apache.hadoop.hbase.tool.view.setting;

import java.util.ArrayList;

import com.apache.hadoop.hbase.tool.view.comp.table.AbstractHTableModel;
import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;

public class TableConfigModel extends AbstractHTableModel<HConfigInfo>{
	public TableConfigModel() {
		super(new ArrayList<HConfigInfo>(), TableConfigColumn.getColumns());
	}

	@Override
	public Object getValueAt(int row, int column) {
		HConfigInfo info = values.get(row);
		if(column==0){
			return info.getName();
		}else{
			return info.getValue();
		}
	}
	

	@Override
	public void setValueAt(Object aValue, int rowIndex, int col) {
		HTableColumn hcol = columns.get(col);
		if(hcol.isEditable()==false) return;
		
		HConfigInfo info = values.get(rowIndex);
		info.setValue(aValue.toString());		
		
		this.fireTableDataChanged();
	}
	

	@Override
	public void loadTestData(int count) {
		for(int i=0;i<count;i++){
			HConfigInfo info = new HConfigInfo();
			info.setName("table-"+i);
			info.setValue("value-"+i);
			values.add(info);
		}
		fireTableDataChanged();
	}

}
