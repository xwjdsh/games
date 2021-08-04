package com.github.xwjdsh.control;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.github.xwjdsh.config.GameConfig;
import com.github.xwjdsh.config.SysConfig;
import com.github.xwjdsh.dto.GameDto;
import com.github.xwjdsh.entity.Player;
import com.github.xwjdsh.logic.GameLogic;
import com.github.xwjdsh.ui.DialogAbout;
import com.github.xwjdsh.ui.DialogCharts;
import com.github.xwjdsh.ui.DialogConfig;
import com.github.xwjdsh.ui.DialogSkin;
import com.github.xwjdsh.ui.GameFrame;
import com.github.xwjdsh.ui.GameImage;
import com.github.xwjdsh.ui.GamePanel;
import com.github.xwjdsh.ui.StyleImage;
import com.github.xwjdsh.util.DBUtil;

/**
 * 游戏的总控制器，控制界面层和逻辑层和配置层，是一个枢纽
 * @author xwjdsh
 */
public class GameControl implements Runnable {

	/**
	 * 控制游戏逻辑层
	 */
	private GameLogic gameLogic;

	/**
	 * 控制界面层
	 */
	private GamePanel gamePanel;

	/**
	 * 游戏数据
	 */
	private GameDto gameDto;

	/**
	 * 游戏线程
	 */
	private Thread gameThread;

	/**
	 * 对所选游戏的相关配置
	 */
	private GameConfig currentGameConfig;

	/**
	 * 键位的配置
	 */
	private Map<Integer, Method> key_method;

	/**
	 * 游戏逻辑对象缓存
	 */
	private static HashMap<Integer, GameLogic> gameLogicCache = new HashMap<Integer, GameLogic>();

	/**
	 * 游戏数据对象缓存
	 */
	private static HashMap<Integer, GameDto> gameDtoCache = new HashMap<Integer, GameDto>();

	public GameControl() {
		this.gamePanel = new GamePanel(this);
		new GameFrame(gamePanel);
	}

	public GameDto getGameDto() {
		return gameDto;
	}

	/**
	 * 分派的键盘监听事件
	 */
	public void disposeKey(int keyCode) {
		if (this.gameLogic == null) {
			// 如果gameLogic为空并且按键在游戏选项的范围
			if (keyCode > 48&& keyCode <= 48 + SysConfig.getCfg().getGameMap().size()) {
				// 把选中的游戏配置加入到配置文件对象中
				SysConfig.getCfg().setCurrentGameCfg(
						SysConfig.getCfg().getGameMap().get(keyCode % 48));
				// 改变当前选中游戏配置文件对象的引用
				this.currentGameConfig = SysConfig.getCfg().getCurrentGameCfg();
				// 初始化键位
				this.currentGameConfig.init_keys();
				// 如果缓存中有就直接从缓存中取，否则反射创建对象
				if (gameLogicCache.containsKey(keyCode)) {
					this.gameLogic = gameLogicCache.get(keyCode);
					this.gameDto = gameDtoCache.get(keyCode);
				} else {
					// 初始化当前游戏的配置文件对象
					init_game(keyCode);
				}
				//初始化游戏素材
				GameImage.init_gameImage(this.currentGameConfig.getMaterialPath());
				// 为游戏面板中的小窗口添加数据对象
				this.gamePanel.addGameDto(this.gameDto);
				// 初始化键位
				init_ksys();
				// 改变按钮为可用
				this.gamePanel.changeBtnStartEnable(true);
				this.gamePanel.changeBtnBackEnable(true);
				this.gamePanel.changeBtnSetingEnable(true);
				this.gamePanel.changeBtnChartsEnable(true);
			}
		} else if (this.gameDto.isStart() && key_method.containsKey(keyCode)) {
			// 如果游戏开始并且配置文件中包含keycode
			try {
				// 通过反射调用相应方法
				key_method.get(keyCode).invoke(this.gameLogic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 重新绘制游戏面板
		this.gamePanel.repaint();
	}

	/**
	 * 读取配置文件，通过反射创建对象,完成键位配置等
	 */
	private void init_game(int keyCode) {
		try {
			// 取得dto类名
			String gameDtoClass = this.currentGameConfig.getDtoClassName();
			// 获得类对象
			Class<?> clazzDto = Class.forName(gameDtoClass);
			// 得到构造器对象
			Constructor<?> consDto = clazzDto.getConstructor();
			// 装入游戏数据对象缓存
			gameDtoCache.put(keyCode, (GameDto)consDto.newInstance());
			// 创建游戏数据对象
			this.gameDto = gameDtoCache.get(keyCode);
			// 取得logic类名
			String gameLogicClass = this.currentGameConfig.getLogicClassName();
			// 获得逻辑类对象
			Class<?> clazzLogic = Class.forName(gameLogicClass);
			// 取得逻辑类的构造器对象
			Constructor<?> consLogic = clazzLogic.getConstructor();
			// 创建游戏逻辑对象
			this.gameLogic = (GameLogic) consLogic.newInstance();
			// 装入游戏逻辑缓存
			gameLogicCache.put(keyCode, this.gameLogic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化键位的配置
	 */
	public void init_ksys() {
		this.key_method = new HashMap<Integer, Method>();
		// 取得映射集
		Set<Map.Entry<String, Integer>> set = this.currentGameConfig.getMethodKeyMap().entrySet();
		// 取得映射集的迭代
		Iterator<Map.Entry<String, Integer>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			try {
				// 根据方法名反射获得方法对象
				this.key_method.put(entry.getValue(), this.gameLogic.getClass().getMethod(entry.getKey()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 游戏开始
	 */
	public void start() {
		// 改变游戏状态
		this.gameDto.setStart(true);
		// 为游戏逻辑对象添加数据对象
		this.gameLogic.setGameDto(this.gameDto);
		// 创建游戏线程
		this.gameThread = new Thread(this);
		// 启动游戏线程
		gameThread.start();
		// 改变按钮状态
		this.gamePanel.changeBtnStartEnable(false);
	}

	/**
	 * 游戏返回
	 */
	public void back() {
		// 清除逻辑
		this.gameLogic = null;
		//清除游戏配置
		this.currentGameConfig=null;
		//设置游戏为返回状态
		this.gameDto.setBack(true);
		// 清除数据，同时结束线程结束线程
		this.gameDto.clearAll();
		// 改变小窗口的null引用，绘制选择项
		this.gamePanel.addGameDto(null);
		//改变按钮可用性
		this.gamePanel.changeBtnStartEnable(false);
		this.gamePanel.changeBtnBackEnable(false);
		this.gamePanel.changeBtnSetingEnable(false);
		this.gamePanel.changeBtnChartsEnable(false);
		// 重绘
		gamePanel.repaint();
	}
	
	/**
	 * 游戏排行
	 */
	public void charts(){
		if(this.currentGameConfig.getPlayers()==null){
			//当没有排行数据时，弹出提示框
			JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this.gamePanel), "当前没有排行数据.","提示",JOptionPane.WARNING_MESSAGE);
		}else{
			//弹出排行信息窗口
			new DialogCharts(JOptionPane.getFrameForComponent(this.gamePanel), "当前排行", true,this.currentGameConfig.getPlayers(),this);
		}
	}
	
	/**
	 * 清除排行数据
	 */
	public void clearCharts() {
		this.currentGameConfig.clearCharts();
	}

	/**
	 * 游戏设置
	 */
	public void setting() {
		//按钮可用，并且游戏未开始或者暂停状态
		if(this.gamePanel.getSetting().isEnabled()&&(!this.gameDto.isStart()||this.gameDto.isSuspend()))
			//弹出设置窗口
			new DialogConfig(JOptionPane.getFrameForComponent(this.gamePanel),"键位设置",true,this);
	}
	
	/**
	 * 更新游戏键位
	 */
	public void updateKey(Map<String, Integer> newDestributeKeyMap){
		//更新配置的键位
		this.currentGameConfig.updateKey(newDestributeKeyMap);
		//更新方法的反射
		init_ksys();
	}
	
	/**
	 * 修改配置文件
	 */
	public void writeKeyConfig(){
		this.currentGameConfig.writeKeyConfig();
	}
	
	/**
	 * 换肤
	 */
	public void skin(){
		//按钮可用，并且游戏未开始或者暂停状态
		if(this.gameDto==null||(this.gamePanel.getSkin().isEnabled()&&(!this.gameDto.isStart()||this.gameDto.isSuspend()))){
			//弹出设置窗口
			new DialogSkin(JOptionPane.getFrameForComponent(this.gamePanel),"更换皮肤",true,this);
		}
	}

	/**
	 * 关于
	 */
	public void about(){
		new DialogAbout(JOptionPane.getFrameForComponent(this.gamePanel),"关于作者",true);
	}
	
	/**
	 * 游戏结束
	 */
	public void gameOver(){
		this.gamePanel.repaint();
		if(!this.gameDto.isBack()){
			//取得输入的玩家名字
			String name=(JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(this.gamePanel),"注:为空存为'匿名',长度取前10位.","请输入姓名",JOptionPane.HEIGHT));
			//非空情况下去空格
			name=name==null?null:name.trim();
			//做相应判断
			name=(name==null||name.equals(""))?"匿名":(name.length()<=10?name:name.substring(0, 10));
			//更新排行对象并刷新存储文件
			this.currentGameConfig.updateCharts(new Player(name, this.gameDto.getScore(), this.gameDto.getAlreadyDone(),this.gameDto.getLevel()));
			//更新数据库记录
			DBUtil.getDbUtil().executeUpdate("insert into record(name,score,done,level,gameId) values('"+name+"',"+this.gameDto.getScore()+","+
			this.gameDto.getAlreadyDone()+","+this.gameDto.getLevel()+","+currentGameConfig.getGameId()+")");;
			//清除游戏数据
			this.gameDto.clearAll();
			//改变按钮可见性
			this.gamePanel.changeBtnStartEnable(true);
			//重绘
			this.gamePanel.repaint();
		}
		this.gameDto.setBack(false);
	}
	
	
	/**
	 * 游戏线程
	 */
	@Override
	public void run() {
		while (true) {
			try {
				// 游戏线程的暂停时间
				long speed = this.gameDto.getThreadSleepTime();
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 如果游戏结束，结束线程
			if (!gameDto.isStart()) {
				this.gameOver();
				break;
			}
			
			// 如果游戏暂停，继续循环
			if (gameDto.isSuspend()) {
				continue;
			}
			// 调用逻辑层的方法,非空的情况下，避免空针异常
			if (gameLogic != null)
				gameLogic.disposeGameThread();
			//重绘
			gamePanel.repaint();
		}
	}

	/**
	 * 改变风格
	 * @param styleCatalog 要改变的风格目录名称
	 */
	public void changeStyle(String styleCatalog) {
		//改变皮肤的目录
		StyleImage.setStyleCatalog(styleCatalog);
		//重新初始化
		StyleImage.changeStyle();
		//改变按钮的显示
		this.gamePanel.changeStyle();
		//重绘
		this.gamePanel.repaint();
	}
}
