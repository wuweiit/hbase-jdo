package com.apache.hadoop.hbase.tool;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.LoggerFactory;

import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.tool.core.ConsoleStream;
import com.apache.hadoop.hbase.tool.core.IConsoleHandler;
import com.apache.hadoop.hbase.tool.core.Log4jLogAppender;
import com.apache.hadoop.hbase.tool.view.HMainPanel;
import com.apache.hadoop.hbase.tool.view.HMenuBar;
import com.apache.hadoop.hbase.tool.view.HView;
import com.apache.hadoop.hbase.tool.view.UIManagerImpl;

/**
 * @author ncanis
 *
 */
public class HBaseTool implements IHBaseLog{
	static {
		HToolConstants.LAF();
	}
	
	public HBaseTool(){
	}
	
	private void startUI() {
		final JFrame frame = new JFrame();		
		frame.setTitle(HToolConstants.TITLE);

		int width = 1200;
		int height = 900;
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if ( (int) screenSize.getWidth() < 1280 ) width = 1024;
		if ( (int) screenSize.getHeight() < 1024 ) height = 768;

		frame.setSize(width, height);
		frame.setLocationRelativeTo( null );
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// init panel.
		final HMainPanel mPanel = new HMainPanel();
		UIManagerImpl.get().setInfo(mPanel,mPanel);
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
		
		// catch console log
//		mPanel.getTaMessage();
//		java.util.logging.Logger rootLog = LogManager.getLogManager().getLogger("");		
//		rootLog.addHandler(new JDKLogHandler(mPanel.getTaMessage()));
		
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("error",e);
			}
		});
		
		org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(log.getName());		
		logger.addAppender(new Log4jLogAppender(mPanel.getTaMessage()));
//		
//		rootLog.addHandler(new ConsoleHandler());
//		
//		ConsoleStream cs = new ConsoleStream(System.out,new IConsoleHandler() {
//			@Override
//			public void message(String msg) {
//				mPanel.getTaMessage().append(msg);
//				log.debug(msg);
//			}
//		});
//		PrintStream ps = new PrintStream(cs);
//		System.setOut(ps);
//		System.setErr(ps);
		
		
		log.debug("started HBase Tool");
		
		// start Table view.
		UIManagerImpl.get().changeUI(HView.TABLE,true);
	}

	
	public static void main(String[] args) {
		HBaseTool client = new HBaseTool();
		client.startUI();
	}
}
