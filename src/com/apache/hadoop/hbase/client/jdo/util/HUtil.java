package com.apache.hadoop.hbase.client.jdo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.hsqldb.lib.Collection;

/**
 * General Util.
 * @author ncanis
 *
 */
public class HUtil {
	
	private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat df3 = new SimpleDateFormat("dd");
	private static final String pattern = "###.000";
	private static final DecimalFormat df4 = new DecimalFormat(pattern);
	private static final DecimalFormat float_format = new DecimalFormat("#.000");
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
	
	public static String convertString(byte[] value){
		if(value==null) return "null";
		else if(value.length==1){
			return value[0]+"";
		}else if(value.length==2) {
			return Bytes.toShort(value)+"";
		}else if(value.length==4){
			return Bytes.toInt(value)+"";
		}else if(value.length==8){			
			return Bytes.toLong(value)+"";
		}else {
			String str = Bytes.toString(value);
			if(StringUtils.isWhitespace(str)){
				return "Blank";
			}else{
				return str;
			}
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


	public static void write(File f, byte[] data) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(data);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static float makeFloat(float f) {
		return Float.parseFloat(df4.format(f));
	}

	public static String makeSimpleDate(long time) {
		if (time == 0)
			return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return makeSimpleDate(c);
	}

	public static String makeSimpleDate(Date date) {
		if (date == null)
			return "None";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return makeSimpleDate(c);
	}

	public static String makeSimpleDate(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		return year + "- " + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}
	
	public static boolean isEmpty(Collection col){
		return col==null || col.size()==0;
	}
	
	public static void close(InputStream is){
		if(is!=null){
			try {is.close();} catch (IOException e) {}
		}
	}
	
	public static void close(OutputStream os){
		if(os!=null){
			try {os.close();} catch (IOException e) {}
		}
	}
	
	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


