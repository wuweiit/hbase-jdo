package com.apache.hadoop.hbase.client.jdo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.tool.HBaseTool;

public class HConfigUtil implements IHBaseLog{
	public static final String HBASE_CONFIG = "/hbase-site.xml";
	
	public static URL getHBaseUrl(){
		return HConfigUtil.class.getResource(HBASE_CONFIG);
	}
	public static HBaseConfiguration makeHBaseConfig(){
		return new HBaseConfiguration();
	}
	
	public static Configuration makeConfig(){
		return new Configuration();
	}
	
	public static HBaseConfiguration saveProperty(String name, String value){		
		HBaseConfiguration config = null;
		FileOutputStream fos = null;
		try {
			config = makeHBaseConfig();
			config.set(name,value);
			URL url = HBaseTool.class.getResource("/hbase-site.xml");
			File f = new File(url.toURI());
			config.writeXml(new FileOutputStream(f));
			
			config.reloadConfiguration();
			log.debug("saved property name={} , value={}",name,value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			HUtil.close(fos);
		}
		return null;
	}
}
