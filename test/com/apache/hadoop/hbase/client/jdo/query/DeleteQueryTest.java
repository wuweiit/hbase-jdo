package com.apache.hadoop.hbase.client.jdo.query;

import java.util.List;


/**
 * deletion test
 * @author ncanis
 *
 */
public class DeleteQueryTest extends InsertQueryTest{
	public void testDelete() throws Exception {
		SelectQuery sQuery = dbo.createSelectQuery(TABLE);
		DeleteQuery dQuery = dbo.createDeleteQuery(TABLE);
		
		HBaseParam param = new HBaseParam();
		param.addColumn(COLS);
		param.addSearchOption(COL_ID,null, QSearch.LIKE);
		List<TestUserBean> list = (List<TestUserBean>)sQuery.search(FAMILY,param, TestUserBean.class);
		log.debug("founded data={}",list.size());
		for(TestUserBean bean:list){
			log.debug(bean.toString());
			dQuery.deleteRow(bean.getRow());
		}
	}
}
