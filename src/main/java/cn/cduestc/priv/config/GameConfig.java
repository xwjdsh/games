package cn.cduestc.priv.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import cn.cduestc.priv.entity.Player;

/**
 * 游戏的配置类
 * 
 * @author xwjdsh
 */
public class GameConfig {

	/**
	 * 游戏id,数据库查询使用
	 */
	private int gameId;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 游戏逻辑的全类名
	 */
	private String logicClassName;

	/**
	 * 配置文件路径
	 */
	private String cfgPath;

	/**
	 * 素材路径
	 */
	private String materialPath;

	/**
	 * 数据对象全类名
	 */
	private String dtoClassName;

	/**
	 * 排行数据路径
	 */
	private String chartsPath;

	/**
	 * 方法与键位的对应
	 */
	private Map<String, Integer> methodkeyMap;

	/**
	 * 描述与键位的对应
	 */
	private Map<String, Integer> destributeKeyMap;

	/**
	 * 描述与方法的对应
	 */
	private Map<String, String> destributeMethodMap;

	/**
	 * 游戏排行的存储
	 */
	private List<Player> players;

	/**
	 * 游戏存储排行的数量
	 */
	private int chartsNum;

	public GameConfig(int gameId, String gameName, String className, String cfgPath, String materialPath,
			String dtoClassName, String chartsPath, int chartsNum) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.logicClassName = className;
		this.cfgPath = cfgPath;
		this.materialPath = materialPath;
		this.dtoClassName = dtoClassName;
		this.chartsPath = chartsPath;
		this.players = this.parseData();
		this.chartsNum = chartsNum;
	}

	/**
	 * 初始化键位信息，取得方法名和描述对keycode的映射
	 */
	@SuppressWarnings("unchecked")
	public void init_keys() {
		try {
			SAXReader reader = new SAXReader();
			// 读取配置文件，配置文件路径选中游戏时取得
			Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream(this.cfgPath));
			// 取得键位元素
			List<Element> keylist = document.getRootElement().element("keys").elements();
			// 初始化方法-键位映射
			this.methodkeyMap = new HashMap<String, Integer>(keylist.size());
			// 初始化描述-键位映射
			this.destributeKeyMap = new HashMap<String, Integer>(keylist.size());
			// 初始化描述-方法映射
			this.destributeMethodMap = new HashMap<String, String>(keylist.size());

			for (Element ele : keylist) {
				// 取得方法名字
				String methodName = ele.attributeValue("method");
				// 取得keycode
				int keycode = Integer.parseInt(ele.attributeValue("keycode"));
				// 取得描述信息
				String describe = ele.attributeValue("describe");
				methodkeyMap.put(methodName, keycode);
				destributeKeyMap.put(describe, keycode);
				destributeMethodMap.put(describe, methodName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新键位
	 */
	public void updateKey(Map<String, Integer> newDestributeKeyMap) {
		// 改变当前描述与键位的引用
		this.destributeKeyMap = newDestributeKeyMap;
		// 改变方法与键位的映射
		for (Map.Entry<String, Integer> entry : newDestributeKeyMap.entrySet()) {
			this.methodkeyMap.put(destributeMethodMap.get(entry.getKey()), entry.getValue());
		}
	}

	/**
	 * 修改配置文件信息,使用xpath
	 */
	public void writeKeyConfig() {

		XMLWriter xw = null;
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(this.cfgPath);
			for (Map.Entry<String, Integer> entry : this.destributeKeyMap.entrySet()) {
				// 通过xpath来获取对应描述的键位属性
				Attribute arr = (Attribute) doc
						.selectSingleNode(String.format("//key[@describe='%s']/@keycode", entry.getKey()));
				// 更新键位
				arr.setValue(String.valueOf(entry.getValue()));
			}
			// 指定输出格式，有空符的xml格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码方式
			format.setEncoding("utf-8");
			// 输出流，参数使用FileOutputStream,FileWriter会产生编码错误
			xw = new XMLWriter(new FileOutputStream(new File(this.cfgPath)), format);
			// 写入xml文件
			xw.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 非空情况下关闭流
			if (xw != null) {
				try {
					xw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解析为排行数据
	 * 
	 * @param chartsFile
	 *            数据文件
	 */
	@SuppressWarnings("unchecked")
	private List<Player> parseData() {
		// 输入对象流
		ObjectInputStream ois = null;
		try {
			URL resource = this.getClass().getClassLoader().getResource("charts");
			String path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
			File chartsFile = new File(path + "/" + this.chartsPath);
			if (!chartsFile.exists()) {
				return null;
			}
			System.out.println(chartsFile.getAbsolutePath());
			ois = new ObjectInputStream(new FileInputStream(chartsFile));
			// 反序列化解析为对象
			return ((List<Player>) ois.readObject());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 非空情况下关闭输入流
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 清除排行数据
	 */
	public void clearCharts() {
		try {

			// 将当期排行数据对象置空
			this.players = null;
			URL resource = this.getClass().getClassLoader().getResource("charts");
			String path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
			File chartsFile = new File(path + "/" + this.chartsPath);
			// 删除存储文件
			if (chartsFile.exists()) {
				chartsFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新当前排行数据并刷新存储文件
	 * 
	 * @param player
	 *            新添加的玩家对象
	 */
	public void updateCharts(Player player) {
		if (this.players == null) {
			this.players = new ArrayList<Player>(this.chartsNum + 1);
		}
		// 添加对象
		this.players.add(player);
		// 对集合进行排序操作，实现Comparator接口compare方法
		Collections.sort(this.players, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return p2.getScore() - p1.getScore();
			}
		});
		if (this.players.size() > this.chartsNum) {
			// 删除多出的记录
			this.players.remove(this.chartsNum);
		}
		// 刷新存储文件
		writeCharts();
	}

	/**
	 * 刷新存储文件
	 */
	private void writeCharts() {
		// 输出对象流
		ObjectOutputStream oos = null;
		try {
			URL resource = this.getClass().getClassLoader().getResource("charts");
			String path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
			oos = new ObjectOutputStream(new FileOutputStream(new File(path + "/" + this.chartsPath)));
			// 输出
			oos.writeObject(this.players);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 非空条件下关闭流
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getGameName() {
		return gameName;
	}

	public String getLogicClassName() {
		return logicClassName;
	}

	public String getCfgPath() {
		return cfgPath;
	}

	public String getMaterialPath() {
		return materialPath;
	}

	public String getDtoClassName() {
		return dtoClassName;
	}

	public Map<String, Integer> getMethodkeyMap() {
		return methodkeyMap;
	}

	public Map<String, Integer> getKeyDestributeMap() {
		return destributeKeyMap;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public int getGameId() {
		return gameId;
	}

}
