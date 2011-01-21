package com.apache.hadoop.hbase.client.jdo;

import com.apache.hadoop.hbase.client.jdo.query.DeleteQuery;
import com.apache.hadoop.hbase.client.jdo.query.InsertQuery;
import com.apache.hadoop.hbase.client.jdo.query.SelectQuery;
import com.apache.hadoop.hbase.client.jdo.query.UpdateQuery;

/**
 * HBaseDBO Implementation.
 * 
 * you can crate custom DBOImpl.
 * @author ncanis
 *
 */
public class HBaseDBOImpl extends AbstractHBaseDBO{

	@Override
	public SelectQuery createSelectQuery(String table) {
		SelectQuery query = new SelectQuery(this,table);
		return query;
	}

	@Override
	public DeleteQuery createDeleteQuery(String table) {
		DeleteQuery query = new DeleteQuery(this,table);
		return query;
	}

	@Override
	public InsertQuery createInsertQuery(String table) {
		InsertQuery query = new InsertQuery(this,table);
		return query;
	}

	@Override
	public UpdateQuery createUpdateQuery(String table) {
		UpdateQuery query = new UpdateQuery(this,table);
		return query;
	}

}
