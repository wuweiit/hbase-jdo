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
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.Filter;

/**
 * A filter, based on the PageFilter, takes two arguments: limit and offset.
 * This filter can be used for specified row count, in order to efficient
 * lookups and paginated results for end users.
 * 
 * This filter can be used when regions are only one.
 */
@Deprecated
public class RowPaginationFilter implements Filter {
	private int rowsAccepted = 0;
	private int limit = 0;
	private int offset = 0;

	/**
	 * Default constructor, filters nothing. Required though for RPC
	 * deserialization.
	 */
	public RowPaginationFilter() {
	}

	/**
	 * Constructor that takes a maximum page size.
	 * 
	 * get row from offset to offset+limit ( offset<= row<=offset+limit )
	 * @param offset start position
	 * @param limit count from offset position
	 */
	public RowPaginationFilter(final int offset, final int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	@Override
	public void reset() {
		// noop
	}
	@Override
	public boolean filterAllRemaining() {
		return this.rowsAccepted > this.limit+this.offset;
	}
	@Override
	public boolean filterRowKey(byte[] rowKey, int offset, int length) {
		return false;
	}
	@Override
	public void readFields(final DataInput in) throws IOException {
		this.offset = in.readInt();
		this.limit = in.readInt();
	}
	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(offset);
		out.writeInt(limit);
	}
	@Override
	public ReturnCode filterKeyValue(KeyValue v) {
		return ReturnCode.INCLUDE;
	}
	//true to exclude row, false to include row.
	@Override
	public boolean filterRow() {		
		boolean isExclude = this.rowsAccepted < this.offset || this.rowsAccepted>=this.limit+this.offset;
		rowsAccepted++;
		return isExclude;
	}

	@Override
	public void filterRow(List<KeyValue> kvs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasFilterRow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KeyValue getNextKeyHint(KeyValue currentKV) {
		// TODO Auto-generated method stub
		return null;
	}
}
