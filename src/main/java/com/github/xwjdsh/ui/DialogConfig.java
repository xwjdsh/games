package com.github.xwjdsh.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.xwjdsh.config.SysConfig;
import com.github.xwjdsh.control.GameControl;
import com.github.xwjdsh.util.GameUtil;


/**
 * 游戏的键位设置窗口，模态对话框，继承JDialog 
 * @author xwjdsh
 */
@SuppressWarnings("serial")
public class DialogConfig extends JDialog{
	
	/**
	 * 游戏控制器对象
	 */
	private GameControl gameControl;

	/**
	 * 按键的描述信息
	 */
	private JLabel[] lables;
	
	/**
	 * 键盘的keycode文本框
	 */
	private KeyText[] keyTexts;
	
	/**
	 * 是否修改配置文件
	 */
	private JCheckBox once;
	
	/**
	 * 确定按钮
	 */
	private JButton confirm;
	
	/**
	 * 取消按钮
	 */
	private JButton cancel;
	
	/**
	 * 错误信息
	 */
	private JLabel errorLable;
	
	
	
	
	/**
	 * 构造键位模态对话框
	 * @param frame 所属的frame
	 * @param title 标题
	 * @param model 是否为模态
	 */
	public DialogConfig(Frame frame,String title,boolean model,GameControl gameControl){
		super(frame, title,model);
		this.gameControl=gameControl;
		//采用边界布局
		this.setLayout(new BorderLayout());
		this.add("Center",init_keysPanel());
		this.add("South",init_buttonPanel());
		this.setSize(300, 400);
		//居中显示
		GameUtil.middle(this);
		//大小不可变
		this.setResizable(false);
		//可见
		this.setVisible(true);
	}

	/**
	 * 获得按钮面板
	 * @return
	 */
	private JPanel init_buttonPanel(){
		//采用默认的流式布局
		JPanel buttonPanel=new JPanel();
		//初始化选择框
		once=new JCheckBox("仅本次",true);
		confirm=new JButton("确定");
		errorLable=new JLabel();
		cancel=new JButton("取消");
		buttonPanel.add(once);
		buttonPanel.add(confirm);
		buttonPanel.add(errorLable);
		buttonPanel.add(cancel);
		//初始化按钮事件
		init_button_event();
		return buttonPanel;
	}

	/**
	 * 初始化按钮事件
	 */
	private void init_button_event() {
		//取消按钮的事件
		this.cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		//确定按钮的事件
		this.confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, Integer> newDestributeKeyMap=new HashMap<String, Integer>(keyTexts.length);
				for(int i=0;i<keyTexts.length;i++){
					newDestributeKeyMap.put(lables[i].getText(), keyTexts[i].getKeycode());
				}
				gameControl.updateKey(newDestributeKeyMap);
				if(!once.isSelected()){
					//当取消勾选仅此一次时，写入到配置文件中
					gameControl.writeKeyConfig();
				}
				//关闭窗口
				dispose();
			}
		});
	}

	/**
	 * 得到键位的面板
	 */
	private JPanel init_keysPanel() {
		//得到按键描述与键位的数组
		Object[] keyDescribute= SysConfig.getCfg().getCurrentGameCfg().getKeyDestributeMap().entrySet().toArray();
		lables=new JLabel[keyDescribute.length];
		keyTexts=new KeyText[keyDescribute.length];
		final Set<Integer> keycodeSet=new HashSet<Integer>();
		JPanel keySeting=new JPanel();
		//设置布局为网格布局
		keySeting.setLayout(new GridLayout(keyDescribute.length,2,0,10));
		KeyAdapter keyAdapter=new KeyAdapter() {
			//按键按下
			@Override
			public void keyPressed(KeyEvent e) {
				((JTextField)e.getSource()).setText("");
			}
			//按键释放
			@Override
			public void keyReleased(KeyEvent e) {
				//得到对应按键的输入框
				KeyText source=((KeyText)e.getSource());
				source.setText(KeyEvent.getKeyText(e.getKeyCode()));
				source.setKeycode(e.getKeyCode());
				keycodeSet.clear();
				//检测，是否有重复，利用Set的不重复特性
				for(KeyText text : keyTexts){
					keycodeSet.add(text.getKeycode());
				}
				if(keycodeSet.size()==keyTexts.length){
					errorLable.setText("");
					confirm.setVisible(true);
				}else{
					errorLable.setForeground(Color.red);
					errorLable.setText("存在键位重复！");
					confirm.setVisible(false);
				}
			}
		};
		for(int i=0;i<keyDescribute.length;i++){
			//拆箱
			@SuppressWarnings("unchecked")
			Map.Entry<String, Integer> entry=(Map.Entry<String, Integer>)keyDescribute[i];
			lables[i]=new JLabel(entry.getKey(),JLabel.CENTER);
			//初始化显示信息
			keyTexts[i]=new KeyText(KeyEvent.getKeyText(entry.getValue()),entry.getValue());
			keyTexts[i].addKeyListener(keyAdapter);
			keySeting.add(lables[i]);
			keySeting.add(keyTexts[i]);
		}
		return keySeting;
	}
	
	/**
	 * 内部类，自定义的输入框，继承自JTextField类，主要为了在后台存储keycode
	 * @author xwjdsh
	 */
	public class KeyText extends JTextField{
		
		//存储按键的keycode
		private int keycode;
		
		public KeyText(String text,int keycode) {
			this.setText(text);
			this.keycode=keycode;
			//设置列宽为10
			this.setColumns(10);
		}

		public int getKeycode() {
			return keycode;
		}

		public void setKeycode(int keycode) {
			this.keycode = keycode;
		}
	}
}
