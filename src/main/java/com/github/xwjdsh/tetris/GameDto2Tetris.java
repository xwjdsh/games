package com.github.xwjdsh.tetris;

import java.awt.Point;
import java.util.List;

import com.github.xwjdsh.dto.GameDto;

/**
 * 针对俄罗斯方块的数据封装类，继承自游戏数据基类，主要封装了方块对象，扩充了点集合显示的方法，用来表示下一个
 * @author xwjdsh
 */
public class GameDto2Tetris extends GameDto {
	
	/**
	 * 方块对象
	 */
	private Square squre;
	
	/**
	 * 下一个方块对象
	 */
	private List<Point> next;
	
	public GameDto2Tetris() {
		this.squre=new Square();
		this.next=squre.nextSquares(squre.getNext_type());
		//初始线程暂停时间为900毫秒
		this.threadSleepTime=900;
	}

	public Square getSqure() {
		return squre;
	}
	
	/**
	 * 下一个方块的点集
	 */
	@Override
	public List<Point> getExpandListShow1() {
		return next;
	}

	@Override
	public Point[] getShadowPoint() {
		return squre.getShadow(this.map);
	}
	
	

	public List<Point> getNext() {
		return next;
	}

	public void setNext(List<Point> next) {
		this.next = next;
	}

	/**
	 * 取得活动点集合
	 */
	@Override
	public List<Point> getActive() {
		return this.squre.getSquares();
	}

	/**
	 * 清除游戏数据
	 */
	@Override
	public void clearAll() {
		super.init_gameDto();
		this.squre=new Square();
	}

	//在改变等级的时候改变线程暂停时间（最高900->1,最低100->21）
	@Override
	public void setLevel(int level) {
		super.setLevel(level);
		this.threadSleepTime=this.getLevel() > 21 ? 100 : -40
				* this.getLevel() + 940;
	}
	
	
	
	
}
