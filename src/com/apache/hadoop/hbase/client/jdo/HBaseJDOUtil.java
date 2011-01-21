package com.apache.hadoop.hbase.client.jdo;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * General Util.
 * @author ncanis
 *
 */
public class HBaseJDOUtil {
	
	/**
	 * convert value to primitive object or String object. 
	 * 
	 * @param value (String, byte[], Primitive type)
	 * @return
	 */
	public static byte[] toBytes(Object value){
		if(value==null) return null;
		else if(value instanceof byte[]){
			return (byte[])value;
		}else if (value instanceof String) {
	        return Bytes.toBytes((String)value);
	        
		} else if (value instanceof Integer) {
	        return Bytes.toBytes((Integer)value);
	        
		} else if (value instanceof Boolean) {
	        return Bytes.toBytes((Boolean)value);
	        
		} else if (value instanceof Long) {
	        return Bytes.toBytes((Long)value);
	        
		} else if (value instanceof Double) {
	        return Bytes.toBytes((Double)value);
	        
		} else if (value instanceof Float) {
	        return Bytes.toBytes((Float)value);
	        
		} else if (value instanceof Short) {
	        return Bytes.toBytes((Short)value);
	        
		} else if (value instanceof Byte) {
	        return Bytes.toBytes((Byte)value);
	        
		} else{
			return Bytes.toBytes((String)value);
		}
	}
	
	/**
	 * convert byte[] to String or Primitive value.
	 * @param c
	 * @param value
	 * @return
	 */
	public static Object makeValue(Class<?> c, byte[] value){
	    if (c.equals(String.class)) {
	        return Bytes.toString(value);
	
	    } else if ( c.equals(Integer.TYPE) || c.equals(Integer.class)) {
	        return Bytes.toInt(value);
	
	    } else if (c.equals(Boolean.TYPE) || c.equals(Boolean.class)) {
	        return Bytes.toBoolean(value);
	
	    } else if (c.equals(Long.TYPE) || c.equals(Long.class)) {
	        return Bytes.toLong(value);
	
	    } else if (c.equals(Double.TYPE) || c.equals(Double.class)) {
	        return Bytes.toDouble(value);
	
	    } else if (c.equals(Float.TYPE) || c.equals(Float.class)) {
	        return Bytes.toFloat(value);
	
	    } else if (c.equals(Short.TYPE) || c.equals(Short.class)) {
	        return Bytes.toShort(value);
	
	    } else if (c.equals(Byte.TYPE) || c.equals(Byte.class)) {
	        return value;
	    } else{
	    	return value;
	    }
	}
}
