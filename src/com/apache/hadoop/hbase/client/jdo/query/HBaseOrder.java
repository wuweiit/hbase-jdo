package com.apache.hadoop.hbase.client.jdo.query;

/**
 * Table data order type.
 * @author ncanis
 *
 */
public enum HBaseOrder {
	DESC("desc"),
	ASC("asc"),
	NONE("none"),
	;
	
	private final String name;
	private HBaseOrder(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public static HBaseOrder getOrder(String order){
		if(order.equals("desc")) {
			return DESC;
		}else if(order.equalsIgnoreCase("desc")) {
			return ASC;
		}else{
			return NONE;
		}
	}
}
