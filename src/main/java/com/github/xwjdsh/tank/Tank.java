package com.github.xwjdsh.tank;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.xwjdsh.config.SysConfig;

public class Tank {

	/**
	 * 坦克的方向
	 */
	private Direction dir;

	/**
	 * 坦克的坐标集合
	 */
	private List<Point> tank_points;

	/**
	 * 坦克的射击点
	 */
	private Point shoot_point;

	/**
	 * 子弹发射点
	 */
	private Point bullet_point;

	/**
	 * 坦克是否是敌人
	 */
	private boolean isEnemy;
	
	/**
	 * 坦克的旋转中心点坐标
	 */
	private int roundX;
	private int roundY;
	

	/**
	 * 限定地图的边界
	 */
	private static final int MAP_MIN_WIDTH = SysConfig.getCfg().getMinWidth();
	private static final int MAP_MAX_WIDTH = SysConfig.getCfg().getMaxWidth() - 1;
	private static final int MAP_MIN_HEIGHT = SysConfig.getCfg().getMinHeight();
	private static final int MAP_MAX_HEIGHT = SysConfig.getCfg().getMaxHeight() - 1;
	
	/**
	 * 游戏地图
	 */
	private static boolean[][] gameMap;
	
	/**
	 * 敌人坦克的坐标集合，方便显示层
	 */
	private static List<Point> enemysPoints;
	
	/**
	 * 障碍的坐标集
	 */
	private static List<Point> hindrances;

	/**
	 * 敌人坦克的子弹集合(方向与子弹的映射)
	 */
	private static Map<Direction, List<Point>> enemy_bullets;

	/**
	 * 玩家坦克的子弹集合(方向与子弹的映射)
	 */
	private static Map<Direction, List<Point>> player_bullets;
	
	/**
	 * 方向的左右映射
	 */
	private static Map<Direction,Direction> leftMap;
	private static Map<Direction,Direction> rightMap;
	
	/**
	 * 初始化子弹和方向的映射关系，静态代码块
	 */
	static {
		init_tankGame();
	}

	/**
	 * 初始化坦克游戏的静态对象
	 */
	public static void init_tankGame(){
		enemysPoints=new ArrayList<Point>();
		enemy_bullets = new HashMap<Direction, List<Point>>();
		player_bullets = new HashMap<Direction, List<Point>>();
		leftMap=new HashMap<Tank.Direction, Tank.Direction>();
		rightMap=new HashMap<Tank.Direction, Tank.Direction>();
		hindrances=new ArrayList<Point>();
		Direction[] directions=Direction.values();
		for(int i=0;i<directions.length;i++){
			enemy_bullets.put(directions[i], new ArrayList<Point>());
			player_bullets.put(directions[i], new ArrayList<Point>());
			//分别存储4个方向的左右方向
			leftMap.put(directions[i], directions[i==0?3:i-1]);
			rightMap.put(directions[i], directions[i==3?0:i+1]);
		}
		for(Point p : TankCfg.getTankCfg().getHindrances()){
			hindrances.add(new Point(p.x,p.y));
		}
	}
	
	/**
	 * 初始化坦克
	 * @param isEnemy 是否是敌人
	 * @param tank_points 坦克的坐标集合
	 */
	public Tank(boolean isEnemy,List<Point> tank_points) {
		this.isEnemy = isEnemy;
		init_tank(tank_points);
	}

	/**
	 * 初始化坦克
	 */
	private void init_tank(List<Point> tank_points) {
		this.tank_points=tank_points;
		if(this.isEnemy){
			enemysPoints.addAll(tank_points);
		}
		//敌人和玩家初始不同的方向
		this.dir=isEnemy?Direction.DOWN:Direction.UP;
		//射击点，配置文件中存在第二个位置
		this.shoot_point=tank_points.get(1);
		//初始化子弹发射点
		this.bullet_point=new Point(this.shoot_point.x,this.shoot_point.y);
		this.dir.bullet_move(this.bullet_point);
	}
	
	/**
	 * 坦克还原
	 */
	public void restore(){
		for(List<Point> tank : TankCfg.getTankCfg().getEnemys()){
			boolean isRestore=true;
			for(Point p : tank){
				if(gameMap[p.y][p.x]){
					isRestore=false;
					break;
				}
			}
			if(isRestore){
				List<Point> tank_points=new ArrayList<Point>();
				for(Point p : this.tank_points){
					gameMap[p.y][p.x]=false;
				}
				for(Point p : tank){
					gameMap[p.y][p.x]=true;
					tank_points.add(new Point(p.x,p.y));
				}
				enemysPoints.removeAll(this.tank_points);
				this.init_tank(tank_points);
			}
		}
	}
	
	/**
	 * 坦克的移动方法，调用方向的移动方法并更新子弹发射点
	 */
	public void move(){
		if(this.dir.tank_move(this.tank_points, this.shoot_point)){
			this.bullet_point.x=this.shoot_point.x;
			this.bullet_point.y=this.shoot_point.y;
			this.dir.bullet_move(this.bullet_point);
		}
	}

	/**
	 * 射击方法
	 */
	public void shoot() {
		//构造新的子弹
		Point bullet = new Point(this.bullet_point.x, this.bullet_point.y);
		//根据isEnemy来选择对应的引用
		Map<Direction, List<Point>> tmp=this.isEnemy?enemy_bullets:player_bullets;
		//加入到集合中
		tmp.get(this.dir).add(bullet);
	}
	
	/**
	 * 坦克旋转的方法(左右90度)
	 * x0= (x - rx0)*cos(a) - (y - ry0)*sin(a) + rx0 ;
     * y0= (x - rx0)*sin(a) + (y - ry0)*cos(a) + ry0 ;
     * @param isCW 是否是顺时针旋转
	 */
	private void round(boolean isCW){
		roundX=this.tank_points.get(0).x;
		roundY=this.tank_points.get(0).y;
		for(int i=1;i<this.tank_points.size();i++){
			gameMap[this.tank_points.get(i).y][this.tank_points.get(i).x]=false;
			//不能直接改变，必须先计算出坐标，再一起赋值
			int x=isCW?roundY+roundX-tank_points.get(i).y:roundX-roundY+tank_points.get(i).y;
			int y=isCW?roundY-roundX+tank_points.get(i).x:roundX+roundY-tank_points.get(i).x;
			gameMap[y][x]=true;
			tank_points.get(i).x=x;
			tank_points.get(i).y=y;
		}
	}
	
	/**
	 * 坦克的旋转(180度)
	 */
	private void round(){
		roundX=this.tank_points.get(0).x;
		roundY=this.tank_points.get(0).y;
		for(int i=1;i<this.tank_points.size();i++){
			gameMap[this.tank_points.get(i).y][this.tank_points.get(i).x]=false;
			tank_points.get(i).x=roundX*2-tank_points.get(i).x;
			tank_points.get(i).y=roundY*2-tank_points.get(i).y;
			gameMap[this.tank_points.get(i).y][this.tank_points.get(i).x]=true;
		}
	}
	
	/**
	 * 将方向与点集的映射转换为点集
	 */
	public static List<Point> transformList(Map<Direction,List<Point>> bulletMap){
		List<Point> bulletsList=new ArrayList<Point>();
		for(Map.Entry<Direction, List<Point>> entry : bulletMap.entrySet()){
			for(Point p : entry.getValue()){
				bulletsList.add(p);
			}
		}
		return bulletsList;
	}
	
	public List<Point> getTank_points() {
		return tank_points;
	}

	public static Map<Direction, List<Point>> getEnemy_bullets() {
		return enemy_bullets;
	}

	public static Map<Direction, List<Point>> getPlayer_bullets() {
		return player_bullets;
	}

	public Direction getDir() {
		return dir;
	}
	
	public static List<Point> getEnemysPoints() {
		return enemysPoints;
	}
	
	public static void setGameMap(boolean[][] gameMap) {
		Tank.gameMap = gameMap;
	}

	public static List<Point> getHindrances() {
		return hindrances;
	}

	/**
	 * 改变方向，并进行对应的旋转
	 * @param direction 新改变的方向
	 */
	public void setDir(Direction direction) {
		if(rightMap.get(rightMap.get(this.dir))==direction){
				round();	
		}else if(leftMap.get(this.dir)==direction){
			round(false);
		}else if(rightMap.get(this.dir)==direction){
			round(true);
		}
		this.dir = direction;
		this.bullet_point.x=this.tank_points.get(1).x;
		this.bullet_point.y=this.tank_points.get(1).y;
		this.dir.bullet_move(this.bullet_point);
	}
	
	public Point getShoot_point() {
		return shoot_point;
	}


	/**
	 * 静态方向枚举
	 * @author xwjdsh
	 */
	static enum Direction {
		UP {
			protected boolean tank_move(List<Point> tank,Point shoot_point) {
				if (!(shoot_point.y - 1 < MAP_MIN_HEIGHT)&&!gameMap[shoot_point.y-1][shoot_point.x]
						&&!gameMap[shoot_point.y-1][shoot_point.x+1]&&!gameMap[shoot_point.y-1][shoot_point.x-1]){
					for (Point p : tank) {
						gameMap[p.y][p.x]=false;
					}
					for (Point p : tank) {
						gameMap[--p.y][p.x]=true;
					}
					
					return true;
				}
				return false;
			}

			protected boolean bullet_move(Point bullet) {
				if(bullet.y<=MAP_MIN_HEIGHT){
					return false;
				}
				bullet.y--;
				return true;
			}
		},
		RIGHT {
			protected boolean tank_move(List<Point> tank,Point shoot_point) {
				if (!(shoot_point.x + 1 > MAP_MAX_WIDTH)&&!gameMap[shoot_point.y][shoot_point.x+1]
						&&!gameMap[shoot_point.y+1][shoot_point.x+1]&&!gameMap[shoot_point.y-1][shoot_point.x+1]) {
					for (Point p : tank) {
						gameMap[p.y][p.x]=false;
					}
					for(Point p : tank){
						gameMap[p.y][++p.x]=true;
					}
					return true;
				}
				return false;
			}
			protected boolean bullet_move(Point bullet) {
				if(bullet.x>=MAP_MAX_WIDTH){
					return false;
				}
				bullet.x++;
				return true;
			}
		},
		DOWN {
			protected boolean tank_move(List<Point> tank,Point shoot_point) {
				if (!(shoot_point.y + 1 > MAP_MAX_HEIGHT)&&!gameMap[shoot_point.y+1][shoot_point.x]
						&&!gameMap[shoot_point.y+1][shoot_point.x-1]&&!gameMap[shoot_point.y+1][shoot_point.x+1]) {
					for (Point p : tank) {
						gameMap[p.y][p.x]=false;
					}
					for(Point p : tank){
						gameMap[++p.y][p.x]=true;
					}
					return true;
				}
				return false;
			}
			protected boolean bullet_move(Point bullet) {
				if(bullet.y>=MAP_MAX_HEIGHT){
					return false;
				}
				bullet.y++;
				return true;
			}
		},
		LEFT {
			protected boolean tank_move(List<Point> tank,Point shoot_point) {
				if (!(shoot_point.x - 1 < MAP_MIN_WIDTH)&&!gameMap[shoot_point.y][shoot_point.x-1]
						&&!gameMap[shoot_point.y-1][shoot_point.x-1]&&!gameMap[shoot_point.y+1][shoot_point.x-1]) {
					for (Point p : tank) {
						gameMap[p.y][p.x]=false;
					}
					for(Point p : tank){
						gameMap[p.y][--p.x]=true;
					}
					return true;
				}
				return false;
			}
			protected boolean bullet_move(Point bullet) {
				if(bullet.x<=MAP_MIN_WIDTH){
					return false;
				}
				bullet.x--;
				return true;
			}

		};
		
		// 坦克的移动方法
		protected abstract boolean tank_move(List<Point> tank,Point shoot_point);

		// 子弹的移动方法
		protected abstract boolean bullet_move(Point bullet);
	}
}
