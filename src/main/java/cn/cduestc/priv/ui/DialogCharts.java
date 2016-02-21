package cn.cduestc.priv.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.cduestc.priv.config.SysConfig;
import cn.cduestc.priv.control.GameControl;
import cn.cduestc.priv.entity.Player;
import cn.cduestc.priv.util.DBUtil;
import cn.cduestc.priv.util.GameUtil;

/**
 * 显示游戏的分数排行窗口，模态对话框，继承JDialog 
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class DialogCharts extends JDialog{
	
	/**
	 * 排行的目标数据
	 */
    private List<Player> targetDataSource;
    
    /**
     * 缓存的本地记录
     */
    private List<Player> localData;
    
    /**
     * 游戏控制器对象
     */
    private GameControl gameControl;
    
    /**
     * 显示排行的面板
     */
    private JPanel showCharts;
    
    private final String sql="select name,score,done,level from record where gameId="+SysConfig.getCfg().getCurrentGameCfg().getGameId();
    
    /**
     * 排序方式选择框的索引和方法的对应
     */
    private static Map<Integer,Method> selectMap;
    
    static{
    	//初始化索引序号和方法的对应
    	selectMap=new HashMap<Integer, Method>();
    	try {
    		//通过反射取得对应的方法
    		Class<?> clazz=Player.class;
			selectMap.put(0,clazz.getMethod("getScore"));
			selectMap.put(1,clazz.getMethod("getDone"));
			selectMap.put(2,clazz.getMethod("getLevel"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    /**
     * 初始化排行数据
     * @param chartsPath 排行文件的路径
     * @param frame 父窗口
     */
	public DialogCharts(Frame frame,String title,boolean model,List<Player> players,GameControl gameControl) {
		//指定父frame，标题，模态
		super(frame,title,model);
		//取得游戏控制器对象
		this.gameControl=gameControl;
		//缓存数据
		this.localData=players;
		//改变目标引用
		this.targetDataSource=players;
		//设置为边界布局
		this.setLayout(new BorderLayout());
		this.showCharts=init_showCharts();
		this.add("Center",this.showCharts);
		this.add("South",this.init_button());
		//窗口大小
		this.setSize(450,300);
		//不可改变窗口大小
		this.setResizable(false);
		//居中显示
		GameUtil.middle(this);
		//可见
		this.setVisible(true);
	}
	
	private JPanel init_button() {
		JPanel btnPanel=new JPanel();
		btnPanel.add(this.getJLable("数据源:", Color.black));
		final JComboBox<String> dataSourceSelect=new JComboBox<String>();
		dataSourceSelect.addItem("本地记录");
		dataSourceSelect.addItem("数据库");
		dataSourceSelect.setSelectedIndex(0);
		dataSourceSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dataSourceSelect.getSelectedIndex()==1){
					targetDataSource=DBUtil.getDbUtil().executeQuery(sql);
				}else{
					targetDataSource=localData;
				}
				updataCharts();
			}
		});
		btnPanel.add(dataSourceSelect);
		btnPanel.add(this.getJLable("排序方式:",Color.black));
		final JComboBox<String> sortSelect=new JComboBox<String>();
		//添加子项
		sortSelect.addItem("分数");
		sortSelect.addItem("完成");
		sortSelect.addItem("等级");
		//默认选中第一项
		sortSelect.setSelectedIndex(0);
		sortSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeChartsSort(sortSelect.getSelectedIndex());
			}
		});
		btnPanel.add(sortSelect);
		//初始化清空按钮
		JButton clear=new JButton("清空数据");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameControl.clearCharts();
				dispose();
			}
		});
		btnPanel.add(clear);
		//初始化关闭按钮
		JButton close=new JButton("关闭");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnPanel.add(close);
		return btnPanel;
	}
	
	/**
	 * 根据索引来执行对应的事件
	 */
	private void changeChartsSort(int index){
		//为了使匿名内部类中可访问
		final int i=index;
		Collections.sort(this.targetDataSource,new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				try {
					//通过方法调用对象
					return (Integer)selectMap.get(i).invoke(p2)-(Integer)selectMap.get(i).invoke(p1);
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}); 
		updataCharts();
	}
	
	/**
	 * 更新显示面板
	 */
	private void updataCharts(){
		//将组件设置为不可见
		this.showCharts.setVisible(false);
		//删除显示信息的Panel
		this.remove(this.showCharts);
		//重新构造显示Panel
		this.showCharts=init_showCharts();
		//添加
		this.add("Center",this.showCharts);
	}

	/**
	 * 初始化排行信息
	 * return 排行信息面板
	 */
	private JPanel init_showCharts() {
		JPanel showCharts=new JPanel();
		//设置布局为网格布局，排名信息数量+1行，5列
		showCharts.setLayout(new GridLayout(targetDataSource.size()+1,5));
		showCharts.add(getJLable("排名", Color.red));
		showCharts.add(getJLable("姓名", Color.red));
		showCharts.add(getJLable("分数", Color.red));
		showCharts.add(getJLable("完成", Color.red));
		showCharts.add(getJLable("等级", Color.red));
		Color color=null;
		//初始颜色，黑色
		int rgb=0;
		//改变的基数，由排行数量决定
		int addBase=200/(this.targetDataSource.size()==0?200:this.targetDataSource.size());
		for(int i=0;i<this.targetDataSource.size();i++){
			color=new Color(rgb,rgb,rgb);
			rgb+=addBase;
			JLabel ranking=getJLable(String.valueOf(i+1),color);
			//单独设置排名的字体格式
			ranking.setFont(new Font("楷体",Font.BOLD+Font.ITALIC,25));
			showCharts.add(ranking);
			showCharts.add(getJLable(targetDataSource.get(i).getName(), color));
			//添加排行信息
			showCharts.add(getJLable(String.valueOf(targetDataSource.get(i).getScore()), color));
			showCharts.add(getJLable(String.valueOf(targetDataSource.get(i).getDone()), color));
			showCharts.add(getJLable(String.valueOf(targetDataSource.get(i).getLevel()), color));
		}
		return showCharts;
	}
	
	/**
	 * 获得一个JLable，为了使用匿名对象，所以写成方法
	 * @param title 显示内容
	 * @param color 颜色
	 * @return 构造好的JLable对象
	 */
	private JLabel getJLable(String title,Color color){
		JLabel tmp=new JLabel(title,JLabel.CENTER);
		tmp.setForeground(color);
		return tmp;
	}

	
}
