package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ExcelButton extends JButton{
	public ExcelButton() {
		super();
		initialize();
		init();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
			
	}

	private void init() {
		BufferedImage bi;
		try {
			URL url = this.getClass().getResource("/com/apache/hadoop/hbase/tool/view/comp/excel.jpg");
			bi = ImageIO.read(url);
			Icon icon = new ImageIcon(bi); 
			setIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setText("Excel");
	}
	
	
}
