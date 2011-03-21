package com.apache.hadoop.hbase.test;

import java.util.List;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.HBaseConfiguration;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.HBaseDBOImpl;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;
import com.apache.hadoop.hbase.client.jdo.query.HBaseOrder;
import com.apache.hadoop.hbase.client.jdo.query.HBaseParam;
import com.apache.hadoop.hbase.client.jdo.query.InsertQuery;
import com.apache.hadoop.hbase.client.jdo.query.QSearch;
import com.apache.hadoop.hbase.client.jdo.query.SelectQuery;
import com.apache.hadoop.hbase.client.jdo.query.UpdateQuery;
import com.apache.hadoop.hbase.client.jdo.util.HUtil;

public class AddIndexTableTest extends TestCase {
	HBaseDBOImpl dbo = new HBaseDBOImpl();
	String table = "add_test";
	String family = "add_family";
	
	String icols = "aaa";
	String[] cols = {"bbb","ccc"};
	
	public void testPut() throws Exception {
		
		
		if(dbo.isTableExist(table)) {
			dbo.deleteTable(table);
		}
		
//		dbo.createTableIfNotExist(table, HBaseOrder.DESC, family);
		dbo.createIndexTable(table,HBaseOrder.DESC,family);
		dbo.addMultiColumnIndex(table, family, icols,cols);
//		dbo.addIndexExistingTable(table, family, "aaa");
		
		InsertQuery iq = dbo.createInsertQuery(table);
		
		for(int i =0; i<3;i++) {
			TestTableBean bean = new TestTableBean();
			bean.set(table, family);
			bean.setAaa(1);
			bean.setBbb(i);
			bean.setCcc(true);
			iq.insert(bean);
		}
		// 3번째 col 모두 업데이트
		List<TestTableBean> list = showList(true);
		UpdateQuery uq = dbo.createUpdateQuery(table);
		for(TestTableBean b:list) {
			uq.update(b.getRow(), family, "ccc", HUtil.toBytes(false));
		}
		
		// 다시 조회
		showList(true);
	}
	
	private List<TestTableBean> showList(boolean isCcc){
		SelectQuery sq = dbo.createSelectQuery(table);
		HBaseParam param = new HBaseParam();
		param.setPage(null,3);
		param.addSearchOption("aaa",1,QSearch.EQUAL);
		param.addSearchOption("ccc",isCcc,QSearch.EQUAL);
		param.addColumn(cols);
		List<TestTableBean> list = (List<TestTableBean>)sq.search(family, param, TestTableBean.class);
		for(TestTableBean bean2:list){
			System.out.println("bean=> ccc="+bean2.isCcc());
		}
		return list;
	}
	
}
