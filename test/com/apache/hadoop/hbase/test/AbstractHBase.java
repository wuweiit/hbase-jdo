package com.apache.hadoop.hbase.test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableDescriptor;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ncanis@gmail.com
 *
 */
public abstract class AbstractHBase {
	protected static final Logger log = LoggerFactory.getLogger("com.hbase");
	protected HBaseConfiguration config;
	
	public AbstractHBase(){
		this.config = makeConfig();
	}
	
	public HBaseConfiguration makeConfig(){
		HBaseConfiguration config = new HBaseConfiguration();
//		config.addResource(HBaseClient.class.getResource("/conf/hbase-default.xml"));
//		config.addResource(HBaseClient.class.getResource("/conf/hbase-site.xml"));

//		config.set("hbase.zookeeper.quorum", "192.168.0.203");
//		config.set("hbase.zookeeper.property.clientPort", "2181");
		return config;
	}
	
	/**
	 * table test
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 */
	public boolean isTableExist(String table) throws IOException {
		return new HBaseAdmin(config).tableExists(table);
	}
	
	/**
	 * delete table
	 * 
	 * @throws IOException
	 */
	public void deleteTable(String... tables) throws IOException {
		HBaseAdmin hbase = new HBaseAdmin(config);
		for(String table:tables) {
//			if (hbase.tableExists(table) == false)
//				return;
			// must diable before delete
			hbase.disableTable(table);
			hbase.deleteTable(table);
			
			log.debug("deleted table={}",table);
		}
	}

	public final void toList(String tag, ResultScanner rs) throws IOException {
		boolean isExist = false;
		
		for (Result r : rs) {
			if (isExist == false)
				isExist = true;
			List<KeyValue> list = r.list();
			StringBuilder sb = new StringBuilder();
			for(KeyValue kv : list) {
				sb.append("col=").append(Bytes.toString(kv.getQualifier()));
				sb.append(" value=").append(Bytes.toString(kv.getValue())).append(",");
			}
			log.debug("rowkey={}, {}",Bytes.toString(r.getRow()),sb.toString());
		}
		if (isExist == false)
			log.debug("### " + tag + " data doens't exist.");

		rs.close();
	}
	
	/**
	 * create table
	 * 
	 * @throws IOException
	 */
	public void createTable(String table, String...family) throws Exception {
		HBaseAdmin hbase = new HBaseAdmin(config);
		HTableDescriptor desc = new HTableDescriptor(table);
		
		for(String f:family) {
			HColumnDescriptor col = new HColumnDescriptor(f.getBytes());
			desc.addFamily(col);
		}
		hbase.createTable(desc);
		hbase.flush(table);
		log.debug("created table="+ table);
	}
	
	/**
	 * @param family
	 * @param id
	 * @return
	 */
	public IndexSpecification makeSpec(String family,String id){
		IndexSpecification isf = new IndexSpecification(id,(family+":"+id).getBytes());
		return isf;
	}
	
	/**
	 * create indexed table.
	 * @param table
	 * @param iss
	 * @throws IOException
	 */
	public void createIndexTable(String table, String... family) throws IOException {
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
	    HTableDescriptor desc = new HTableDescriptor(table);

	    if(family!=null) {
		    for(String f:family) {
				HColumnDescriptor col = new HColumnDescriptor(f.getBytes());
				desc.addFamily(col);
			}
	    }
	    admin.createTable(desc);
	    
	    log.debug("created index table ={}",table);
	}
	
	public void addIndexExistingTable(String table, List<IndexSpecification> columns) throws IOException{
		addIndexExistingTable(table, columns.toArray(new IndexSpecification[columns.size()]));
	}
	/**
	 * create index in existing table
	 * @param table
	 * @param columns
	 * @throws IOException
	 */
	public void addIndexExistingTable(String table, IndexSpecification ... columns) throws IOException{
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		admin.disableTable(table);
		for(IndexSpecification is: columns){					
			log.debug("creating index... {}",is.getIndexId());
			admin.addIndex(table.getBytes(),is);
			log.debug("added index ={}, table={}",is.getIndexId(),is.getIndexedTableName(table.getBytes()));
		}
	}
	
	/**
	 * delete index
	 * @param table
	 * @param indexID
	 * @throws IOException
	 */
	public void removeIndex(String table, String indexID) throws IOException{
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		admin.removeIndex(table.getBytes(),indexID);
		log.debug("removed index ={}",indexID);
	}
	
	public boolean isIndexExist(String table, String indexID) throws IOException {
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		HTableDescriptor desc = admin.getTableDescriptor(table.getBytes());
		IndexedTableDescriptor indexDesc = new IndexedTableDescriptor(desc);
		IndexSpecification spec = indexDesc.getIndex(indexID);
		return spec!=null;
	}
	
	public Collection<IndexSpecification> getIndexList(String table) throws IOException {
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		HTableDescriptor desc = admin.getTableDescriptor(table.getBytes());
		IndexedTableDescriptor indexDesc = new IndexedTableDescriptor(desc);		
		return indexDesc.getIndexes();
	}
	
	public void removeIndexAll(String table) throws IOException {
		Collection<IndexSpecification> idxs = getIndexList(table);
		for(IndexSpecification is: idxs){
			removeIndex(table,is.getIndexId());
			log.debug("{} index removed.",is.getIndexedTableName(table.getBytes()));
		}
	}
	
	public void deleteIndexes(String table) throws IOException {
		Collection<IndexSpecification> c = getIndexList(table);
		for(IndexSpecification is:c){
			removeIndex(table,is.getIndexId());
		}
	}
}
