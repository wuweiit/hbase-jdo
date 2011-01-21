package com.apache.hadoop.hbase.client.jdo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.hadoop.fs.Path;

import com.apache.hadoop.hbase.client.jdo.HBaseBigFile;
import com.apache.hadoop.hbase.client.jdo.query.JDOTest;


public class HBaseBigFileTest extends JDOTest{
	public void testBigFile() throws FileNotFoundException {
		HBaseBigFile ht = new HBaseBigFile();
		
		File file = new File("doc/movie.avi");
		FileInputStream fis = new FileInputStream(file);
		Path rootPath = new Path("/files/");
		String filename = "movie.avi";

		start();
		boolean isUploaded = ht.uploadFile(rootPath,filename,fis,true);
		assertTrue(isUploaded);
		log.debug("uploaded={}, size={} bytes. time={} ms",
				new Object[]{isUploaded, file.length(), end()});
		
		start();
		boolean isReceived = ht.copyFile2Local(new Path(rootPath,filename), new File("test/"+filename));
		assertTrue(isReceived);
		log.debug("saved file time={} ms",end());

	}
}
