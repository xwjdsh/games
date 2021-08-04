package com.github.xwjdsh.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import com.github.xwjdsh.config.SysConfig;

/**
 * 游戏显示的小窗口
 * @author xwjdsh
 */
public class WindowShow extends Window{
	
	/**
	 * 取得窗口的拔高
	 */
	private int panding= SysConfig.getCfg().getPanding();
	
	/**
	 * 获取数字图片相关，避免重复取值
	 */
	private static int NUM_IMG_W=StyleImage.NUM_IMG.getWidth(null)/10;
	private static int NUM_IMG_H=StyleImage.NUM_IMG.getHeight(null);
	
	/**
	 * 获取各图片的高，便于确定显示位置
	 */
	private static int LEVEL_IMG_H=StyleImage.LEVEL_IMG.getHeight(null);
	private static int GRADE_IMG_H=StyleImage.GRADE_IMG.getHeight(null);
	private static int DONE_IMG_H=StyleImage.DONE_IMG.getHeight(null);
	private static int NEXT_IMG_H=StyleImage.NEXT_IMG.getHeight(null);
	
	
	public WindowShow(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	

	@Override
	public void paint(Graphics g) {
		createWindow(g);
		if(this.gameDto!=null){
			//等级图片
			drawString(g,StyleImage.LEVEL_IMG,this.x+this.panding,this.y+this.panding);
			//等级数字
			drawNumber(g, this.x+this.panding-5,this.y+LEVEL_IMG_H, this.gameDto.getLevel());
			//分数图片
			drawString(g, StyleImage.GRADE_IMG,this.x+this.panding,this.y+this.panding+this.h/5);
			//分数数字
			drawNumber(g, this.x+this.panding-5, this.y+this.h/5+GRADE_IMG_H, this.gameDto.getScore());
			//已完成图片
			drawString(g, StyleImage.DONE_IMG, this.x+this.panding, this.y+this.panding+this.h/5*2);
			//已完成数字
			drawNumber(g, this.x+this.panding-5, this.y+this.h/5*2+DONE_IMG_H+2, this.gameDto.getAlreadyDone());
			//绘制扩充的
			drawOther(g);
		}
	}
	
	/**
	 * 绘制其他
	 * @param g
	 */
	private void drawOther(Graphics g) {
		if(this.gameDto.getExpandListShow1()!=null){
			//绘制下一个图片
			drawString(g, StyleImage.NEXT_IMG, this.x+this.panding, this.y+this.panding+this.h/5*3);
			//使居中
			int minX=10;
			for(Point p : this.gameDto.getExpandListShow1()){
				minX=p.x<minX?p.x:minX;
			}
			//显示
			for(Point p : this.gameDto.getExpandListShow1()){
				g.drawImage(GameImage.EXPAND_LISTSHOW1, this.x+this.panding*2+(p.x-minX)*20, this.y+this.panding*2+this.h/5*3+NEXT_IMG_H+p.y*20,
						this.x+this.panding*2+(p.x-minX+1)*20, this.y+this.panding*2+this.h/5*3+NEXT_IMG_H+(p.y+1)*20, 0, 0, 20, 20, null);
			}
		}
	}


	/**
	 * 绘制图片的方法，简单的对drawimage做了封装
	 */
	private void drawString(Graphics g,Image img,int startX,int startY){
		g.drawImage(img, startX, startY,null);
	}
	
	/**
	 * 绘制数字图片的方法
	 * @param g 画笔
	 * @param startX 起点x坐标
	 * @param startY 起点y坐标
	 * @param number 要绘制的数字
	 */
	private void drawNumber(Graphics g,int startX,int startY,int number){
		String numberStr=String.valueOf(number);
		//限定位数为4位，左对齐
		startX+=(4-numberStr.length())*NUM_IMG_W;
		for(int i=0;i<numberStr.length();i++){
			int one=numberStr.charAt(i)-'0';
			g.drawImage(StyleImage.NUM_IMG, startX+i*NUM_IMG_W, startY, startX+NUM_IMG_W*(i+1), startY+NUM_IMG_H,
					NUM_IMG_W*one, 0, NUM_IMG_W*(one+1), NUM_IMG_H, null);
		}
	}
}
