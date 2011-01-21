package com.apache.hadoop.hbase.client.jdo.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.query.HBaseBeanProcessor.ANT_TYPE;


/**
 * processor test
 * @author ncanis
 *
 */
public class HBaseBeanProcessorTest extends TestCase{
	HBaseBeanProcessor proc = new HBaseBeanProcessor();
	
	private Result makeResult(String rowKey){
		List<KeyValue> list = new ArrayList<KeyValue>();
		byte[] row = Bytes.toBytes(rowKey);
		byte[] family = Bytes.toBytes("user");
		byte[] qualifier = Bytes.toBytes("id");
		byte[] value = Bytes.toBytes("value-id-"+rowKey);
		KeyValue kv = new KeyValue(row,family,qualifier,value);
		
		byte[] qualifier2 = Bytes.toBytes("name");
		byte[] value2 = Bytes.toBytes("value-name-"+rowKey);
		KeyValue kv2 = new KeyValue(row,family,qualifier2,value2);
		
		list.add(kv);
		list.add(kv2);
		
		Result r = new Result(list);
		return r;
	}
	
	public void testConvert() throws Exception {
		Result result = makeResult("1");
		TestUserBean bean = (TestUserBean)proc.convert("tableName",TestUserBean.class,result);
		assertEquals(Bytes.toString(bean.getRow()),"1");
		assertEquals(bean.getId(),"value-id-1");
		assertEquals(bean.getName(),"value-name-1");
	}
	
	public void testConvertList() throws Exception {
		Result result1 = makeResult("key1");
		Result result2 = makeResult("key2");
		Result result3 = makeResult("key3");
		List<TestUserBean> list = (List<TestUserBean>)proc.convert("tableName",TestUserBean.class,result1,result2,result3);
		
		for(TestUserBean bean:list){
			System.out.println("family="+bean.getFamily());
			System.out.println("id="+bean.getId());
			System.out.println("name="+bean.getName());
		}
	}
	
	public void testProperties() throws Exception {
		TestUserBean bean = new TestUserBean();
		bean.setId("id-value");
		bean.setName("name-value");
		
		Properties p = proc.toProperties(bean,ANT_TYPE.COLUMN);
		assertEquals(p.get("id"),"id-value");
		assertEquals(p.get("name"),"name-value");
	}
}
