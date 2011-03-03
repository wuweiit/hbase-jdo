package com.apache.hadoop.hbase.client.jdo;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HServerAddress;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableDescriptor;

import com.apache.hadoop.hbase.client.jdo.query.DeleteQuery;
import com.apache.hadoop.hbase.client.jdo.query.HBaseBeanProcessor;
import com.apache.hadoop.hbase.client.jdo.query.HBaseOrder;
import com.apache.hadoop.hbase.client.jdo.query.InsertQuery;
import com.apache.hadoop.hbase.client.jdo.query.SelectQuery;
import com.apache.hadoop.hbase.client.jdo.query.UpdateQuery;
import com.apache.hadoop.hbase.client.jdo.util.HConfigUtil;

/**
 * HBase DBO
 * 
 * @author ncanis@gmail.com
 */
public abstract class AbstractHBaseDBO implements IHBaseLog {
	private HBaseConfiguration config = null;
	private HBaseBeanProcessor processor = null;
	private SequenceGenerator sequence = null;

	private HBasePoolManager pool;
	public AbstractHBaseDBO() {
		this(HConfigUtil.makeHBaseConfig());
	}

	public AbstractHBaseDBO(HBaseConfiguration config) {
		this.config = config;
		this.processor = new HBaseBeanProcessor();
		this.pool = HBasePoolManager.get(config);
		this.sequence = new SequenceGenerator(this);
		
	}

	public HBaseConfiguration getConfig() {
		return config;
	}

	public HBaseBeanProcessor getProcessor() {
		return processor;
	}

	public HBasePoolManager getPool() {
		return pool;
	}

	/**
	 * Set BeanProcessor.
	 * 
	 * @param processor
	 */
	public void setProcessor(HBaseBeanProcessor processor) {
		this.processor = processor;
	}

	/**
	 * find out table is already exist.
	 * 
	 * @param table
	 * @return
	 */
	public boolean isTableExist(final String table) {
		boolean isExist = false;
		try {
			IndexedTableAdmin admin = new IndexedTableAdmin(config);
			isExist = admin.tableExists(table);
		} catch (Exception e) {
			log.error("HBaseAdmin error", e);
		}
		return isExist;
	}

	/**
	 * delete table
	 * 
	 * @throws IOException
	 */
	public boolean deleteTable(String... tables) {
		boolean isSuccess = false;
		try {
			IndexedTableAdmin admin = new IndexedTableAdmin(config);
			for (String table : tables) {
				removeIndexAll(table);
				// must diable before delete
				admin.disableTable(table);
				admin.deleteTable(table);
				log.debug("deleted table={}", table);
			}
		} catch (IOException e) {
			log.error("deleteTable error", e);
		}
		return isSuccess;
	}

	public boolean createTableIfNotExist(String table, HBaseOrder order,String... family) {
		if (isTableExist(table))
			return true;
		else {
			return createIndexTable(table, order,family);
		}
	}

	// /**
	// * create table
	// *
	// * @throws IOException
	// */
	// public boolean createTable(String table, String...family){
	// boolean isSuccess = false;
	// HBaseAdmin admin = null;
	// try {
	// admin = new HBaseAdmin(config);
	// HTableDescriptor desc = new HTableDescriptor(table);
	// for(String f:family) {
	// HColumnDescriptor col = new HColumnDescriptor(f.getBytes());
	// desc.addFamily(col);
	// }
	// admin.createTable(desc);
	// admin.flush(table);
	// isSuccess = true;
	// log.debug("created table="+ table);
	// } catch (IOException e) {
	// log.error("create table",e);
	// }
	// return isSuccess;
	// }

	/**
	 * make IndexSpecification instache. index column= family:column
	 * 
	 * @param family
	 * @param id
	 * @return
	 */
	public final IndexSpecification makeSpec(String family, String id) {
		IndexSpecification isf = new IndexSpecification(makeIndexID(family,id), (family + ":" + id).getBytes());
		return isf;
	}
	
	public final String makeIndexID(String family, String colName){
//		return family+"-"+colName;
		return colName;
	}
	
	public final String makeColName(String indexID){
//		int pos = indexID.lastIndexOf("-");
//		return indexID.substring(pos+1,indexID.length());
		return indexID;
	}

	/**
	 * create indexed table.
	 * @param table
	 * @param order order option
	 * @param family
	 * @return
	 */
	public boolean createIndexTable(String table, HBaseOrder order,String... family) {
		boolean isSuccess = false;
		IndexedTableAdmin admin = null;
		try {
			admin = new IndexedTableAdmin(config);
			HTableDescriptor desc = new HTableDescriptor(table);

			if (family != null) {
				for (String f : family) {
					HColumnDescriptor col = new HColumnDescriptor(f.getBytes());
					desc.addFamily(col);
				}
			}
			admin.createTable(desc);
			admin.flush(table);
			
			if(order!=HBaseOrder.NONE) {
				sequence.createSequence(table,order);
			}
			
			isSuccess = true;
			log.debug("created index table ={}", table);
		} catch (Exception e) {
			log.error("createIndexTable", e);
		}
		return isSuccess;
	}

	/**
	 * add a index to an existing table.
	 * 
	 * @param table
	 * @param family
	 * @param cols
	 * @return
	 */
	public boolean addIndexExistingTable(String table, String family, String... cols) {
		for (String col : cols) {
			addIndexExistingTable(table, makeSpec(family, col));
		}
		return true;
	}

	/**
	 * add a index to an existing table.
	 * 
	 * @param table
	 * @param columns
	 * @return
	 */
	public boolean addIndexExistingTable(String table, List<IndexSpecification> columns) {
		return addIndexExistingTable(table, columns.toArray(new IndexSpecification[columns.size()]));
	}

	/**
	 * add a index to an existing table
	 * 
	 * @param table
	 * @param columns
	 * @throws IOException
	 */
	public boolean addIndexExistingTable(String table, IndexSpecification... columns) {
		boolean isSuccess = false;
		IndexedTableAdmin admin = null;
		try {
			admin = new IndexedTableAdmin(config);
			admin.disableTable(table);
			for (IndexSpecification is : columns) {
				admin.addIndex(table.getBytes(), is);
				log.debug("created indexTable={}{}", table,is.getIndexId());
			}
			isSuccess = true;
		} catch (IOException e) {
			log.error("addIndexExistingTable", e);
		}
		return isSuccess;

	}

	/**
	 * remove a index
	 * 
	 * @param table
	 * @param indexID
	 * @throws IOException
	 */
	public boolean removeIndex(String table, String indexID) {
		boolean isSuccess = false;
		IndexedTableAdmin admin = null;
		try {
			admin = new IndexedTableAdmin(config);
			log.debug("try to remove indexTable ={}{}", table,indexID);
			admin.removeIndex(table.getBytes(), indexID);
			log.debug("removed indexTable ={}{}", table,indexID);
			isSuccess = true;
		} catch (IOException e) {
			log.error("removeIndex", e);
		}
		return isSuccess;

	}

	/**
	 * check to see if index is existing.
	 * 
	 * @param table
	 * @param indexID
	 * @return
	 */
	public boolean existIndex(String table, String indexID) {
		return getIndexSpec(table, indexID) != null;
	}

	/**
	 * get index descriptor.
	 * 
	 * @param table
	 * @return
	 * @throws IOException
	 */
	private IndexedTableDescriptor getIndexDesc(String table) throws IOException {
		IndexedTableAdmin admin = new IndexedTableAdmin(config);
		HTableDescriptor desc = admin.getTableDescriptor(table.getBytes());
		IndexedTableDescriptor idesc = new IndexedTableDescriptor(desc);
		return idesc;
	}

	/**
	 * get index specification.
	 * 
	 * @param table
	 * @param indexID
	 * @return
	 */
	public IndexSpecification getIndexSpec(String table, String indexID) {
		IndexSpecification spec = null;
		try {
			spec = getIndexDesc(table).getIndex(indexID);
		} catch (IOException e) {
			log.error("getIndexSpec", e);
		}
		return spec;
	}

	/**
	 * get existing all index list in table. if main table is already deleted,
	 * you cannot get indexes at all.
	 * 
	 * @param table
	 * @return
	 */
	public Collection<IndexSpecification> getIndexList(String table) {
		Collection<IndexSpecification> cols = null;
		try {
			cols = getIndexDesc(table).getIndexes();
		} catch (IOException e) {
			log.error("getIndexList", e);
		}
		return cols;
	}

	/**
	 * remove all index list in table.
	 * 
	 * @param table
	 * @return
	 */
	public boolean removeIndexAll(String table) {
		Collection<IndexSpecification> idxs = getIndexList(table);
		for (IndexSpecification is : idxs) {
			removeIndex(table, is.getIndexId());
		}
		return true;
	}

	/**
	 * get first index column in table.
	 * 
	 * @param tableName
	 * @param cols
	 * @return
	 */
	public IndexSpecification getFirstIndexColumn(String tableName, String family,Collection<String> cols) {
		Collection<IndexSpecification> indexes = getIndexList(tableName);
		if(indexes==null) return null;
		for (IndexSpecification index : indexes) {
			byte[][] idxCols = index.getIndexedColumns();
			for(byte[] idc:idxCols){				
				String idxName = new String(idc); // family:name
				for (String col : cols) {
					if (idxName.equals(family+":"+col)) {
						return index;
					}
				}				
			}
		}

		return null;
	}

	/**
	 * Split a table or an individual region.
	 * this has had some time
	 * in configuration
	 * - hbase.server.thread.wakefrequency
	 * - hbase.server.thread.wakefrequency
	 * @param t
	 * @return
	 * @throws IOException
	 */
	public Map<HRegionInfo, HServerAddress> splitTable(final HTable t) throws Exception {
		// Split this table in two.
		HBaseAdmin admin = new HBaseAdmin(config);
		admin.split(t.getTableName());
		Map<HRegionInfo, HServerAddress> regions = waitOnSplit(t);
		return regions;
	}

	/*
	 * Wait on table split. May return because we waited long enough on the
	 * split and it didn't happen. Caller should check.
	 * 
	 * @param t
	 * @return Map of table regions; caller needs to check table actually split.
	 */
	private Map<HRegionInfo, HServerAddress> waitOnSplit(final HTable t) throws IOException {
		Map<HRegionInfo, HServerAddress> regions = t.getRegionsInfo();
		int originalCount = regions.size();
		for (int i = 0; i < this.config.getInt("hbase.test.retries", 30); i++) {
			Thread.currentThread();
			try {
				Thread.sleep(this.config.getInt("hbase.server.thread.wakefrequency", 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			regions = t.getRegionsInfo();
			if (regions.size() > originalCount)
				break;
		}
		return regions;
	}

	public SequenceGenerator getSequence() {
		return sequence;
	}
	/**
	 * create selectQuery.
	 * 
	 * @param table
	 * @return
	 */
	public abstract SelectQuery createSelectQuery(String table);

	/**
	 * create deleteQuery
	 * 
	 * @param table
	 * @return
	 */
	public abstract DeleteQuery createDeleteQuery(String table);

	/**
	 * create insertQuery.
	 * 
	 * @param table
	 * @return
	 */
	public abstract InsertQuery createInsertQuery(String table);
	
	/**
	 * create updateQuery.
	 * @param table
	 * @return
	 */
	public abstract UpdateQuery createUpdateQuery(String table);



}
