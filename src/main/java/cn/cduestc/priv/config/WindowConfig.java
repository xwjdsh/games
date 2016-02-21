package cn.cduestc.priv.config;

/**
 * 窗口的配置对象
 * 
 * @author xwjdsh
 */
public class WindowConfig {

	/**
	 * 小窗口的全类名
	 */
	private String classNmae;

	/**
	 * 左上角x坐标
	 */
	private int startX;

	/**
	 * 左上角y坐标
	 */
	private int startY;

	/**
	 * 窗口宽度
	 */
	private int width;

	/**
	 * 窗口高度
	 */
	private int height;

	public WindowConfig(String classNmae, int startX, int startY, int width,
			int height) {
		this.classNmae = classNmae;
		this.startX = startX;
		this.startY = startY;
		this.height = height;
		this.width = width;
	}

	public String getClassNmae() {
		return classNmae;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
