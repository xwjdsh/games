package com.github.xwjdsh.config;

/**
 * 按钮的配置类
 * @author xwjdsh
 */
public class ButtonCfg {
	//按钮的x坐标
	private final int x;
	//按钮的y坐标
	private final int y;
	//按钮的宽
	private final int w;
	//按钮的高
	private final int h;
	
	
	public ButtonCfg(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public int getW() {
		return w;
	}


	public int getH() {
		return h;
	}
	
	
}	
