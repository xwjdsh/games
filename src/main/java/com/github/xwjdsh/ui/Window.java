package com.github.xwjdsh.ui;

import java.awt.Graphics;
import java.awt.Image;

import com.github.xwjdsh.config.SysConfig;
import com.github.xwjdsh.dto.GameDto;


/**
 * 构造边框的抽象基类，提供一个绘制边框的方法，子类用到。抽象方法由子类实现，主要用来执行对应逻辑
 * 在类中定义了一个上层的dto对象，会通过JPanel传递一个具体的dto对象，再由子类读取数据显示
 * @author xwjdsh
 */
public abstract class Window {
	/**
	 * 窗口的起始x坐标
	 */
	protected int x;
	
	/**
	 * 窗口的起始y坐标
	 */
	protected int y;
	
	/**
	 * 窗口的宽
	 */
	protected int w;
	
	/**
	 * 窗口的高
	 */
	protected int h;
	
	/**
	 * 游戏数据对象
	 */
	protected GameDto gameDto;
	
	/**
	 * 围成窗口的边框图片
	 */
	private static Image IMG = StyleImage.BORDER_IMG;
	
	/**
	 * 边框的长度
	 */
	public final static int BORDERSIZE = SysConfig.getCfg().getBorderSize();
	
	/**
	 * 边框图片的宽
	 */
	private final static int IMG_W = IMG.getWidth(null);
	
	/**
	 * 边框图片的高
	 */
	private final static int IMG_H = IMG.getHeight(null);
	
	
	public Window(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	
	/**
	 * 绘制窗口边框
	 */
	protected void createWindow(Graphics g) {
		// 左上
		g.drawImage(StyleImage.BORDER_IMG, x, y, x + BORDERSIZE, y + BORDERSIZE, 0, 0, BORDERSIZE, BORDERSIZE, null);
		// 上
		g.drawImage(StyleImage.BORDER_IMG, x + BORDERSIZE, y, x + w - BORDERSIZE, y + BORDERSIZE, BORDERSIZE, 0, IMG_W- BORDERSIZE, BORDERSIZE, null);
		// 右上
		g.drawImage(StyleImage.BORDER_IMG, x + w - BORDERSIZE, y, x + w, y + BORDERSIZE, IMG_W - BORDERSIZE, 0,IMG_W, BORDERSIZE, null);
		// 右
		g.drawImage(StyleImage.BORDER_IMG, x + w - BORDERSIZE, y + BORDERSIZE, x + w, y + h - BORDERSIZE, IMG_W- BORDERSIZE, BORDERSIZE, IMG_W, IMG_H - BORDERSIZE, null);
		// 右下
		g.drawImage(StyleImage.BORDER_IMG, x + w - BORDERSIZE, y + h - BORDERSIZE, x + w, y + h, IMG_W - BORDERSIZE,IMG_H - BORDERSIZE, IMG_W, IMG_H, null);
		// 下
		g.drawImage(StyleImage.BORDER_IMG, x + BORDERSIZE, y + h - BORDERSIZE, x + w - BORDERSIZE, y + h, BORDERSIZE,IMG_H - BORDERSIZE, IMG_W - BORDERSIZE, IMG_H, null);
		// 左下
		g.drawImage(StyleImage.BORDER_IMG, x, y + h - BORDERSIZE, x + BORDERSIZE, y + h, 0, IMG_H - BORDERSIZE,BORDERSIZE, IMG_H, null);
		// 左
		g.drawImage(StyleImage.BORDER_IMG, x, y + BORDERSIZE, x + BORDERSIZE, y + h - BORDERSIZE, 0, BORDERSIZE, BORDERSIZE,IMG_H - BORDERSIZE, null);
		// 中间
		g.drawImage(StyleImage.BORDER_IMG, x + BORDERSIZE, y + BORDERSIZE, x + w - BORDERSIZE, y + h - BORDERSIZE, BORDERSIZE,BORDERSIZE, IMG_W - BORDERSIZE, IMG_H - BORDERSIZE, null);
	}
	
	/**
	 * 绘制窗口的抽象方法，由各个窗口具体实现
	 */
	public abstract void paint(Graphics g);


	public void setGameDto(GameDto gameDto) {
		this.gameDto = gameDto;
	}
}
