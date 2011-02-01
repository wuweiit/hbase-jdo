package com.apache.hadoop.hbase.tool.view.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.tool.view.comp.table.AbstractHTableModel;
import com.apache.hadoop.hbase.tool.view.comp.table.HTableColumn;
import com.apache.hadoop.hbase.tool.view.processor.TableDetailInfo;

public class TableColumnInfoModel extends AbstractHTableModel<TableDetailInfo>{
	public TableColumnInfoModel() {
		super(new ArrayList<TableDetailInfo>(), TableColumnInfoColumn.getColumns());
	}

	@Override
	protected void resetColumns(List<TableDetailInfo> values) {
		this.columns.clear();
		this.columns.add(TableColumnInfoColumn.NAME);
		int maxCount = 0;
		for(TableDetailInfo tdi: values){
			if(tdi.getColumnCount()>=maxCount){
				maxCount=tdi.getColumnCount();
			}
		}
		if(maxCount>0) {
			for(int i=1;i<=maxCount;i++){
				this.columns.add(new TableColumnInfoColumn(i,i+"","Column-"+i,160));
			}
		}
	}

	@Override
	public Object getValueAt(int r, int c) {
		TableDetailInfo info = this.values.get(r);
		
		HTableColumn hcol = TableColumnInfoColumn.get(TableColumnInfoColumn.getColumns(),c);
		if(hcol==TableColumnInfoColumn.NAME) return info.getName();
		else{
			Set<String> cols = info.getColumns().keySet();
			int i = 0;
			for(String col:cols){
				i++;
				if(i!=c) continue;
				byte[] value = info.getColumns().get(col);
				StringBuilder sb = new StringBuilder();
				sb.append(col).append("(");
				sb.append(Bytes.toString(value)).append(")");
				return sb.toString();
			}
		}
		return "=No Data=";
	}

	@Override
	public void loadTestData(int count) {
		for(int i=0;i<count;i++){
			TableDetailInfo info = new TableDetailInfo();
			info.setFamilycount(i);
			info.setMaxFileSize(i);
			info.setMemStoreFlushSize(i);
			info.setName("table-"+i);
			info.setFamily("family-"+i);
			
			Map<String, byte[]> cols= new HashMap<String, byte[]>();
			for(int j=0;j<5;j++){
				cols.put("col"+i, ("value-"+j).getBytes());
			}
			info.setColumns(cols);
			values.add(info);
		}
		fireTableDataChanged();
	}
}
