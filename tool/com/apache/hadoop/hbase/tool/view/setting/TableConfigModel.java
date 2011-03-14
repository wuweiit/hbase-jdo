package com.apache.hadoop.hbase.tool.view.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.apache.hadoop.hbase.client.jdo.util.HConfigUtil;
import com.apache.hadoop.hbase.tool.HBaseTool;
import com.apache.hadoop.hbase.tool.HToolConstants;
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
	
		String pName = getValueAt(rowIndex,0).toString();
		String pValue = aValue.toString();
		HConfigUtil.saveProperty(pName,pValue);			
		
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

	@Override
	public long getValueLength(int row, int column) {
		return getValueAt(row,column).toString().length();
	}

}
