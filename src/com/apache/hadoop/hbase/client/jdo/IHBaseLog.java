package com.apache.hadoop.hbase.client.jdo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HBase JDO Logger.
 * 
 * If you want to logging, you should implement IHBaseLog.
 * @author ncanis
 */
public interface IHBaseLog {
	Logger log = LoggerFactory.getLogger("hbase.jdo");
}
