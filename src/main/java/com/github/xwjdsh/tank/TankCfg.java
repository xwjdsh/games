package com.github.xwjdsh.tank;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.github.xwjdsh.config.SysConfig;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 坦克的配置类，单例模式
 * @author xwjdsh
 */
public class TankCfg {

	/**
	 * 敌人坦克的起始点集
	 */
	private List<List<Point>> enemys;
	
	/**
	 * 玩家坦克的起始点
	 */
	private List<Point> playerTank;
	
	/**
	 * 老王的点
	 */
	private Point general;
	
	/**
	 * 地图上的障碍
	 */
	private List<Point> hindrances;
	
	/**
	 * 升级的基数
	 */
	private int levelScore;

	private static TankCfg tankCfg;

	/**
	 * 初始化
	 */
	private TankCfg() {
		try {
			//sax解析器
			SAXReader reader = new SAXReader();
			//取得文档对象
			Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream(SysConfig.getCfg().getCurrentGameCfg().getCfgPath()));
			//game根元素
			Element game=document.getRootElement();
			this.levelScore=Integer.parseInt(game.attributeValue("levelScore"));
			//初始化敌人的点集
			init_enemys(game.element("enemy_Points"));
			//初始话玩家的点集
			init_player(game.element("player_Points"));
			//初始化老王和保护层
			init_general(game.element("general"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 初始化老王和保护层的坐标集合
	 */
	@SuppressWarnings("unchecked")
	private void init_general(Element general) {
		//初始化老王的坐标
		this.general=new Point(Integer.parseInt(general.attributeValue("generalX"))
				,Integer.parseInt(general.attributeValue("generalY")));
		//初始化障碍方块的集合
		this.hindrances=new ArrayList<Point>();
		List<Element> general_point=general.elements();
		for(Element ele : general_point){
			this.hindrances.add(new Point(Integer.parseInt(ele.attributeValue("x"))
					,Integer.parseInt(ele.attributeValue("y"))));
		}
	}

	/**
	 * 初始化敌人的点集
	 */
	@SuppressWarnings("unchecked")
	private void init_enemys(Element enemys_points) {
		List<Element> enemy_points = enemys_points.elements();
		//存储敌人的坐标点，方便显示层
		enemys=new ArrayList<List<Point>>();
		for (Element enemy : enemy_points) {
			//构造一个新的敌人坦克的集合
			List<Point> enemyPoints = new ArrayList<Point>();
			List<Element> enemy_body = enemy.elements();
			for (Element body : enemy_body) {
				Point tmp=new Point(Integer.parseInt(body
						.attributeValue("x")), Integer.parseInt(body
						.attributeValue("y")));
				enemyPoints.add(tmp);
			}
			//添加到敌人坦克的初始位置集合
			enemys.add(enemyPoints);
		}
	}
	
	/**
	 * 初始化玩家坐标集
	 */
	@SuppressWarnings("unchecked")
	private void init_player(Element player_points){
		//玩家坐标集合
		List<Element> player=player_points.element("start_point").elements();
		playerTank=new ArrayList<Point>();
		for (Element enemy : player) {
			playerTank.add(new Point(Integer.parseInt(enemy.attributeValue("x"))
					,Integer.parseInt(enemy.attributeValue("y"))));
		}
	}

	public static TankCfg getTankCfg() {
		if (tankCfg == null) {
			tankCfg = new TankCfg();
		}
		return tankCfg;
	}

	public List<List<Point>> getEnemys() {
		return enemys;
	}

	public List<Point> getPlayerTank() {
		return playerTank;
	}

	public Point getGeneral() {
		return general;
	}

	public List<Point> getHindrances() {
		return hindrances;
	}

	public int getLevelScore() {
		return levelScore;
	}	
	
	
}
