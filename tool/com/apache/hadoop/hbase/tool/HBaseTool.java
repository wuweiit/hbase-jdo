package com.apache.hadoop.hbase.tool;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.apache.hadoop.hbase.tool.view.HMainPanel;
import com.apache.hadoop.hbase.tool.view.HMenuBar;
import com.apache.hadoop.hbase.tool.view.HView;
import com.apache.hadoop.hbase.tool.view.UIManagerImpl;

/**
 * @author ncanis
 *
 */
public class HBaseTool {
	static {
		HConstants.LAF();
	}
	
	public HBaseTool(){
	}
	
	private void startUI() {
		final JFrame frame = new JFrame();		
		frame.setTitle("HBase Tool");

		int width = 1200;
		int height = 900;
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if ( (int) screenSize.getWidth() < 1280 ) width = 1024;
		if ( (int) screenSize.getHeight() < 1024 ) height = 768;

		frame.setSize(width, height);
		frame.setLocationRelativeTo( null );
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// init panel.
		HMainPanel mPanel = new HMainPanel();
		UIManagerImpl.get().setRootPanel(mPanel);
		UIManagerImpl.get().setFrame(frame);
		
		HMenuBar bar = new HMenuBar();
		frame.setJMenuBar(bar.getGmMenu());
		mPanel.setMenuPanel(bar);
		frame.setContentPane(mPanel);
		
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result=JOptionPane.showConfirmDialog(frame,"Exit","Exit Program",JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if(result==JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		
		// start Table view.
		UIManagerImpl.get().changeUI(HView.TABLE);
	}

	
	public static void main(String[] args) {
		HBaseTool client = new HBaseTool();
		client.startUI();
	}
}
