package cn.cduestc.priv.dto;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 * Data Transfer Object数据传输对象
 * 抽象类，游戏数据的基类，封装了游戏通用的数据
 * @author xwjdsh
 */
public abstract class GameDto{
	
	/**
	 * 游戏的地图
	 */
	protected boolean[][] map;
	
	/**
	 * 当前得分
	 */
	protected int score;
	
	/**
	 * 当前等级(可以通过这个来控制速度)
	 */
	protected int level;
	
	/**
	 * 已经完成的
	 */
	protected int alreadyDone;
	
	/**
	 * 游戏是否开始
	 */
	protected boolean isStart;
	
	/**
	 * 游戏是否暂停
	 */
	protected boolean isSuspend;
	
	/**
	 * 游戏是否绘制阴影
	 */
	protected boolean isShowShadow;
	
	/**
	 * 游戏是否返回，定义此成员是为了解决游戏返回依然记录成绩的bug
	 */
	protected boolean isBack;
	
	/**
	 * 游戏线程的间隔暂停时间
	 */
	protected long threadSleepTime;
	
	
	
	protected GameDto(){
		init_gameDto();
	}
	
	/**
	 * 初始游戏数据，也被清除方法调用
	 */
	protected void init_gameDto(){
		this.map=new boolean[20][10];
		this.score=0;
		this.level=1;
		this.alreadyDone=0;
		this.isSuspend=false;
		this.isStart=false;
	}

	/**
	 * 抽象方法，清除游戏数据
	 */
	public abstract void clearAll();

	/**
	 * 抽象方法，获取活动点集
	 * @return 游戏的活动点集
	 */
	public abstract List<Point> getActive();
	
	/**
	 * 返回游戏活动点的阴影矩形，子类覆写，不硬要求
	 */
	public Rectangle getShadowRectangle(){return null;}
	
	/**
	 * 返回游戏活动点的投影数组，子类覆写，不硬要求
	 */
	public Point[] getShadowPoint(){return null;}
	
	/**
	 * 扩充点1
	 */
	public Point getExpandPoint1() {return null;}
	
	/**
	 * 扩充点2
	 */
	public Point getExpandPoint2(){return null;}
	
	/**
	 * 扩充点集1
	 */
	public List<Point> getExpandListPoint1() { return null;}
	
	/**
	 * 扩充点集2
	 */
	public List<Point> getExpandListPoint2(){ return null;}
	
	/**
	 * 扩充点集3
	 */
	public List<Point> getExpandListPoint3(){ return null;}
	
	/**
	 * 扩充点集4
	 */
	public List<Point> getExpandListPoint4(){ return null;}
	

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAlreadyDone() {
		return alreadyDone;
	}

	public void setAlreadyDone(int alreadyDone) {
		this.alreadyDone = alreadyDone;
	}


	public boolean[][] getMap() {
		return map;
	}

	public boolean isStart() {
		return this.isStart;
	}
	
	

	public boolean isShowShadow() {
		return isShowShadow;
	}

	public void changeShowShadow() {
		this.isShowShadow = !this.isShowShadow;
	}

	public void setStart(boolean start) {
		this.isStart = start;
	}
	
	public List<Point> getExpandListShow1(){
		return null;
	}

	public boolean isSuspend() {
		return isSuspend;
	}

	public void changeSuspend() {
		this.isSuspend = !this.isSuspend;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isBack() {
		return isBack;
	}

	public void setBack(boolean isback) {
		this.isBack = isback;
	}

	public long getThreadSleepTime() {
		return threadSleepTime;
	}
	
	
	
}
