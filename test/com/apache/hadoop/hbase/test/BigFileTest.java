package com.apache.hadoop.hbase.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Stress Test
 * @author ncanis@gmail.com
 *
 */
public class BigFileTest extends AbstractHBase{
	private String tableName;
	private String family[];
	private String cols[];
	private SecureRandom srand = null;
	
	public BigFileTest(String tableName, String[] family, String[] cols){
		this.tableName = tableName;
		this.family = family;
		this.cols = cols;
		srand = new SecureRandom(Bytes.toBytes(System.currentTimeMillis()));
	}
	
	public void deleteTable() throws IOException {		
		deleteTable(tableName);		
	}
	public void createTable() throws Exception {
		createTable(tableName, family);
	}
	
	public void insertBigFile(File file) throws IOException {
		long start = System.currentTimeMillis();
		HTable table = new HTable(config, tableName.getBytes());
		table.setWriteBufferSize(1024*1024*1);
		table.setAutoFlush(false);
		
		byte[] data = readFully(new File("doc/movie.avi"));
		
		String key= "key"+srand.nextLong();
		Put p = new Put(key.getBytes());		
		p.add(family[0].getBytes(), cols[0].getBytes(),file.getName().getBytes());				
		p.add(family[0].getBytes(), cols[1].getBytes(),data);
		table.put(p);

		table.flushCommits();		
		table.close();
		long end = System.currentTimeMillis();
		log.debug("big file inserting Finished. time={}ms",(end-start));
	}
	
	private byte[] readFully(File file) throws IOException {
		FileInputStream in = null;
		ByteArrayOutputStream baos = null;
		BufferedInputStream bis = null;
		
		try{
			in = new FileInputStream(file);
			baos = new ByteArrayOutputStream(10240);
			byte[] buf = new byte[8192];

			bis = new BufferedInputStream(in);
			int read = 0;
			int total = 0;
			while((read=bis.read(buf))!=-1) {
				baos.write(buf, 0, read);
				total+=read;
				System.out.println("read="+total);
			}
		}catch(IOException e){
			throw e;
		}finally {
			bis.close();
			baos.close();
			in.close();
		}
		
		return baos.toByteArray();
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		String table ="tost_bigtable";
		String[] family = {"filelist"};
		String[] cols = {"name","data"};
		
		BigFileTest test = new BigFileTest(table,family,cols);
		// create & delete
		test.deleteTable();
		test.createTable();

		test.insertBigFile(new File("doc/movie.avi"));
	}
}
