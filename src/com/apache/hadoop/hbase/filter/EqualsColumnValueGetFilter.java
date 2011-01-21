/**
 * Copyright 2008 The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apache.hadoop.hbase.filter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.FilterBase;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * This filter can be used for specified row count, 
 * in order to find cout column value existing.
 * 
 */
@Deprecated
public class EqualsColumnValueGetFilter extends FilterBase {
	private byte[] value;
	private byte[] colName;
	private int limit;
	private int count;

	/**
	 * Default constructor, filters nothing. Required though for RPC
	 * deserialization.
	 */
	public EqualsColumnValueGetFilter() {
	}

	/**
	 * Constructor that column's value is equal.
	 * 
	 * @param colName column name
	 * @param value column value
	 * @param limit column count
	 */
	public EqualsColumnValueGetFilter(final byte[] colName, final byte[] value, final int limit) {
		this.colName = colName;
		this.value = value;
		this.limit = limit;
	}

	@Override
	public void reset() {
		// noop
	}
	@Override
	public boolean filterAllRemaining() {
		return this.count > this.limit;
	}
	@Override
	public boolean filterRowKey(byte[] rowKey, int offset, int length) {
		return false;
	}
	@Override
	public void readFields(final DataInput in) throws IOException {
		this.limit = in.readInt();
		int colSize = in.readInt();
		
		this.colName = new byte[colSize];
		in.readFully(colName);
		
		int valueSize = in.readInt();
		this.value = new byte[valueSize];
		in.readFully(value);
	}
	@Override
	public void write(final DataOutput out) throws IOException {
		int colSize = colName.length;
		int valueSize = value.length;
		
		out.writeInt(limit);
		
		out.writeInt(colSize);
		out.write(colName);
		
		out.writeInt(valueSize);
		out.write(value);
	}
	
	@Override
	public ReturnCode filterKeyValue(KeyValue v) {
		if(v.getValue().length!=value.length) { // for performace.
			return ReturnCode.NEXT_COL;
		}else if(Arrays.equals(value,v.getValue())){
			count++;
			return ReturnCode.INCLUDE;
		}else{
			return ReturnCode.NEXT_COL;
		}
	}
	
	//true to exclude row, false to include row.
	@Override
	public boolean filterRow() {		
		return false;
	}
}
