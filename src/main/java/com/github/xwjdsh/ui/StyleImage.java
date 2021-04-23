package com.github.xwjdsh.ui;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * 风格图片，皮肤
 * @author xwjdsh
 */
public class StyleImage {
	
	/**
	 * 皮肤的存储目录
	 */
	private static String  catalog="img/style/";
	
	
	/**
	 * 皮肤的名字
	 */
	private static String styleCatalog="style1";
	
	//边框图片
	public static Image BORDER_IMG;
	
	//背景图片
	public static Image BG_IMG;
	
	//数字图片
	public static Image NUM_IMG;
	
	//等级图片
	public static Image LEVEL_IMG;
	
	//得分图片
	public static Image GRADE_IMG;
	
	//已完成图片
	public static Image DONE_IMG;
	
	//暂停图片
	public static Image SUSPEND_IMG;
	
	//下一个图片
	public static Image NEXT_IMG;
	
	//开始按钮图片
	public static ImageIcon START_IMG;
	
	//返回按钮图片
	public static ImageIcon BACK_IMG;
	
	//设置图标图片
	public static ImageIcon SETED_IMG;
	
	public static ImageIcon SETING_IMG;
	
	//换肤图标图片
	public static ImageIcon SKINED_IMG;
	public static ImageIcon SKINING_IMG;
	
	//排行榜图标图片
	public static ImageIcon CHARTSED_IMG;
	public static ImageIcon CHARTSING_IMG;
	
	//关于图标图片
	public static ImageIcon ABOUTED_IMG;
	public static ImageIcon ABOUTING_IMG;
	
	
	static{
		try{
			changeStyle();
			SETED_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/seted.png")));
			SETING_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/setting.png")));
			
			SKINED_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/skined.png")));
			SKINING_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/skining.png")));
			
			CHARTSED_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/chartsed.png")));
			CHARTSING_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/chartsing.png")));
			
			ABOUTED_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/abouted.png")));
			ABOUTING_IMG=new ImageIcon(ImageIO.read(StyleImage.class.getClassLoader().getResourceAsStream("img/system/abouting.png")));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化图片，改变皮肤的时候调用
	 */
	public static void changeStyle(){
		try{
		ClassLoader loader=StyleImage.class.getClassLoader();
		BORDER_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/border.png"))).getImage();
		BG_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/background.png"))).getImage();
		NUM_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/number.png"))).getImage();
		LEVEL_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/level.png"))).getImage();
		GRADE_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/grade.png"))).getImage();
		DONE_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/done.png"))).getImage();
		NEXT_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/next.png"))).getImage();
		START_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/start.png")));
		BACK_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/back.png")));
		SUSPEND_IMG=new ImageIcon(ImageIO.read(loader.getResourceAsStream(catalog+styleCatalog+"/suspend.png"))).getImage();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getStyleCatalog() {
		return styleCatalog;
	}
	
	public static void setStyleCatalog(String styleCatalog) {
		StyleImage.styleCatalog = styleCatalog;
	}
	
	public static String getCatalog() {
		return catalog;
	}
	
}
