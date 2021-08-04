package com.github.xwjdsh.ui;

import java.awt.Graphics;

/**
 * 放置按钮的小窗口
 * @author xwjdsh
 */
public class WindowButton extends Window{
	
	public WindowButton(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	@Override
	public void paint(Graphics g) {
		createWindow(g);
	}
}
