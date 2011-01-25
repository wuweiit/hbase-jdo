package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.Font;


public class GFont {

	private static GFont gFont;
	private Font font;
	
	public static GFont get(){
		synchronized(GFont.class) {
			if (gFont == null) {
				gFont = new GFont();
			}
			return gFont;
		}		
	}

	public Font font() {
		if ( font == null ){
			font = new Font("\uad74\ub9bc", Font.PLAIN, 11);
		}
		return font;
	}
}
