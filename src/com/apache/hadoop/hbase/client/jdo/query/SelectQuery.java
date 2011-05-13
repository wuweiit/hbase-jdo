package com.apache.hadoop.hbase.client.jdo.query;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.tableindexed.IndexNotFoundException;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.transactional.TransactionState;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.HBaseJDOException;
import com.apache.hadoop.hbase.client.jdo.util.HUtil;

/**
 * Selection Query.
 * @author ncanis
 *
 */
public class SelectQuery extends HBQuery{
	public SelectQuery(AbstractHBaseDBO dbo,String tableName){
		super(dbo,tableName);
	}
	
	/**
	 * get row data for the specified row
	 * @param row
	 * @param c
	 * @return
	 */
	public AbstractHBaseBean select(final byte[] row, final Class<? extends AbstractHBaseBean> c){
		IndexedTable table = null;
		AbstractHBaseBean bean = null;
		try{
			table = borrowTable();
			bean = select(table,null,row,c);
		}catch(Exception e){
			log.error("select",e);
		}finally{
			releaseTable(table);
		}
		return bean;
	}
	
	/**
	 * confirm  existing an specified column's value. 
	 * @param family
	 * @param col
	 * @param value
	 * @return
	 */
	public boolean existColumnValue(String family, String col, byte[] value){
		// offset, limit		
//		EqualsColumnValueGetFilter countFilter = new EqualsColumnValueGetFilter(col.getBytes(), value,1);
		
		FilterList list = new FilterList();
		ValueFilter valueFilter = new ValueFilter(CompareOp.EQUAL, new BinaryComparator(value));
		PageFilter rowFilter = new PageFilter(1);
		list.addFilter(valueFilter);
		list.addFilter(rowFilter);
		
		boolean isExist = false;
		ResultScanner scanner=null;
		IndexedTable table = null;
		try{
			table = borrowTable();
			
			Scan scan = new Scan();
			scan.setFilter(list);
			scanner = table.getScanner(scan);
			Result result = scanner.next();
			isExist = result!=null;
		} catch (IndexNotFoundException e) {
			log.error("Search",e);			
		} catch (Exception e) {
			log.error("Search",e);
		} finally{
			closeScanner(scanner);
			releaseTable(table);
		}
		
		return isExist;
	}
	
	/**
	 *  get row data for the specified row
	 * @param state Transaction mode
	 * @param row 
	 * @param c
	 * @return
	 * @throws IOException
	 */
	public AbstractHBaseBean select(IndexedTable table,TransactionState state, byte[] row, Class<? extends AbstractHBaseBean> c) throws IOException{
		Get g = new Get(row);
		Result r = state==null? table.get(g):table.get(state,g);
		return processor.convert(tableName,c,r);
	}
	
	/**
	 *  get column data for the specified row
	 * @param row
	 * @param family
	 * @param col
	 * @param type
	 * @return
	 */
	public Object selectColumn(byte[] row, String family,String col, Class type){
		IndexedTable table = null;
		Object o = null;
		try{
			table = borrowTable();
			o  = selectColumn(table,null,row,family,col,type);
		}catch(Exception e){
			log.error("selectColumn",e);
		}finally{
			releaseTable(table);
		}
		return o;
	}
	
	/**
	 *  get row data for the specified row in Transaction mode.
	 * @param state 
	 * @param row
	 * @param family
	 * @param col
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public Object selectColumn(IndexedTable table, TransactionState state,byte[] row, String family,String col, Class type) throws IOException{
		Get g = new Get(row);
		Result r = state==null? table.get(g):table.get(state,g);
		byte[] data = r.getValue(Bytes.toBytes(family), Bytes.toBytes(col));
		return HUtil.makeValue(type,data);
	}
	
	/**
	 * search table for the specified parameters.
	 * @param family
	 * @param param
	 * @param c
	 * @return
	 */
	public List<? extends AbstractHBaseBean> search(String family, HBaseParam param, Class<? extends AbstractHBaseBean> c){
		if(param==null){
			log.error("Search", new HBaseJDOException("HBaseParam cannot be null."));
			return null;
		}
		
		// parameter info.
		Hashtable<String, SearchInfo> options = param.getOptions();
		Hashtable<String, HBaseOrder> orders = param.getOrders();
		FilterList indexFilterList = new FilterList();
		
		
		byte[] startRow = param.getStartRow();
		byte[] endRow = null;
		
		// get first index column.
		IndexSpecification is = dbo.getFirstIndexColumn(tableName,family,options.keySet());
		if(is==null) {
			log.error("Search", new HBaseJDOException("You must have index column in search options"));
			return null;			
		}else{
			SearchInfo si = options.get(dbo.makeColName(is.getIndexId()));
			startRow = Bytes.add(si.getValue(),startRow); // start row= column value + base row key
		}
		
		// add filter for search option	
		if(options!=null) {
			for(String col:options.keySet()) {
				SearchInfo si = options.get(col);
				SingleColumnValueFilter filter = null;
				if(si.getSOP()==QSearch.EQUAL || si.getSOP()==QSearch.NOT_EQUAL) {
					filter = new SingleColumnValueFilter(Bytes.toBytes(family),
						Bytes.toBytes(col),si.getSOP().toCompareOp(),si.getValue());
				}else{
					filter = new SingleColumnValueFilter(Bytes.toBytes(family),
							Bytes.toBytes(col),si.getSOP().toCompareOp(),
							new BinaryPrefixComparator(si.getValue()));
				}
				
				indexFilterList.addFilter(filter);			
			}
		}
		
		// offset, limit		
		PageFilter pageFilter = new PageFilter(param.getLimit());
//		RowPaginationFilter pageFilter = new RowPaginationFilter(param.getOffset(),param.getLimit());
		indexFilterList.addFilter(pageFilter);
		
		byte[][] indexColumns = is.getAllColumns();
		byte[][] baseColumns = param.makeBaseColumnsBytes(family);
		
		
		/*
		 * String indexId, 
		 * final byte[] indexStartRow,  
		 * final byte[] indexStopRow,
      	   byte[][] indexColumns, 
      	   final Filter indexFilter,
      	   final byte[][] baseColumns
		 */
		ResultScanner scanner=null;
		List<AbstractHBaseBean> list = null;
		
		IndexedTable table = null;
		try{
			table = borrowTable();
			
			scanner = table.getIndexedScanner(
					is.getIndexId(), // index id
					startRow, // start row
					endRow,  // end row
					indexColumns,		        
					indexFilterList, // filter
					baseColumns
			);			
			list = processor.convert(tableName,c,scanner);
		} catch (IndexNotFoundException e) {
			log.error("Search",e);			
		} catch (Exception e) {
			log.error("Search",e);
		} finally{
			closeScanner(scanner);
			releaseTable(table);
		}
		
		return list;
	}
	
	/**
	 * get row count of table.
	 * this is extremely slow. I don't recommend to use this.
	 * 
	 * @deprecated
	 * @param receiver will be called when the rows receive {countPerCall) count.
	 * @return total row count.
	 */
	public int getTotalRowCount(RowCountReceiver receiver, int countPerCall){
		
		int count = 0;
		ResultScanner rs=null;
		IndexedTable table = null;
		try{
			table = borrowTable();
			table.setScannerCaching(100);// this is important.
			table.setAutoFlush(false);
			
			FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
			Scan scan = new Scan();
			scan.setFilter(filter);
			
			rs = table.getScanner(scan);
			Iterator it = rs.iterator();
			
			while(it.hasNext()){
				it.next();
				count++;
				if(receiver!=null && count%countPerCall==0) receiver.receive(count);
			}
			if(receiver!=null) receiver.receive(count);
			rs.close();
		} catch (Exception e) {
			log.error("total row count",e);
		} finally{
			closeScanner(rs);
			releaseTable(table);
		}
		return count;
	}
	
}
