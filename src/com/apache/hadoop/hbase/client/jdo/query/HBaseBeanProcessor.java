package com.apache.hadoop.hbase.client.jdo.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.HBaseJDOException;
import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;
import com.apache.hadoop.hbase.client.jdo.util.HUtil;

/**
 * This class is for Result convertor
 * 
 * @author ncanis
 *
 */
public class HBaseBeanProcessor implements IHBaseLog{
	public enum ANT_TYPE{INDEX, COLUMN};
	
	
	/**
	 * Result to Bean class ( Object bean binding )	
	 * @param tableName
	 * @param c
	 * @param rs
	 * @return
	 */
	public List<AbstractHBaseBean> convert(String tableName,Class<? extends AbstractHBaseBean> c, 
			ResultScanner rs){		
		List<AbstractHBaseBean> list = new ArrayList<AbstractHBaseBean>();
		for(Result r:rs){
			list.add(convert(tableName,c,r));
		}
		return list;
	}
	
	public Iterator<AbstractHBaseBean> convertIterator(String tableName,Class<? extends AbstractHBaseBean> c, 
			ResultScanner rs, long offset, long limit){		
		return iterator(tableName, c, rs.iterator());
	}
	
	/**
	 * Result to Bean class ( Object bean binding )	
	 * @param tableName
	 * @param c
	 * @param rs
	 * @return
	 */
	public List<? extends AbstractHBaseBean> convert(String tableName, Class<? extends AbstractHBaseBean> c, Result ... rs){		
		List<AbstractHBaseBean> list = new ArrayList<AbstractHBaseBean>();
		for(Result r:rs){
			list.add(convert(tableName,c,r));
		}
		return list;
	}
	
	/**
	 * Result to Bean class ( Object bean binding )	
	 * @param tableName
	 * @param c
	 * @param r
	 * @return
	 */
	public AbstractHBaseBean convert(String tableName,Class<? extends AbstractHBaseBean> c, Result r){
		AbstractHBaseBean bean = null;
		try {
			bean = (AbstractHBaseBean)newInstance(c);
			bean.set(tableName,bean.getFamily());
			bean.setRow(r.getRow());			
			
			List<Field> fieldList = getColumns(c);
			// family, qualifier(key,value)
			NavigableMap<byte[],NavigableMap<byte[],byte[]>> map = r.getNoVersionMap();
			
			bean.setTime(r.getCellValue().getTimestamp());
			Set<byte[]> families = map.keySet();
			for(byte[] family:families){
				bean.setFamily(Bytes.toString(family));
//				BeanUtils.setProperty(bean,Bytes.toString(family), makeValue(field.getType(),values));
				
				NavigableMap<byte[],byte[]> map2 = map.get(family);
				Set<byte[]> columns = map2.keySet();
				for(byte[] col:columns){
					byte[] values = map2.get(col);
					String colName = Bytes.toString(col);
					Field field = getField(fieldList,colName);
					if(field!=null) {
						BeanUtils.setProperty(bean,colName, HUtil.makeValue(field.getType(),values));
					}
				}
			}
		} catch (HBaseJDOException e) {
			log.error("convert",e);
		} catch (IllegalAccessException e) {
			log.error("convert",e);
		} catch (InvocationTargetException e) {
			log.error("convert",e);
		}
		return bean;
	}
	
	/**
	 * get field has the name.
	 * @param list
	 * @param name
	 * @return
	 */
	private Field getField(List<Field> list,String name){
		for(Field f:list){
			if(f.getName().equals(name)){
				return f;
			}
		}
		return null;
	}
	/**
	 * Get the annotation fields from Bean class
	 * @param c
	 * @return
	 */
	public List<Field> getColumns(Class<? extends AbstractHBaseBean> c) {
		return getAnotationFields(c,ANT_TYPE.COLUMN);
	}
	/**
	 * Get the annotation fields of the specified ant_type from Bean class
	 * @param c
	 * @param type Column, Index
	 * @return
	 */
	public List<Field> getAnotationFields(Class<? extends AbstractHBaseBean> c, ANT_TYPE type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			Annotation[] ats = f.getAnnotations();
			for (Annotation at : ats) {
				if (type==ANT_TYPE.INDEX && at.annotationType() == Index.class) {
					list.add(f);
				}else if(type==ANT_TYPE.COLUMN && at.annotationType() == Column.class){
					list.add(f);
				}
			}
		}
		return list;
	}
	
	/**
	 * get the bean data from the specified ant_type
	 * @param bean
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Properties toProperties(AbstractHBaseBean bean, ANT_TYPE type) throws Exception{
		List<Field> list = getAnotationFields(bean.getClass(), type);
		Properties p = new Properties();
		for(Field f:list){
			Object value = PropertyUtils.getProperty(bean,f.getName());
			p.put(f.getName(),HUtil.toBytes(value));
		}
		return p;
	}

	/**
	 * new instance creation.
	 * @param c
	 * @return
	 * @throws HBaseJDOException
	 */
	protected Object newInstance(Class<? extends AbstractHBaseBean> c) throws HBaseJDOException {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			throw new HBaseJDOException("Cannot create " + c.getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new HBaseJDOException("Cannot create " + c.getName() + ": " + e.getMessage());
		}
	}
	
	private Iterator<AbstractHBaseBean> iterator(final String tableName, final Class<? extends AbstractHBaseBean> c,
			final Iterator<Result> it) {
		return new Iterator<AbstractHBaseBean>() {
			public boolean hasNext() {
				return it.hasNext();
			}

			public AbstractHBaseBean next() {
				if (!hasNext()) {
					return null;
				}
				Result result = it.next();
				return convert(tableName, c, result);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
}
