package com.apache.hadoop.hbase.tool.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.tool.core.IRootPanel;
import com.apache.hadoop.hbase.tool.core.UIResult;

/**
 * @author ncanis
 */
public abstract class AbstractHPanel extends JPanel implements IRootPanel, IHBaseLog{
	protected final JFrame frame = UIManagerImpl.get().getFrame(); 
	public final void showSimpleDialog(String message){		
		JOptionPane.showMessageDialog(getParent(),message);
	}
	public final void showDialog ( Component component, boolean isModal, String title, boolean isShowOkBtn, int width, int height ){
		final JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(isModal);		
		dialog.setSize(width, height);
		dialog.setLocationRelativeTo(getParent());

		JButton btn = new JButton("OK"); //$NON-NLS-1$
		btn.setVisible(false);
		btn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(component, BorderLayout.CENTER);
		panel.add(btn, BorderLayout.SOUTH);
		dialog.setContentPane(panel);
		dialog.setVisible(true);
		if ( isShowOkBtn ){
			btn.setVisible(true);
		}

	}
	public final void showDialog( Component component, boolean isModal , String title){
		showDialog( component, isModal, title, true, 600,300);
		
	}
	protected final int showConfirmDialog(String message,String title){
		 return JOptionPane.showConfirmDialog(getParent(), message, title, JOptionPane.YES_NO_OPTION);
	}
	
	public final String showInputDialog(String message, String title, String value){
		return (String) JOptionPane.showInputDialog(getParent(), message, title, JOptionPane.QUESTION_MESSAGE,null,null,value);
	}
	

	@Override
	public UIResult checkAvailable() {
		return UIResult.SUCCESS;
	}

	@Override
	public boolean doConfirm() {
		return true;
	}
}
