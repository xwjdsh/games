package com.github.xwjdsh.ui;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.github.xwjdsh.util.GameUtil;

/**
 * 关于作者弹出框
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class DialogAbout extends JDialog{
	/**
	 * 作者图片
	 */
	private JLabel author;
	
	/**
	 * sw
	 */
	private JLabel sw;
	
	{
		try{
			author=new JLabel(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("img/system/author.png"))));
			sw=new JLabel(new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("img/system/sw.png"))));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 描述信息
	 */
	private JLabel discribe;
	
	
	public DialogAbout(Frame frame,String title,boolean model) {
		//指定父frame，标题，模态
		super(frame,title,model);
		//设置为边界布局
		this.setLayout(new FlowLayout());
		this.add(author);
		this.add(sw);
		//初始化描述信息
		this.add(init_describe());
		//窗口大小
		this.setSize(400,350);
		//不可改变窗口大小
		this.setResizable(false);
		//居中显示
		GameUtil.middle(this);
		//可见
		this.setVisible(true);
	}


	private JLabel init_describe() {
		//以html的方式实现样式和换行
		discribe=new JLabel("<html><body><br/><br/><br/><font color='red'>"
				+ "<br/><br/<br/>始于2014年11月初，完成于2014年12月底."
				+ "</font<br/></body></html>");
		return discribe;
	}
}
