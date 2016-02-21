package cn.cduestc.priv.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 整个项目的配置中心，存储ui和game的配置，单例模式
 * @author xwjdsh
 */
public class SysConfig {
	
	/**
	 * 单例对象
	 */
	private static SysConfig cfg=new SysConfig();
	
	/**
	 * 配置文件的路径
	 */
	private final static String FilePath="config/gameCfg.xml";
	
	
	/**
	 * 窗口标题
	 */
	private String title;
	
	/**
	 * 窗口宽度
	 */
	private int width;
	
	/**
	 * 窗口的高度
	 */
	private int height;
	
	/**
	 * 游戏的边框宽度
	 */
	private int borderSize;
	
	/**
	 * 显示中一个格子的宽高
	 */
	private int cellSize;
	
	/**
	 * 游戏地图相关
	 */
	private int maxWidth;
	private int minWidth;
	private int maxHeight;
	private int minHeight;
	
	/**
	 * 游戏的字体拔高
	 */
	private int panding;
	
	/**
	 * 小窗口的配置存储数组
	 */
	private  WindowConfig[] window_list;
	
	/**
	 * 游戏的选项配置
	 */
	private HashMap<Integer, GameConfig> gameMap;
	
	/**
	 * 当前的游戏配置
	 */
	private GameConfig currentGameCfg;
	
	/**
	 * 开始按钮的配置
	 */
	private ButtonCfg startBtn;
	
	/**
	 * 返回按钮的配置
	 */
	private ButtonCfg backBtn;
	
	/**
	 * 排行按钮的配置 
	 */
	private ButtonCfg charts;
	
	/**
	 * 设置按钮的配置
	 */
	private ButtonCfg setingBtn;
	
	/**
	 * 换肤按钮的配置
	 */
	private ButtonCfg skinBtn;
	
	/**
	 * 关于按钮的配置
	 */
	private ButtonCfg aboutBtn;
	
	/**
	 * 默认记录的排行数
	 */
	private int chartsNum;
	
	/**
	 * 数据库用户名
	 */
	private String username;
	
	/**
	 * 数据库密码
	 */
	private String password;
	
	/**
	 * 数据库连接url
	 */
	private String url;
	
	/**
	 * 允许的图片后缀名
	 */
	private String[] suffixs;
	
	
	private SysConfig(){
		try {
			//取得sax解析器
			SAXReader saxReader=new SAXReader();
			//获得文档对象
			Document document=saxReader.read(this.getClass().getClassLoader().getResourceAsStream(FilePath));
			//获得根节点
			Element root=document.getRootElement();
			//初始化ui
			init_ui(root.element("ui"));
			//初始化游戏选项
			init_game(root.element("games"));
			//初始化数据库选项
			init_db(root.element("db"));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	private void init_db(Element db) {
		this.username=db.attributeValue("username");
		this.password=db.attributeValue("password");
		this.url=db.attributeValue("url");
	}


	/**
	 * 初始化游戏选项
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private void init_game(Element element) {
		//读取游戏排行的存储上限
		this.chartsNum=Integer.parseInt(element.attributeValue("chartsNum"));
		//读取并解析游戏的配置元素
		List<Element> games=element.elements();
		//初始化游戏的映射
		gameMap=new HashMap<Integer, GameConfig>(games.size());
		for(int i=0;i<games.size();i++){
			int gameId=Integer.parseInt(games.get(i).attributeValue("gameId"));
			String gameName=games.get(i).attributeValue("gameName");
			String logicClassName=games.get(i).attributeValue("logicClassName");
			String cfgPath=games.get(i).attributeValue("cfgPath");
			String materialPath=games.get(i).attributeValue("materialPath");
			String dtoClassName=games.get(i).attributeValue("dtoClassName");
			String chartsPath=games.get(i).attributeValue("chartsPath");
			gameMap.put(i+1, new GameConfig(gameId,gameName, logicClassName, cfgPath, materialPath,dtoClassName,chartsPath,this.chartsNum));
		}
	}

	
	/**
	 * 初始化界面配置
	 * @param ui
	 */
	@SuppressWarnings("unchecked")
	private void init_ui(Element ui){
		//标题
		this.title=ui.attributeValue("title");
		//窗口高度
		this.height=Integer.parseInt(ui.attributeValue("height"));
		//窗口宽度
		this.width=Integer.parseInt(ui.attributeValue("width"));
		//边框宽度
		this.borderSize=Integer.parseInt(ui.attributeValue("borderSize"));
		//取得字体拔高
		this.panding=Integer.parseInt(ui.attributeValue("panding"));
		//取得一个格子的宽高
		this.cellSize=Integer.parseInt(ui.attributeValue("cellSize"));
		//取得地图元素
		Element gamemap=ui.element("gamemap");
		//地图大小相关
		this.minWidth=Integer.parseInt(gamemap.attributeValue("minWidth"));
		this.maxWidth=Integer.parseInt(gamemap.attributeValue("maxWidth"));
		this.minHeight=Integer.parseInt(gamemap.attributeValue("minHeight"));
		this.maxHeight=Integer.parseInt(gamemap.attributeValue("maxHeight"));
		//取得文件后缀集合
		List<Element> suffixsEle=ui.element("suffixs").elements();
		//初始化文件后缀集
		this.suffixs=new String[suffixsEle.size()];
		for(int i=0;i<suffixsEle.size();i++){
			//赋值
			this.suffixs[i]=suffixsEle.get(i).getText();
		}
		//小窗口配置
		List<Element> list=ui.element("list_window").elements();
		this.window_list=new WindowConfig[list.size()];
		for(int i=0;i<list.size();i++){
			String classNmae=list.get(i).attributeValue("className");
			int x=Integer.parseInt(list.get(i).attributeValue("x"));
			int y=Integer.parseInt(list.get(i).attributeValue("y"));
			int w=Integer.parseInt(list.get(i).attributeValue("w"));
			int h=Integer.parseInt(list.get(i).attributeValue("h"));
			window_list[i]=new WindowConfig(classNmae, x, y, w, h);
		}
		//初始化按钮的配置
		Element buttons=ui.element("buttons");
		this.startBtn=init_button(buttons.element("start"));
		this.backBtn=init_button(buttons.element("back"));
		this.setingBtn=init_button(buttons.element("seting"));
		this.skinBtn=init_button(buttons.element("skin"));
		this.charts=init_button(buttons.element("charts"));
		this.aboutBtn=init_button(buttons.element("about"));
	}
	
	/**
	 * 初始化按钮的配置
	 * @param ele 传入的带解析的元素
	 * @return
	 */
	private ButtonCfg init_button(Element ele){
		int x=Integer.parseInt(ele.attributeValue("x"));
		int y=Integer.parseInt(ele.attributeValue("y"));
		int w=Integer.parseInt(ele.attributeValue("w"));
		int h=Integer.parseInt(ele.attributeValue("h"));
		ButtonCfg buttonCfg=new ButtonCfg(x, y, w, h);
		return buttonCfg;
	}

	//传递所选游戏的配置
	public void setCurrentGameCfg(GameConfig currentGameCfg) {
		this.currentGameCfg = currentGameCfg;
	}
	
	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public WindowConfig[] getWindow_list() {
		return window_list;
	}
	public static SysConfig getCfg(){
		return cfg;
	}

	public HashMap<Integer, GameConfig> getGameMap() {
		return gameMap;
	}

	public int getBorderSize() {
		return borderSize;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMinHeight() {
		return minHeight;
	}


	public GameConfig getCurrentGameCfg() {
		return currentGameCfg;
	}



	public ButtonCfg getStartBtn() {
		return startBtn;
	}


	public ButtonCfg getBackBtn() {
		return backBtn;
	}
	
	public ButtonCfg getSetingBtn() {
		return setingBtn;
	}
	
	public ButtonCfg getSkinBtn() {
		return skinBtn;
	}
	
	public ButtonCfg getCharts() {
		return charts;
	}

	public ButtonCfg getAboutBtn() {
		return aboutBtn;
	}


	public int getPanding() {
		return panding;
	}

	public String[] getSuffixs() {
		return suffixs;
	}


	public int getCellSize() {
		return cellSize;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public String getUrl() {
		return url;
	}
	
	
}
