package com.apache.hadoop.hbase.client.jdo.query;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.HBaseJDOException;
import com.apache.hadoop.hbase.client.jdo.HBaseJDOUtil;

/**
 * HBase Query Parameter Information.
 * @author ncanis
 *
 */
public class HBaseParam {
	private Set<String> columns = null;
	private Hashtable<String, SearchInfo> options = null;
	private Hashtable<String, HBaseOrder> orders = null;
	private int limit;
	private byte[] startRow;
	
	public HBaseParam(){
		this.limit = Integer.MAX_VALUE;
		this.columns = new HashSet<String>();
		this.options = new Hashtable<String, SearchInfo>();
		this.orders = new Hashtable<String, HBaseOrder>();
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public byte[] getStartRow() {
		return startRow;
	}

	public void setStartRow(byte[] startRow) {
		if(startRow==null) startRow = new byte[0];
		this.startRow = startRow;
	}

	public void setPage(byte[] startRow, int limit){
		setStartRow(startRow);
		setLimit(limit);
	}
	/**
	 * add selection colum
	 * @param cols
	 * @return
	 * @throws HBaseJDOException
	 */
	public void addColumn(String... cols){
		for(String col:cols) {
			columns.add(col);
		}
	}
	
	/**
	 * remove selection colum
	 * @param cols
	 */
	public void removeColumn(String... cols){
		for(String col:cols) {
			columns.remove(col);
		}
	}
	
	/**
	 * add search option.
	 * @param col column name
	 * @param value column value
	 * @param op EQUAL, NOT_EQUAL, LIKE(this is prefix searching mode)
	 * @return
	 */
	public boolean addSearchOption(String col, Object value, QSearch op){
		if(value==null) value = new byte[0];
		SearchInfo si = new SearchInfo(col,HBaseJDOUtil.toBytes(value),op);
		
		boolean isSuccess = this.options.put(col, si)==null? true:false;
		return isSuccess;
	}
	
	/**
	 *  remove search option.
	 * @param keys
	 */
	public void removeSearchOption(String... keys){
		for(String key:keys){
			this.options.remove(key);
		}
	}

	/**
	 * add Order option.
	 * @param col
	 * @param order
	 * @return
	 */
	public boolean addOrder(String col, HBaseOrder order){
		boolean isExist = this.orders.put(col,order)==null? true:false;
		return isExist;
	}
	
	/**
	 * remove order option.
	 * @param cols
	 */
	public void removeOrder(String... cols){
		for(String col:cols){
			this.orders.remove(col);
		}
	}

	/**
	 * get columns.
	 * @return
	 */
	public Set<String> getColumns() {
		return columns;
	}
	
	/**
	 * to make family:id
	 * @return
	 */
	public byte[][] makeBaseColumnsBytes(String family) {
		if(columns.size()==0) return null;
		byte[][] data = new byte[columns.size()][];
		int i = 0;
		for(String col:columns){
			data[i] = Bytes.toBytes(family+":"+col);
			i++;
		}
		return data;
	}
	

	public Hashtable<String, SearchInfo> getOptions() {
		return options;
	}

	public Hashtable<String, HBaseOrder> getOrders() {
		return orders;
	}
}

/**
 * Order Information.
 * @author ncanis
 *
 */
class OrderInfo{
	private String col;
	private HBaseOrder order;
	public OrderInfo(String col, HBaseOrder order){
		this.col = col;
		this.order = order;
	}
	public String getCol() {
		return col;
	}
	public HBaseOrder getOrder() {
		return order;
	}
	
}

/**
 * Search Information.
 * @author ncanis
 *
 */
class SearchInfo{
	private String col;
	private QSearch compare;
	private byte[] value;
	
	public SearchInfo(String col, byte[] value,QSearch compare){
		this.col = col;
		this.compare = compare;
		this.value = value;
	}

	public String getCol() {
		return col;
	}

	public QSearch getSOP() {
		return compare;
	}

	public byte[] getValue() {
		return value;
	}
}
