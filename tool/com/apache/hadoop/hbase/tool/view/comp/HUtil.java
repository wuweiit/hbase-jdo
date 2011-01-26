package com.apache.hadoop.hbase.tool.view.comp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.hsqldb.lib.Collection;

public class HUtil {
	private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat df3 = new SimpleDateFormat("dd");
	private static final String pattern = "###.000";
	private static final DecimalFormat df4 = new DecimalFormat(pattern);
	private static final DecimalFormat float_format = new DecimalFormat("#.000");

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
}
