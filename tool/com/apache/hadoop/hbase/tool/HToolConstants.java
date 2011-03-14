package com.apache.hadoop.hbase.tool;

import javax.swing.UIManager;

/**
 * @author ncanis
 *
 */
public class HToolConstants {
	
	public static final int STATUS_INIT = 0;
	public static final String TITLE = "HBase Tool ver0.21(http://code.google.com/p/hbase-jdo/)";

	public static void LAF() {
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //$NON-NLS-1$
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {}
	}
}
