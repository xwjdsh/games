package com.github.xwjdsh.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.xwjdsh.control.GameControl;
import com.github.xwjdsh.util.GameUtil;

/**
 * 换肤的弹出框
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class DialogSkin extends JDialog {
	
	/**
	 * 风格的存储目录
	 */
	private static String catalog=StyleImage.getCatalog();
	
	/**
	 * 显示的预览图名字
	 */
	private static String view="/view.png";
	
	/**
	 * 皮肤目录的缓存
	 */
	private static List<String> styles;
	
	/**
	 * 下拉选择风格框
	 */
	private JComboBox<String> selectStyleCobox;
	
	/**
	 * 显示风格预览图
	 */
	private JLabel showStyle;
	
	/**
	 * 游戏控制器
	 */
	private GameControl gameControl;
	
	/**
	 * 确定按钮
	 */
	private JButton confirm;
	
	
	public DialogSkin(Frame frame,String title,boolean model,GameControl gameControl) {
		super(frame, title,model);
		this.gameControl=gameControl;
		//采用边界布局
		this.setLayout(new BorderLayout());
		this.add("South",init_selectPanel());
		this.add("Center",init_stylePanel());
		//设置大小
		this.setSize(350,490);
		//居中显示
		GameUtil.middle(this);
		//大小不可变
		this.setResizable(false);
		//可见
		this.setVisible(true);
	}
	
	/**
	 * 初始化选择面板
	 */
	private JPanel init_selectPanel() {
		JPanel selectPanel=new JPanel();
		this.selectStyleCobox=new JComboBox<String>();
		addItem();
		confirm=new JButton("确定");
		selectPanel.add(selectStyleCobox);
		this.selectStyleCobox.addActionListener(new ActionListener() {
			//增加监听事件
			@Override
			public void actionPerformed(ActionEvent e) {
				//改变预览图
				showStyle.setIcon(new ImageIcon(catalog+selectStyleCobox.getSelectedItem()+view));
			}
		});
		selectPanel.add(confirm);
		confirm.addActionListener(new ActionListener() {
			//增加监听事件
			@Override
			public void actionPerformed(ActionEvent e) {
				//调用控制器改变风格
				gameControl.changeStyle((String)selectStyleCobox.getSelectedItem());
				//关闭
				dispose();
			}
		});
		return selectPanel;
	}
	
	/**
	 * 给选择框增加可选项，从缓存中取
	 */
	private void addItem() {
		if(styles==null){
			styles=new ArrayList<String>();
			//从文件中解析
			for(File cata : new File(catalog).listFiles()){
				if(cata.isDirectory()){
					styles.add(cata.getName());
				}
			}
		}
		for(String cata : styles){
			this.selectStyleCobox.addItem(cata);
		}
	}

	/**
	 * 初始化预览面板
	 */
	private JPanel init_stylePanel() {
		JPanel stylePanel=new JPanel();
		//显示预览图
		showStyle=new JLabel(new ImageIcon(catalog+this.selectStyleCobox.getSelectedItem()+view));
		//设置大小和边界
		showStyle.setBounds(0, 0, 332, 418);
		stylePanel.add(showStyle);
		return stylePanel;
	}


}
