package com.apache.hadoop.hbase.tool.view;

import com.apache.hadoop.hbase.tool.core.IRootPanel;
import com.apache.hadoop.hbase.tool.core.IView;
import com.apache.hadoop.hbase.tool.view.search.HSearchView;
import com.apache.hadoop.hbase.tool.view.table.HTableView;


/**
 * @author ncanis
 *
 */
public enum HView implements IView{
	TABLE(1,"Table",HTableView.class),
	SEARCH(2,"Search",HSearchView.class),
	HADOOP(3,"Hadoop",null),
	;
	
	private final int index;	 
	private final String desc;
	private final Class viewClass;
	
	private HView(int index, String desc,Class viewClass){
		this.index = index;
		this.desc = desc;
		this.viewClass = viewClass;
	}
	
	public int getIndex() {
		return index;
	}

	public String getDesc() {
		return desc;
	}
	
	public static HView getView(int index){
		for(HView view:HView.values()) {
			if(view.index==index) return view;
		}
		return null;
	}
	
	public static HView getView(String title){
		for(HView view:HView.values()) {
			if(view.desc==title) return view;
		}
		return null;
	}

	@Override
	public IRootPanel make() {
		try {
			return (IRootPanel)viewClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}

