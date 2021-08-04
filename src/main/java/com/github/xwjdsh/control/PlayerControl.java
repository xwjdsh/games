package com.github.xwjdsh.control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 玩家控制器（手柄），控制游戏控制器，继承了键盘适配器类，将事件传递给游戏控制器
 * @author xwjdsh
 */
public class PlayerControl extends KeyAdapter {
	
	private GameControl gameContraol;
	
	
	public PlayerControl(GameControl gameContraol) {
		this.gameContraol = gameContraol;
	}

	/**
	 * 键盘按下事件，分派给gamecontrol进行对应处理
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		this.gameContraol.disposeKey(e.getKeyCode());
	}
}
