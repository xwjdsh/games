package com.github.xwjdsh.ui;

import java.awt.Image;

import com.github.xwjdsh.util.GameUtil;


public class GameImage {
	


	/**
	 * 用来表示地图的图片
	 */
	public static Image MAP_IMG;
	
	/**
	 * 用来表示活动点的图片
	 */
	public static Image ACTION_IMG;
	
	/**
	 * 表示扩展点1的图片
	 */
	public static Image EXPAND_POINT1_IMG;
	
	/**
	 * 表示扩展点2的图片
	 */
	public static Image EXPAND_POINT2_IMG;
	
	/**
	 * 扩充点集1的图片
	 */
	public static Image EXPAND_LISTPOINT1_IMG;
	
	/**
	 * 扩充点集2的图片
	 */
	public static Image EXPAND_LISTPOINT2_IMG;
	
	/**
	 * 扩充点集3的图片
	 */
	public static Image EXPAND_LISTPOINT3_IMG;
	
	/**
	 * 扩充点集4的图片
	 */
	public static Image EXPAND_LISTPOINT4_IMG;
	
	/**
	 * 扩充显示的点集
	 */
	public static Image EXPAND_LISTSHOW1;
	
	/**
	 * 游戏结束时的图片
	 */
	public static Image DEATH_IMG;
	
	/**
	 * 静态初始化方法
	 * @param material 文件夹的路径
	 */
	public static void init_gameImage(String material){
		GameUtil.installImage(GameImage.class, material);
	}
}
