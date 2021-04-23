package com.github.xwjdsh.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Random;

import com.github.xwjdsh.config.GameConfig;
import com.github.xwjdsh.config.SysConfig;

/**
 * 游戏运行的主窗口
 * @author xwjdsh
 */
public class WindowGame extends Window{
	
	/**
	 * 表示活动点的图片
	 */
	private static int CELL_SIZE= SysConfig.getCfg().getCellSize();
	
	/**
	 * 产生随机数，主要用于产生阴影颜色
	 */
	private Random random=new Random();
	
	
	public WindowGame(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	
	/**
	 * 绘制方法
	 */
	@Override
	public void paint(Graphics g) {
		//绘制边框
		createWindow(g);
		if(this.gameDto==null){
			//在dto为空的时候绘制选项
			drawSelect(g);
		}else{
			if(this.gameDto.isStart()){
				//绘制地图
				drawMap(g);
				//绘制活动点集
				drawActive(g);
				//绘制其他的显示
				drawOther(g);
				//绘制阴影
				drawShadow(g);
				//绘制暂停
				drawSuspend(g);
			}else{
				drawDeath(g);
			}
		}
	}

	/**
	 * 当游戏结束时，改变显示
	 * @param g
	 */
	private void drawDeath(Graphics g) {
		for(int y=0;y<this.gameDto.getMap().length;y++){
			for(int x=0;x<this.gameDto.getMap()[0].length;x++){
				if(this.gameDto.getMap()[y][x]){
					draw(g,GameImage.DEATH_IMG , x, y);
				}
			}
		}
		for(Point p : this.gameDto.getActive()){
			draw(g,GameImage.DEATH_IMG,p.x,p.y);
		}
	}



	/**
	 * 绘制阴影
	 */
	private void drawShadow(Graphics g) {
		if(this.gameDto.isShowShadow()){
		//如果绘制阴影开关打开并且子类重写了得到阴影方法
			if(this.gameDto.getShadowRectangle()!=null){
				Color color=new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
				g.setColor(color);
				Rectangle re=this.gameDto.getShadowRectangle();
				g.draw3DRect(this.x+re.x*CELL_SIZE+BORDERSIZE, this.y+re.y*CELL_SIZE+BORDERSIZE,
						CELL_SIZE*re.width, CELL_SIZE*re.height,true);
			}
			if(this.gameDto.getShadowPoint()!=null){
				Color color=new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
				g.setColor(color);
				for(Point p : this.gameDto.getShadowPoint()){
					g.draw3DRect(this.x+BORDERSIZE+p.x*CELL_SIZE, this.y+BORDERSIZE+p.y*CELL_SIZE, CELL_SIZE, CELL_SIZE, true);
				}
			}
		}
	}



	/**
	 * 绘制地图
	 */
	private void drawMap(Graphics g) {
		for(int y=0;y<this.gameDto.getMap().length;y++){
			for(int x=0;x<this.gameDto.getMap()[0].length;x++){
				if(this.gameDto.getMap()[y][x]){
					draw(g, GameImage.MAP_IMG, x, y);
				}
			}
		}
	}
	
	/**
	 * 绘制活动点集
	 * @param g
	 */
	private void drawActive(Graphics g) {
		for(Point point : this.gameDto.getActive()){
			draw(g, GameImage.ACTION_IMG, point.x, point.y);
		}
	}
	
	/**
	 * 私有的绘图方法
	 * @param g 画笔对象
	 * @param img 绘制的图片
	 * @param x 相对坐标x
	 * @param y 相对坐标y
	 */
	private void draw(Graphics g,Image img,int x,int y){
		g.drawImage(img,
				this.x+x*CELL_SIZE+BORDERSIZE, 
				this.y+y*CELL_SIZE+BORDERSIZE,
				this.x+(x+1)*CELL_SIZE+BORDERSIZE, 
				this.y+(y+1)*CELL_SIZE+BORDERSIZE, 0, 0, CELL_SIZE, CELL_SIZE, null);
	}
	
	/**
	 * 绘制开始的游戏选项 
	 */
	//TODO 写到配置文件
	private void drawSelect(Graphics g){
		Collection<GameConfig> gamecfg_list=SysConfig.getCfg().getGameMap().values();
		int startX=this.x+SysConfig.getCfg().getBorderSize()*2;
		//绘制字的方法，初始x在左下角，跟绘制图片不一样
		int startY=this.y+(this.h-(2*gamecfg_list.size()-1)*20)/2+20;
		g.setColor(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
		g.setFont(new Font("楷体", Font.BOLD, 20));
		int i=1;
		for(GameConfig gamecfg : gamecfg_list){
			g.drawString((i++)+" ."+gamecfg.getGameName(), startX, startY);
			startY+=40;
		}
	}
	
	/**
	 * 绘制其他的扩充
	 */
	private void drawOther(Graphics g){
		if(this.gameDto.getExpandPoint1()!=null){
			draw(g,GameImage.EXPAND_POINT1_IMG, this.gameDto.getExpandPoint1().x, gameDto.getExpandPoint1().y);
		}
		if(this.gameDto.getExpandPoint2()!=null){
			draw(g, GameImage.EXPAND_POINT2_IMG, this.gameDto.getExpandPoint2().x, gameDto.getExpandPoint2().y);
		}
		if(this.gameDto.getExpandListPoint1()!=null){
			for(Point p : this.gameDto.getExpandListPoint1()){
				draw(g,GameImage.EXPAND_LISTPOINT1_IMG, p.x, p.y);
			}
		}
		if(this.gameDto.getExpandListPoint2()!=null){
			for(Point p : this.gameDto.getExpandListPoint2()){
				draw(g,GameImage.EXPAND_LISTPOINT2_IMG, p.x, p.y);
			}
		}
		if(this.gameDto.getExpandListPoint3()!=null){
			for(Point p : this.gameDto.getExpandListPoint3()){
				draw(g,GameImage.EXPAND_LISTPOINT3_IMG, p.x, p.y);
			}
		}
		if(this.gameDto.getExpandListPoint4()!=null){
			for(Point p : this.gameDto.getExpandListPoint4()){
				draw(g,GameImage.EXPAND_LISTPOINT4_IMG, p.x, p.y);
			}
		}
	}
	
	/**
	 * 绘制暂停图片
	 */
	private void drawSuspend(Graphics g) {
		if(this.gameDto.isSuspend()){
			g.drawImage(StyleImage.SUSPEND_IMG,this.x+(this.w-StyleImage.SUSPEND_IMG.getWidth(null)>>1),this.y+(this.h-StyleImage.SUSPEND_IMG.getHeight(null)>>1),null);
		}
	}

}
