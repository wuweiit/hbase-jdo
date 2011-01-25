package com.apache.hadoop.hbase.tool.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import com.apache.hadoop.hbase.tool.core.IChanger;
import com.apache.hadoop.hbase.tool.core.IRootPanel;
import com.apache.hadoop.hbase.tool.core.IView;
import com.apache.hadoop.hbase.tool.core.UIResult;
import com.apache.hadoop.hbase.tool.view.comp.SpacerIcon;
import com.apache.hadoop.hbase.tool.view.search.HSearchView;
import com.apache.hadoop.hbase.tool.view.table.HTableView;

/**
 * @author ncanis
 *
 */
public class HMainPanel extends AbstractHPanel implements IRootPanel,IChanger {
	private JLabel label_status = null;
	private JPanel southPanel = null;
	private HMenuBar menuBarPanel = null;
	private JTabbedPane tpane_center = null;
	
	
	// panels
	private HTableView tablePanel;
	private HSearchView searchPanel;
	
	public HMainPanel(){
		initialize();
	}
	
	////////////////////////////////////////////////////////////////////
	private void initialize() {
		tablePanel = (HTableView)UIManagerImpl.get().getPanel(HView.TABLE);
		searchPanel = (HSearchView)UIManagerImpl.get().getPanel(HView.SEARCH);

		label_status = new JLabel();
		label_status.setText("Hbase tool");
		setLayout(new BorderLayout());
//		this.setSize(new Dimension(812, 267));
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		this.add(getTpane_center(), BorderLayout.CENTER);

	}

	////////////////////////////////////////////////////////

//	private int getTabIndex(HView gmView){
//		for ( int i = 0; i< tpane_center.getTabCount(); i++ ){
//			String tpane_title = tpane_center.getTitleAt(i);
//			if ( gmView.getDesc().equalsIgnoreCase(tpane_title))
//				return i;
//		}
//		return 0;
//	}
//	
//	public void changeTabEnable(boolean state) {
//		for ( int i = 0; i< tpane_center.getTabCount(); i++ ){
//			tpane_center.setEnabledAt(i, state);
//		}
//	}

	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			southPanel.setLayout(new FlowLayout());
			southPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			southPanel.setPreferredSize(new Dimension(94, 30));
			southPanel.setPreferredSize(new Dimension(94, 35));
			southPanel.add(label_status, null);
		}
		return southPanel;
	}
	
	@Override
	public void initStatus(int status) {
		if(menuBarPanel!=null) menuBarPanel.initStatus(status);
	}
	
	
	public void setMenuPanel(HMenuBar bar) {
		menuBarPanel = bar;
	}

	/**
	 * This method initializes tpane_center	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTpane_center() {
		if (tpane_center == null) {
			tpane_center = new JTabbedPane();
			tpane_center.addTab(HView.TABLE.getDesc(),  new SpacerIcon(30), tablePanel, null);
			tpane_center.addTab(HView.SEARCH.getDesc(),  new SpacerIcon(30), searchPanel, null);
			tpane_center.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					JTabbedPane pane = (JTabbedPane)e.getSource();

					int index = pane.getSelectedIndex();
					String title = pane.getTitleAt(index);
					HView view = HView.getView(title);
					UIManagerImpl.get().changeUI(view);	
				}
			});
		}
		return tpane_center;
	}

	@Override
	public boolean changeUI(IRootPanel panel, IView view) {
		return true;
	}

	@Override
	public void update() {
	}

	@Override
	public void startPanel() {
	}

	@Override
	public void clearPanel() {
	}

	@Override
	public void closePanel() {
		
	}

	@Override
	public UIResult checkAvailable() {
		return UIResult.SUCCESS;
	}

	@Override
	public boolean doConfirm() {
		return true;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
