package com.github.xwjdsh.tetris;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.github.xwjdsh.config.SysConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * 方块的配置对象
 * @author xwjdsh
 */
public class TetrisCfg {
	
	/**
	 * 方块配置集合对象
	 */
	private List<List<Point>> square_list;
	
	/**
	 * 方块的升级分数
	 */
	private int levelScore;
	
	/**
	 * 单例方块对象
	 */
	private static TetrisCfg tetrisCfg;
	
	
	private TetrisCfg(){
		init();
	}
	
	//初始化方块配置对象
	private void init(){
		try {
			SAXReader reader=new SAXReader();
			Document document=reader.read(this.getClass().getClassLoader().getResourceAsStream(SysConfig.getCfg().getCurrentGameCfg().getCfgPath()));
			Element game=document.getRootElement();
			this.levelScore=Integer.parseInt(game.attributeValue("levelScore"));
			init_squares(game.element("squares"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化方块的配置
	 * @param squares
	 */
	@SuppressWarnings("unchecked")
	private void init_squares(Element squares){
		List<Element> tmp_squares=squares.elements();
		//初始化square_list
		square_list=new ArrayList<List<Point>>(tmp_squares.size());
		for(Element square : tmp_squares){
			List<Element> points=square.elements();
			List<Point> point_list=new ArrayList<Point>(points.size());
			for(Element point : points){
				point_list.add(new Point(Integer.parseInt(
						point.attributeValue("x")),
						Integer.parseInt(point.attributeValue("y"))));
			}
			square_list.add(point_list);
		}
	}
	
	public static TetrisCfg getTetrisCfg(){
		if(tetrisCfg==null){
			tetrisCfg=new TetrisCfg();
		}
		return tetrisCfg;
	}

	public List<List<Point>> getSquare_list() {
		return square_list;
	}

	public int getLevelScore() {
		return levelScore;
	}
	
}
