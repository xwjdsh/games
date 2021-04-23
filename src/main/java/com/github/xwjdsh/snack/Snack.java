package com.github.xwjdsh.snack;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.xwjdsh.config.SysConfig;


/**
 * 贪吃蛇对象，包含了大部分逻辑实现
 * @author xwjdsh
 *
 */
public class Snack {
	
	/**
	 * 蛇头
	 */
	private Point head;
	
	/**
	 * 蛇头+蛇身 ，被引用是为了解决类型问题
	 */
	private List<Point> snack;
	private LinkedList<Point> snack_copy;
	
	/**
	 * 初始的蛇的长度
	 */
	private int startLength;
	
	/**
	 * 蛇的方向
	 */
	private Direction direction;
	
	/**
	 * 食物
	 */
	private Point food;
	
	/**
	 * 产生随机数
	 */
	private Random random;
	
	/**
	 * 判断是否改变了蛇
	 */
	private boolean isChange;
	
	
	/**
	 * 游戏地图相关
	 */
	private static final int MAP_MIN_WIDTH= SysConfig.getCfg().getMinWidth();
	private static final int MAP_MAX_WIDTH=SysConfig.getCfg().getMaxWidth()-1;
	private static final int MAP_MIN_HEIGHT=SysConfig.getCfg().getMinHeight();
	private static final int MAP_MAX_HEIGHT=SysConfig.getCfg().getMaxHeight()-1;
	
	/**
	 * 蛇的方向和初始点的数组映射 
	 */
	private static final Map<Direction,Point> STR_POINT;
	static{
		STR_POINT=new HashMap<Snack.Direction, Point>(4);
		STR_POINT.put(Direction.UP, new Point(MAP_MAX_WIDTH>>1,MAP_MAX_HEIGHT));
		STR_POINT.put(Direction.DOWN, new Point(MAP_MAX_WIDTH>>1,MAP_MIN_HEIGHT));
		STR_POINT.put(Direction.LEFT, new Point(MAP_MAX_WIDTH,MAP_MAX_HEIGHT>>1));
		STR_POINT.put(Direction.RIGHT, new Point(MAP_MIN_WIDTH,MAP_MAX_HEIGHT>>1));
	}
	/**
	 * 蛇的方向的枚举类，内部类
	 * @author xwjdsh
	 */
	public static enum Direction{
		LEFT{
		public  Point getNewHead(Point oldHead){
			return new Point(oldHead.x-1,oldHead.y);
		}
		public Rectangle getShadow(Point head) {
			return new Rectangle(0,head.y,head.x+1,1);
		}},
		UP{
		public  Point getNewHead(Point oldHead){
			return new Point(oldHead.x,oldHead.y-1);
		}
		public Rectangle getShadow(Point head) {
			return new Rectangle(head.x,0,1,head.y+1);
		}},
		RIGHT{
		public  Point getNewHead(Point oldHead){
			return new Point(oldHead.x+1,oldHead.y);
		}
		public Rectangle getShadow(Point head) {
			return new Rectangle(head.x,head.y,MAP_MAX_WIDTH-head.x+1,1);
		}},
		DOWN{public  Point getNewHead(Point oldHead){
			return new Point(oldHead.x,oldHead.y+1);
		}
		public Rectangle getShadow(Point head) {
			return new Rectangle(head.x,head.y,1,MAP_MAX_HEIGHT-head.y+1);
		}};
		//获取新的蛇头的坐标，由不同的方向做具体实现
		public abstract Point getNewHead(Point oldHead);
		//获得当前蛇与边框的距离，绘制阴影使用
		public abstract Rectangle getShadow(Point head);
	}
	
	
	public Snack(boolean[][] gameMap){
		this.random=new Random();
		this.startLength=SnackCfg.getSnackCfg().getStartLength();
		init_snack(gameMap);
		init_GameMap(gameMap);
	}

	/**
	 * 初始化蛇的构造方法
	 */
	private void init_snack(boolean[][] gameMap) {
		Direction[] dirs=Direction.values();
		//随机产生一个数来决定蛇的初始方向，在根据方向的数组映射获取初始点
		this.direction=dirs[random.nextInt(dirs.length)];
		this.snack=new LinkedList<Point>();
		this.snack_copy=(LinkedList<Point>)this.snack;
		//因为是引用类型，必须要重新构造
		this.snack_copy.addFirst(new Point(STR_POINT.get(this.direction).x,STR_POINT.get(this.direction).y));
		this.head=snack_copy.getFirst();
		//因为已经添加了一个，所以索引从1开始
		for(int i=1;i<this.startLength;i++){
			this.snack_copy.addFirst(this.direction.getNewHead(this.head));
			this.head=snack_copy.getFirst();
		}
		//初始化食物
		createFood(gameMap);
	}
	
	/**
	 * 被外界访问的改变地图方法，多了一个清空地图
	 */
	public void changeGameMap(boolean[][] gameMap){
		//清空地图
		for(int i=0;i<=MAP_MAX_HEIGHT;i++){
			for(int j=0;j<=MAP_MAX_WIDTH;j++){
				gameMap[i][j]=false;
			}
		}
		//重新初始化
		init_GameMap(gameMap);
	}
	
	/**
	 * 初始化地图方法
	 */
	private void init_GameMap(boolean[][] gameMap){
		int stone=0;
		while(stone<=SnackCfg.getSnackCfg().getStoneCount()){
			int x=random.nextInt(MAP_MAX_WIDTH+1);
			int y=random.nextInt(MAP_MAX_HEIGHT+1);
			if(!canCreateFood(x, y, gameMap)&&(x!=this.food.x||y!=this.food.y)&&safeSnack(x, y)){
				gameMap[y][x]=true;
				stone++;
			}
		}
	}
	
	/**
	 * 保证产生的障碍不会出现在蛇头的周围
	 */
	private boolean safeSnack(int x,int y){
		for(Direction tmpDir : Direction.values()){
			Point tmp=tmpDir.getNewHead(this.head);
			if(tmp.x==x&&tmp.y==y){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 蛇的移动方法
	 * @param map
	 */
	public Boolean move(boolean[][] map){
		//产生新的蛇头
		Point newHead=this.direction.getNewHead(this.head);
		//检测是否越界
		if(isExceedMap(newHead.x, newHead.y, map)){
			return null;
		}
		this.snack_copy.addFirst(newHead);
		this.head=newHead;
		//如果没有吃到食物，那么去尾
		if(!isEatFood()){
			this.snack_copy.removeLast();
			isChange=true;
			return false;
		}
		//产生新的食物
		createFood(map);
		return true;
	}
	
	/**
	 * 检测新的产生的点（蛇头+食物）是否出地图或遇到障碍
	 * @param newHeadX 新的x坐标
	 * @param newHeadY 新的y坐标
	 * @param map 游戏地图
	 * @return  是否失败
	 */
	private boolean isExceedMap(int newPointX,int newPointY,boolean[][] map){
		if(newPointX<MAP_MIN_WIDTH||newPointX>MAP_MAX_WIDTH||newPointY<MAP_MIN_HEIGHT||newPointY>MAP_MAX_HEIGHT||map[newPointY][newPointX]){
			return true;
		}
		//因为蛇头不可能和蛇身（含蛇头）的前4个点相撞，所以索引从4开始
		for(int i=4;i<this.snack_copy.size();i++){
			if(this.snack_copy.get(i).x==newPointX&&this.snack_copy.get(i).y==newPointY){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否吃到食物
	 * @return 是否吃到
	 */
	private boolean isEatFood(){
		return this.head.x==this.food.x&&this.head.y==this.food.y;
	}
	
	/**
	 * 产生新的食物
	 */
	private void createFood(boolean[][] map){
		int foodX=random.nextInt(MAP_MAX_WIDTH+1);
		int foodY=random.nextInt(MAP_MAX_HEIGHT+1);
		if(!canCreateFood(foodX, foodY, map)){
			this.food=new Point(foodX,foodY);
			return;
		}
		createFood(map);
	}

	/**
	 * 判断时候可以在传入参数的点上产生食物
	 * @param foodX x坐标
	 * @param foodY y坐标
	 * @param map 游戏地图
	 * @return 检测结果
	 */
	private boolean canCreateFood(int foodX, int foodY, boolean[][] map) {
		for(Point p : this.snack_copy){
			if(p.x==foodX&&p.y==foodY){
				return false;
			}
		}
		return map[foodY][foodX];
	}

	/**
	 * 检测是否可以改变方向
	 * @param dir 新的方向
	 */
	public boolean canChangeDir(Direction dir){
		return (((this.direction==Direction.LEFT||this.direction==Direction.RIGHT)&&
				(dir==Direction.LEFT||dir==Direction.RIGHT))||
				((this.direction==Direction.UP||this.direction==Direction.DOWN)&&
				(dir==Direction.UP||dir==Direction.DOWN)));
	}
	/**
	 * 得到绘制的阴影
	 * @return 阴影的矩形
	 */
	public Rectangle getShadow() {
		return this.direction.getShadow(this.head);
	}
	
	public LinkedList<Point> getSnack() {
		return snack_copy;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Point getFood() {
		return food;
	}
	
	

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public Point getHead() {
		return head;
	}
	
}
