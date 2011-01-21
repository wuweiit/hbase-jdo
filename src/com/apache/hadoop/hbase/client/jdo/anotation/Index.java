package com.apache.hadoop.hbase.client.jdo.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HBase Index Column.
 * 
 * Index annotation.
 * If you have index column in hbase table, you must declare Index annotation in Field of Bean.
 * 
 * Bean's Field name must same column name of hbase table.
 * @author ncanis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Index {

}
