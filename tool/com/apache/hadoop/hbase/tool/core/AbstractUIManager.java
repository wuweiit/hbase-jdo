package com.apache.hadoop.hbase.tool.core;

import java.util.Hashtable;
import java.util.Properties;


/**
 * @author ncanis
 *
 */
public abstract class AbstractUIManager {
	private Hashtable<IView, IRootPanel> tables = new Hashtable<IView,IRootPanel>();
	private IChanger changer = null;
	private IRootPanel rootPanel;
	private IView curView;
	private IView befView;
	
	public IChanger getChanger() {
		return changer;
	}
	
	public void setInfo(IRootPanel root, IChanger changer){
		this.rootPanel = root;
		this.changer = changer;
	}

	public IView getCurView() {
		return curView;
	}

	public IView getBefView() {
		return befView;
	}

	public IRootPanel getRootPanel() {
		return rootPanel;
	}

	public void update(){
		update(tables.keySet().toArray(new IView[tables.keySet().size()]));
	}

	public void update(IView ...views){
		updateMain();
		if(views!=null) {
			for(IView view:views){
				tables.get(view).update();
			}
		}
	}

	public void updateMain(){
		rootPanel.update();
	}

	public void changeBackUI() {
		if(this.befView==null) return;
		changeUI(befView,true);
	}
	
	public IRootPanel getPanel(IView view){
		synchronized(AbstractUIManager.class) {
			if(tables.get(view)!=null){
				return tables.get(view);
			}else{
				IRootPanel panel = view.make();
				tables.put(view,panel);
				return panel;
			}
		}
	}
	
	public void changeUI(IView menu,boolean isCallUI) {
		if(this.rootPanel==null) {
			return;
		}else if(menu==befView || isCallUI==false){
			return;
		}
		
		if(befView!=null) {
			IRootPanel curPanel = getPanel(befView);
			curPanel.clearPanel();
			curPanel.closePanel();
		}
		
		IRootPanel nextPanel = getPanel(menu);				
		boolean isMoved = changer.changeUI(nextPanel,menu);
		if(isMoved){
			nextPanel.startPanel();
			this.befView = menu;
		}		
	}
	
}
