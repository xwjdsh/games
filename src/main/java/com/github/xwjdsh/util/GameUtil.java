package com.github.xwjdsh.util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.xwjdsh.config.SysConfig;

/**
 * 游戏辅助类，主要是一些静态方法
 * 
 * @author xwjdsh
 */
public class GameUtil {

	private GameUtil() {
	}

	/**
	 * 使窗口居中显示
	 */
	public static void middle(Container conta) {
		// 居中显示
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		int w = screen.width - conta.getWidth() >> 1;
		int h = (screen.height - conta.getHeight() >> 1) - 16;
		conta.setLocation(w, h);
	}

	public static void installImage(Class<?> imageClass, String material) {
		// 通过反射取得字段名
		for (Field field : imageClass.getFields()) {
			for (String suffix : SysConfig.getCfg().getSuffixs()) {
				// 构造路径
				String path = material + "/" + field.getName() + suffix;
				// 检查文件是否存在
				try {
					// 为字段赋值
					InputStream input=GameUtil.class.getClassLoader().getResourceAsStream(path);
					if(input!=null){
						field.set(null,
								new ImageIcon(ImageIO.read(input)).getImage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}