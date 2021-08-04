package com.github.xwjdsh.snack;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import com.github.xwjdsh.dto.GameDto;

/**
 *针对贪吃蛇的数据封装类，继承自游戏数据基类，主要封装了蛇对象。扩充了1，2点方法，来表示蛇头和食物
 * @author xwjdsh
 */
public class GameDto2Snack extends GameDto{

	/**
	 * 蛇对象
	 */
	private Snack snack;
	
	public GameDto2Snack() {
		this.snack=new Snack(this.map);
		this.threadSleepTime=500;
	}
	
	public List<Point> getActive() {
		return snack.getSnack();
	}

	public Snack getSnack() {
		return snack;
	}

	/**
	 * 用来表示食物
	 */
	@Override
	public Point getExpandPoint1() {
		return this.snack.getFood();
	}
	
	/**
	 * 用来表示蛇头
	 */
	@Override
	public Point getExpandPoint2() {
		return snack.getHead();
	}
	
	public int getAlreadyDone() {
		return super.getAlreadyDone();
	}
	
	/**
	 * 重载绘制阴影的方法
	 */
	@Override
	public Rectangle getShadowRectangle() {
		return snack.getShadow();
	}

	/**
	 * 清除游戏数据
	 */
	@Override
	public void clearAll() {
		super.init_gameDto();
		this.snack=new Snack(this.map);
	}

	@Override
	public void setLevel(int level) {
		super.setLevel(level);
		//升级的时候改变线程暂停的时间
		this.threadSleepTime=this.level>6?100:580-this.level*80;
	}
	
	

}
