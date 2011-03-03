package com.apache.hadoop.hbase.test;

import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.HBaseDBOImpl;
import com.apache.hadoop.hbase.client.jdo.query.HBaseOrder;
import com.apache.hadoop.hbase.client.jdo.query.HBaseParam;
import com.apache.hadoop.hbase.client.jdo.query.InsertQuery;
import com.apache.hadoop.hbase.client.jdo.query.QSearch;
import com.apache.hadoop.hbase.client.jdo.query.SelectQuery;
import com.apache.hadoop.hbase.client.jdo.query.UpdateQuery;
import com.apache.hadoop.hbase.client.jdo.util.HUtil;

public class PutOverwriteTest extends TestCase {
	public void testPut() throws Exception {
		String tableName = "put_test";
		String family = "put_family";
		HBaseConfiguration config = new HBaseConfiguration();
		HBaseAdmin admin = new HBaseAdmin(config);
		if(admin.tableExists(tableName)==false){
			HTableDescriptor desc = new HTableDescriptor(tableName);
			
			HColumnDescriptor col = new HColumnDescriptor(family.getBytes());
			desc.addFamily(col);
			
			admin.createTable(desc);
			admin.flush(tableName);
		}
		HTable table = new HTable(config, "put_test");
		
		for(int i=0;i<10;i++) {
			Put p = new Put("1".getBytes());
			p.add(family.getBytes(), ("name").getBytes(),"test".getBytes());
			table.put(p);		
			table.flushCommits();
		}
		System.out.println("111 finished. HTabe");
	}
	
	public void testIndexedPut() throws Exception {
		String tableName = "put_test2";
		String family = "put_family2";
		HBaseConfiguration config = new HBaseConfiguration();
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		if(admin.tableExists(tableName)==false){
			HTableDescriptor desc = new HTableDescriptor(tableName);
			
			HColumnDescriptor col = new HColumnDescriptor(family.getBytes());
			desc.addFamily(col);
			
			admin.createTable(desc);
			admin.flush(tableName);
			
			IndexSpecification is = new IndexSpecification("name", (family+":name").getBytes());
			admin.addIndex(tableName.getBytes(),is);
		}
		IndexedTable table = new IndexedTable(config,tableName.getBytes());
		
		for(int i=0;i<10;i++) {
			Put p = new Put("1".getBytes());
			p.add(family.getBytes(), ("name").getBytes(),"test".getBytes());
			table.put(p);		
			table.flushCommits();
		}
		
		System.out.println("222 finished. Indexed HTabe");
	}
	
	public void testHBaseUtil() {
		String tableName = "put_test4";
		String family = "put_family4";
		String cols = "age";
		AbstractHBaseDBO dbo = new HBaseDBOImpl();
		if(dbo.isTableExist(tableName)==false) {
			dbo.createTableIfNotExist(tableName,HBaseOrder.DESC, family);
			dbo.addIndexExistingTable(tableName,family, cols);
		}
		
		InsertQuery iq = dbo.createInsertQuery(tableName);
		TestBean bean = new TestBean();
		bean.setFamily(family);
		bean.setAge("1");
		iq.insert(bean);
		
		System.out.println("inserted.");
		
		SelectQuery sq = dbo.createSelectQuery(tableName);
		HBaseParam param = new HBaseParam();
		param.setPage(null,Integer.MAX_VALUE);
		param.addColumn("age");
		param.addSearchOption("age","1",QSearch.EQUAL);
		List<TestBean> list = (List<TestBean>)sq.search(family, param, TestBean.class);
		
		System.out.println("searched.");
		
		TestBean bean2 = list.get(0);
		Hashtable<String,byte[]> cols2 = new Hashtable<String, byte[]>();
		cols2.put("age",HUtil.toBytes("2"));
		
		for(int i=0;i<10;i++) {
			UpdateQuery uq = dbo.createUpdateQuery(tableName);
//			uq.update(bean2.getRow(),family,"age","2".getBytes());
			uq.overwrite(family,cols2, bean2.getRow());
		}
		
		System.out.println("333 finished hbaseUtil");
	}
	
}
