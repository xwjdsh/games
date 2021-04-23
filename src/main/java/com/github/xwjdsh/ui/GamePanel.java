package com.github.xwjdsh.ui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.xwjdsh.config.SysConfig;
import com.github.xwjdsh.config.WindowConfig;
import com.github.xwjdsh.control.GameControl;
import com.github.xwjdsh.control.PlayerControl;
import com.github.xwjdsh.dto.GameDto;

/**
 * 继承自JPanel类，显示游戏面板。添加到GameFrame对象上
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	/**
	 * 在面板上显示的小窗口的集合
	 */
	private Window[] multi_window;

	/**
	 * 引用配置文件对象
	 */
	private SysConfig cfg = SysConfig.getCfg();
	
	/**
	 * 游戏控制器对象
	 */
	private GameControl gameControl;
	
	/**
	 * 开始按钮
	 */
	private JButton start;

	/**
	 * 返回按钮
	 */
	private JButton back;
	
	/**
	 * 存档按钮
	 */
	private JButton save;
	
	/**
	 * 排行按钮
	 */
	private JButton charts;
	
	/**
	 * 设置按钮
	 */
	private JButton setting;
	
	/**
	 * 换肤按钮
	 */
	private JButton skin;
	
	/**
	 * 关于按钮
	 */
	private JButton about;
	
	/**
	 * 按钮与方法的映射
	 */
	private static Map<JButton,Method> eventMap;
	
	/**
	 * 按钮与图片的映射
	 */
	private static Map<JButton,ImageIcon[]> imageIconMap;
	
	
	public GamePanel(GameControl gameControl) {
		//取得游戏控制器引用
		this.gameControl=gameControl;
		//增加键盘监听
		this.addKeyListener(new PlayerControl(gameControl));
		// 初始化小窗口
		init_multi_window();
		//初始化按钮
		init_button();
	}

	/**
	 * 从配置文件中获取关于窗口的属性，初始化
	 */
	private void init_multi_window() {
		WindowConfig[] windowCfg_list = cfg.getWindow_list();
		multi_window = new Window[windowCfg_list.length];
		for (int i = 0; i < windowCfg_list.length; i++) {
			try {
				Class<?> clazz = Class.forName(windowCfg_list[i].getClassNmae());
				Constructor<?> constructor = clazz.getConstructor(int.class,
						int.class, int.class, int.class);
				Window window = (Window)constructor.newInstance(
						windowCfg_list[i].getStartX(), windowCfg_list[i].getStartY(),
						windowCfg_list[i].getWidth(), windowCfg_list[i].getHeight());
				multi_window[i] = window;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 初始化按钮
	 */
	private void init_button(){
		//设置布局管理器为null，绝对布局方式，否则setBouns方法出错
		this.setLayout(null);
		this.start=new JButton(StyleImage.START_IMG);
		//初始化开始按钮
		this.start.setBounds(cfg.getStartBtn().getX(),cfg.getStartBtn().getY(),
						cfg.getStartBtn().getW(),cfg.getStartBtn().getH());
		//设置不显示焦点痕迹
		this.start.setFocusPainted(false);
		this.add(start);
		//初始化返回按钮
		this.back=new JButton(StyleImage.BACK_IMG);
		this.back.setBounds(cfg.getBackBtn().getX(),cfg.getBackBtn().getY(),
					   cfg.getBackBtn().getW(),cfg.getBackBtn().getH());
		this.back.setFocusPainted(false);
		this.add(back);
		//初始化排行按钮
		this.charts=new JButton(StyleImage.CHARTSED_IMG);
		
		this.charts.setBounds(cfg.getCharts().getX(), cfg.getCharts().getY(),
				cfg.getCharts().getW(), cfg.getCharts().getH());
		//使排行按钮透明
		this.transparentBtn(this.charts);
		this.add(this.charts);
		//初始化设置按钮
		this.setting=new JButton(StyleImage.SETED_IMG);
		this.setting.setBounds(cfg.getSetingBtn().getX(), cfg.getSetingBtn().getY(),
						cfg.getSetingBtn().getW(), cfg.getSetingBtn().getH());
		//使设置按钮透明
		this.transparentBtn(this.setting);
		this.add(setting);
		this.skin=new JButton(StyleImage.SKINED_IMG);
		this.skin.setBounds(cfg.getSkinBtn().getX(), cfg.getSkinBtn().getY(), 
						cfg.getSkinBtn().getW(), cfg.getSkinBtn().getH());
		//使换肤按钮透明
		this.transparentBtn(this.skin);
		this.add(this.skin);
		//初始化关于按钮
		this.about=new JButton(StyleImage.ABOUTED_IMG);
		this.about.setBounds(cfg.getAboutBtn().getX(), cfg.getAboutBtn().getY(),
				cfg.getAboutBtn().getW(), cfg.getAboutBtn().getH());
		this.transparentBtn(this.about);
		this.add(about);
		//初始为不可用
		this.start.setEnabled(false);
		this.back.setEnabled(false);
		this.setting.setEnabled(false);
		this.charts.setEnabled(false);
		//初始化按钮与方法映射关系
		init_event_image_Map();
		//增加监听事件
		init_button_action();
	}
	
	/**
	 * 初始化按钮与控制器方法的映射
	 */
	private void init_event_image_Map(){
		eventMap=new HashMap<JButton, Method>();
		imageIconMap=new HashMap<JButton, ImageIcon[]>();
		try {
			//通过反射来调用方法，实现共同的事件监听
			Class<?> clazz=this.gameControl.getClass();
			eventMap.put(this.back, clazz.getMethod("back"));
			eventMap.put(this.start, clazz.getMethod("start"));
			eventMap.put(this.setting, clazz.getMethod("setting"));
			imageIconMap.put(this.setting, new ImageIcon[]{StyleImage.SETED_IMG,StyleImage.SETING_IMG});
			eventMap.put(this.charts, clazz.getMethod("charts"));
			imageIconMap.put(this.charts, new ImageIcon[]{StyleImage.CHARTSED_IMG,StyleImage.CHARTSING_IMG});
			eventMap.put(this.skin,clazz.getMethod("skin"));
			imageIconMap.put(this.skin, new ImageIcon[]{StyleImage.SKINED_IMG,StyleImage.SKINING_IMG});
			eventMap.put(this.about, clazz.getMethod("about"));
			imageIconMap.put(this.about, new ImageIcon[]{StyleImage.ABOUTED_IMG,StyleImage.ABOUTING_IMG});
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 为按钮统一增加监听事件
	 */
	private void init_button_action(){
		MouseAdapter action_event=new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					JButton source=(JButton)e.getSource();
					//在java中，按键被禁用也可以触发事件，与c#不同，所以需要在执行前增加一次判断
					if(source.isEnabled()){
						eventMap.get(source).invoke(gameControl);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
			public void mouseEntered(MouseEvent e) {
				JButton source=(JButton)e.getSource();
				if(imageIconMap.get(source)!=null){
					source.setIcon(imageIconMap.get(source)[1]);
					//改变鼠标样式为手形
					source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
			public void mouseExited(MouseEvent e) {
				JButton source=(JButton)e.getSource();
				if(imageIconMap.get(source)!=null){
					source.setIcon(imageIconMap.get(source)[0]);
					//恢复鼠标样式为默认
					source.setCursor(Cursor.getDefaultCursor());
				}
			}
		};
		//开始按钮事件
		this.start.addMouseListener(action_event);
		//返回按钮事件
		this.back.addMouseListener(action_event);
		//排行按钮事件
		this.charts.addMouseListener(action_event);
		//设置按钮事件
		this.setting.addMouseListener(action_event);
		//设置按钮事件
		this.skin.addMouseListener(action_event);
		//关于按钮事件
		this.about.addMouseListener(action_event);
	}
	
	/**
	 * 改变按钮图标的风格
	 */
	public void changeStyle() {
		this.start.setIcon(StyleImage.START_IMG);
		this.back.setIcon(StyleImage.BACK_IMG);
	}
	
	/**
	 * 图片透明按钮
	 * @param btn 按钮对象
	 */
	private void transparentBtn(JButton btn){
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
	}
	
	/**
	 * 将dto添加到小窗口集合上
	 */
	public void addGameDto(GameDto gameDto){
		for(int i=0;i<multi_window.length;i++){
			multi_window[i].setGameDto(gameDto);
		}
	}

	/**
	 * 绘图方法
	 * 补充：paint和paintComponent，paint会依次调用paintComponent，paintBorder
	 * paintChildren三个方法，swing中一般不需要后两个方法
	 */
	@Override
	protected void paintComponent(Graphics g) {
		//调用父类方法，避免错误
		super.paintComponent(g);
		//循环调用每一个小窗口的paint方法
		for (int i = 0; i < multi_window.length; multi_window[i++].paint(g));
		//使得当前的游戏面板获得焦点
		this.requestFocus();
	}
	
	public void changeBtnStartEnable(boolean enable){
		this.start.setEnabled(enable);
	}
	
	public void changeBtnBackEnable(boolean enable) {
		this.back.setEnabled(enable);
	}

	public void changeBtnSetingEnable(boolean enable) {
		this.setting.setEnabled(enable);
	}
	
	public void changeBtnChartsEnable(boolean enable) {
		this.charts.setEnabled(enable);
	}
	
	public JButton getStart() {
		return start;
	}

	public JButton getSave() {
		return save;
	}

	public JButton getSetting() {
		return setting;
	}

	public JButton getSkin() {
		return skin;
	}
}
