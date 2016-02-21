package cn.cduestc.priv.ui;

import javax.swing.JFrame;

import cn.cduestc.priv.config.SysConfig;
import cn.cduestc.priv.util.GameUtil;

/**
 * 继承自JFrame类，设置属性，作为游戏窗口的对象
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame{

	public GameFrame(GamePanel gamePanel){
		//设置游戏标题
		this.setTitle(SysConfig.getCfg().getTitle());
		//设置默认的程序关闭属性（程序结束）
		this.setDefaultCloseOperation(3);
		//设置游戏窗口的大小
		this.setSize(SysConfig.getCfg().getWidth(),SysConfig.getCfg().getHeight());
		//设置为不允许用户改变窗口大小
		this.setResizable(false);
		//设置没有标题栏
		//this.setUndecorated(true);
		//居中显示
		GameUtil.middle(this);
		//设置默认的Panel
		this.setContentPane(gamePanel);
		//设置显示
		this.setVisible(true);
	}
}
