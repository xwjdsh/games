package com.github.xwjdsh.tank;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.github.xwjdsh.dto.GameDto;

/**
 * 坦克的游戏数据对象，主要包含了敌人坦克的数组和一个玩家坦克
 * @author xwjdsh
 */
public class GameDto2Tank extends GameDto{
	
	private Tank[] enemyTanks;
	
	private Tank playerTank;
	
	private List<Point> enemy_shoots;
	
	public GameDto2Tank(){
		this.threadSleepTime=500;
		enemy_shoots=new ArrayList<Point>();
		init_enemyTanks();
		init_playerTank();
		init_map();
	}
	
	/**
	 * 初始化地图
	 */
	private void init_map() {
		for(Point p : TankCfg.getTankCfg().getHindrances()){
			this.map[p.y][p.x]=true;
		}
		Tank.setGameMap(this.map);
	}

	/**
	 * 初始化玩家坦克
	 */
	private void init_playerTank() {
		//从游戏配置对象中取得引用
		List<Point> player=TankCfg.getTankCfg().getPlayerTank();
		//玩家的点集拷贝
		List<Point> player_copy=new ArrayList<Point>();
		for(int i=0;i<player.size();i++){
			this.map[player.get(i).y][player.get(i).x]=true;
			player_copy.add(new Point(player.get(i).x,player.get(i).y));
		}
		this.playerTank=new Tank(false,player_copy);
	}

	/**
	 * 初始化敌人的坦克
	 */
	private void init_enemyTanks(){
		List<List<Point>> enemys=TankCfg.getTankCfg().getEnemys();
		enemyTanks=new Tank[enemys.size()];
		for(int i=0;i<enemyTanks.length;i++){
			List<Point> enemy=enemys.get(i);
			List<Point> enemy_copy=new ArrayList<Point>();
			for(int j=0;j<enemy.size();j++){
				this.map[enemy.get(j).y][enemy.get(j).x]=true;
				enemy_copy.add(new Point(enemy.get(j).x,enemy.get(j).y));
			}
			enemyTanks[i]=new Tank(true, enemy_copy);
		}
	}

	/**
	 * 覆写扩充点集1，用来表示玩家的子弹
	 */
	@Override
	public List<Point> getExpandListPoint1() {
		return Tank.transformList(Tank.getPlayer_bullets());
	}
	
	/**
	 * 覆写扩充点集2，用来表示敌人的子弹
	 */
	@Override
	public List<Point> getExpandListPoint2() {
		return Tank.transformList(Tank.getEnemy_bullets());
	}
	
	/**
	 * 覆写扩充点集3，用来表示敌人的坦克
	 */
	@Override
	public List<Point> getExpandListPoint3() {
		return Tank.getEnemysPoints();
	}
	
	/**
	 * 覆写扩充点集4，用来表示敌人坦克的射击点
	 */
	@Override
	public List<Point> getExpandListPoint4() {
		enemy_shoots.clear();
		for(Tank enemy : this.enemyTanks){
			enemy_shoots.add(enemy.getShoot_point());
		}
		return this.enemy_shoots;
	}
	
	/**
	 * 覆写扩充点1，用来表示老王的坐标
	 */
	@Override
	public Point getExpandPoint1() {
		return TankCfg.getTankCfg().getGeneral();
	}
	
	

	/**
	 * 覆写扩充点2，用来表示玩家坦克的射击点
	 */
	@Override
	public Point getExpandPoint2() {
		return playerTank.getShoot_point();
	}
	

	@Override
	public void clearAll() {
		super.init_gameDto();
		Tank.init_tankGame();
		init_enemyTanks();
		init_playerTank();
		init_map();
	}

	@Override
	public List<Point> getActive() {
		return playerTank.getTank_points();
	}
	
	
	
	

	@Override
	public void setLevel(int level) {
		super.setLevel(level);
		this.threadSleepTime=this.threadSleepTime<100?this.threadSleepTime:this.threadSleepTime-20*level;
	}

	public Tank[] getEnemyTanks() {
		return enemyTanks;
	}

	public void setEnemyTanks(Tank[] enemyTanks) {
		this.enemyTanks = enemyTanks;
	}

	public Tank getPlayerTank() {
		return playerTank;
	}

	public void setPlayerTank(Tank playerTank) {
		this.playerTank = playerTank;
	}
}
