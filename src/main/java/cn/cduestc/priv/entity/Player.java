package cn.cduestc.priv.entity;

import java.io.Serializable;

/**
 * 玩家对象，因为要写排行记录文件，所以需要实现序列化接口
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class Player implements Serializable{
	//姓名
	private String name;
	//分数
	private int score;
	//等级
	private int level;
	//已完成
	private int done;
	
	public Player(String name,int score,int done,int level) {
		this.name=name;
		this.score=score;
		this.done=done;
		this.level=level;
	}
	
	public String getName() {
		return name;
	}
	public int getScore() {
		return score;
	}
	public int getLevel() {
		return level;
	}

	public int getDone() {
		return done;
	}
}
