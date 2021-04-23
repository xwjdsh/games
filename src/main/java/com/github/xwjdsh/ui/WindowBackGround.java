package com.github.xwjdsh.ui;

import java.awt.Graphics;

import com.github.xwjdsh.config.SysConfig;


/**
 * 背景窗口，不需要边框，仅仅画背景
 * @author xwjdsh
 */
public class WindowBackGround extends Window {
		
	public WindowBackGround(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(StyleImage.BG_IMG, 0, 0, SysConfig.getCfg().getWidth(), SysConfig.getCfg().getHeight(), null);
	}
}
