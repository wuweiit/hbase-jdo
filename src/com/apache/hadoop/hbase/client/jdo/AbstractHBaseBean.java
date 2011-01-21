package com.apache.hadoop.hbase.client.jdo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * all of beans class must extend this class.
 * 
 * you must declare annotation in field comment.
 * for example)
 * 
 * <code>
 * public class TestUserBean extends AbstractHBaseBean{
	@Index @Column
	private String id;
	@Column
	private String name;
	
	public TestUserBean() {		
	}
	
	.. ..
  }
  </code>
 * @author ncanis
 *
 */
public abstract class AbstractHBaseBean {
	protected String tableName; // table name
	protected byte[] row;		// row key(should be long type)
	protected String family;	// family key.
	
	public AbstractHBaseBean(){
	}
	
	public AbstractHBaseBean(String family){
		this.family = family;
	}
	public void set(String tableName, String family){
		this.tableName = tableName;
		this.family = family;
	}
	
	public void setFamily(String family) {
		this.family = family;
	}

	public String getTableName() {
		return tableName;
	}
	public byte[] getRow() {
		return row;
	}
	public void setRow(byte[] row) {
		this.row = row;
	}
	public String getFamily() {
		return family;
	}

	@Override
	public String toString() {
		// this show all information in bean.
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
