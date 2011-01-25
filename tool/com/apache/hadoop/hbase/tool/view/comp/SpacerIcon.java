package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class SpacerIcon implements Icon{
	 private int extraHeight;

     private Icon realIcon;

     public SpacerIcon(int extraHeight) {
        if (extraHeight < 0) {
           throw
             new IllegalArgumentException("extraHeight must be >= 0");
        }
        this.extraHeight = extraHeight;
     }
     public SpacerIcon(int extraHeight, Icon icon) {
        this(extraHeight);
        if (icon == null) {
           throw new IllegalArgumentException("icon cannot be null");
        }
        this.realIcon = icon;
     }
     public int getIconHeight() {
        if (realIcon == null)
           return extraHeight;
        else
           return realIcon.getIconHeight() + extraHeight;
     }
     public int getIconWidth() {
        if (realIcon == null)
           return 0;
        else
           return realIcon.getIconWidth();
     }
     public void paintIcon(Component c, Graphics g, int x, int y) {
        if (realIcon != null) {
           g.translate(0, extraHeight / 2);
           realIcon.paintIcon(c, g, x, y);
           g.translate(0, -extraHeight / 2);
        }
     }
}
