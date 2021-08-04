package com.github.xwjdsh.snack;

import com.github.xwjdsh.config.SysConfig;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 蛇的配置对象，单例
 * @author xwjdsh
 */
public class SnackCfg {
	/**
	 * 蛇的初始长度
	 */
	private int startLength;
	
	/**
	 * 蛇的升级分数
	 */
	private int levelScore;
	
	/**
	 * 地图的障碍数
	 */
	private int stoneCount;
	
	/**
	 *单例对象
	 */
	private static SnackCfg snackCfg;
	
	
	
	/**
	 * 解析数据
	 */
	private SnackCfg(){
		try {
			SAXReader reader=new SAXReader();
			Document document=reader.read(this.getClass().getClassLoader().getResourceAsStream(SysConfig.getCfg().getCurrentGameCfg().getCfgPath()));
			Element game=document.getRootElement();
			this.startLength=Integer.parseInt(game.attributeValue("startLength"));
			this.levelScore=Integer.parseInt(game.attributeValue("levelScore"));
			this.stoneCount=Integer.parseInt(game.attributeValue("stoneCount"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getStartLength() {
		return startLength;
	}

	public int getLevelScore() {
		return levelScore;
	}

	public static SnackCfg getSnackCfg() {
		if(snackCfg==null)
			snackCfg=new SnackCfg();
		return snackCfg;
	}

	public int getStoneCount() {
		return stoneCount;
	}
	
}
