package cn.cduestc.priv.tetris;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.cduestc.priv.config.SysConfig;


/**
 * 俄罗斯方块的实体类，封装在dto中，可以由逻辑层获取并修改
 * @author xwjdsh
 */
public class Square {
	
	/**
	 * 存储方块种类的集合，在静态代码块中为其加入元素
	 */
	private static List<List<Point>> point_list=TetrisCfg.getTetrisCfg().getSquare_list();
	
	
	/**
	 * 限定地图的边界 
	 */
	private static final int MAP_MIN_WIDTH=SysConfig.getCfg().getMinWidth();
	private static final int MAP_MAX_WIDTH=SysConfig.getCfg().getMaxWidth()-1;
	private static final int MAP_MIN_HEIGHT=SysConfig.getCfg().getMinHeight();
	private static final int MAP_MAX_HEIGHT=SysConfig.getCfg().getMaxHeight()-1;
	
	/**
	 * 当前的方块数组
	 */
	private List<Point> squares;
	
	/**
	 * 当前的方块类型，之所以定义是因为要使正方形不能旋转
	 */
	private int current_type;
	/**
	 * 下一个方块类型
	 */
	private int next_type;
	
	
	public Square() {
		this.current_type=new Random().nextInt(point_list.size());
		this.squares =nextSquares(current_type);
		this.next_type=new Random().nextInt(point_list.size());
	}

	/**
	 * 方块的移动方法
	 * @param x 向x轴的偏移
	 * @param y 向y轴的偏移
	 * @return 是否移动成功，下键需要根据返回值进一步处理
	 */
	public boolean move(int x,int y,boolean[][] map){
		//两次循环第一次检验，第二次改变
		for(Point point : squares){
			if(isExceedMap(point.x+x, point.y+y,map))
				return false;
		}
		for(Point point : squares){
			point.x+=x;
			point.y+=y;
		}
		return true;
	}
	
	/**
	 * 方块的旋转
	 */
	public void rotate(boolean[][] map){
		//排除正方形方块的旋转
		if(current_type==1)
			return;
		//检验
		for(int i=1;i<this.squares.size();i++){
			int newX=squares.get(0).y+squares.get(0).x-squares.get(i).y;
			int newY=squares.get(0).y-squares.get(0).x+squares.get(i).x;
			if(isExceedMap(newX, newY,map))
				return;
		}
		//改变
		for(int i=1;i<this.squares.size();i++){
			int newX=squares.get(0).y+squares.get(0).x-squares.get(i).y;
			int newY=squares.get(0).y-squares.get(0).x+squares.get(i).x;
			squares.get(i).x=newX;
			squares.get(i).y=newY;
		}
	}
	/**
	 *方块的消行
	 * @param map 游戏地图
	 * @return 一次性消行的总数
	 */
	public int RemoveLine(boolean[][] map){
		int startRow=-1;
		int removeLineNumber=0;
		//用循环是排除一次消行后又堆积成行的情况  TODO 是否必要？
		while((startRow=isRemoveLine(map))>=0){
			for(int y=startRow;y>0;y--){
				for(int x=0;x<map[0].length;x++){
					map[y][x]=map[y-1][x];
				}
			}
			removeLineNumber++;
		}
		return removeLineNumber;
	}
	
	/**
	 * 内部方法，得到待消的行数
	 * @param map 游戏地图
	 * @return 从下往上第一个要消的行号
	 */
	private int isRemoveLine(boolean[][] map){
		for(int y=MAP_MAX_HEIGHT;y>=0;y--){
			if(isRowRemoveLine(y, map)){
				return y;
			}
		}
		return -1;
	}
	/**
	 * 内部方法，判断某一行是否可消行
	 */
	private boolean isRowRemoveLine(int y,boolean[][] map){
		for(int x=0;x<=MAP_MAX_WIDTH;x++){
			if(!map[y][x])
				return false;
		}
		return true;
	}
	
	
	/**
	 * 内部方法，判断改变后的坐标是否超过地图边界
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean isExceedMap(int newX,int newY,boolean[][] map){
		return newX<MAP_MIN_WIDTH||newX>MAP_MAX_WIDTH||newY<MAP_MIN_HEIGHT||newY>MAP_MAX_HEIGHT||map[newY][newX];
	}
	/**
	 * 刷新新的方块
	 * @param type 代表方块的类型
	 */
	public void changeSquares(List<Point> next){
		this.squares=next;
		this.current_type=next_type;
	}
	/**
	 * 获取下一个方块
	 * @param type 方块序号，由逻辑层随机产生
	 */
	public List<Point> nextSquares(int type){
		this.next_type=type;
		List<Point> tmp=point_list.get(type);
		List<Point> next=new ArrayList<Point>(tmp.size());
		for(int i=0;i<tmp.size();i++){
			next.add(new Point(tmp.get(i).x,tmp.get(i).y));
		}
		return next;
	}
	
	public List<Point> getSquares() {
		return squares;
	}
	

	public static List<List<Point>> getPoints_list() {
		return point_list;
	}

	/**
	 * 返回当前活动点的阴影矩形
	 * @return 阴影矩形
	 *//*
	 *矩形阴影
	public Rectangle getShadow() {
		int minX=10,minY=20,maxX=0;
		for(Point p : this.squares){
			minX=p.x<minX?p.x:minX;
			maxX=p.x>maxX?p.x:maxX;
			minY=p.y<minY?p.y:minY;
		}
		return new Rectangle(minX,minY,maxX-minX+1,MAP_MAX_HEIGHT-minY+1);
	}*/
	
	/**
	 * 返回当前活动点的阴影点集合
	 */
	public Point[] getShadow(boolean[][] gameMap){
		Point[] shadow=new Point[this.squares.size()];
		//取得当前活动点的副本
		for(int i=0;i<shadow.length;i++){
			Point p=this.squares.get(i);
			shadow[i]=new Point(p.x,p.y);
		}
		while(true){
			for(Point p : shadow){
				if(isExceedMap(p.x, p.y+1, gameMap)){
					return shadow;
				}
			}
			for(Point p : shadow){
				p.y+=1;
			}
		}
	}

	public int getNext_type() {
		return next_type;
	}
	
	
	
}
