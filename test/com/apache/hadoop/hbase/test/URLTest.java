package com.apache.hadoop.hbase.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * 1. hadoop fs -copyFromLocal test/test.txt hdfs://samba.samba:9100/test/test.txt
 *    를 통해 로컬에 있는 test.txt를 hdfs로 복사한다.
 * 2. URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory()) 를 지정한다.
 *    (hdfs 프로토콜을 인식시키기위해)
 *    
 * 3. URL로 연결하여 hadoop 에 있는 test.txt를 읽어온다. 
 * 
 * 
 * @author ncanis
 */
public class URLTest {
	static{
		
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		InputStream in = new URL("hdfs://192.168.0.203:9100/test/test.txt").openStream();
		IOUtils.copyBytes(in,System.out, 4096,false);
		IOUtils.closeStream(in);
	}

}
