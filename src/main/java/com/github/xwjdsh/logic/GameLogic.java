package com.github.xwjdsh.logic;

import com.github.xwjdsh.dto.GameDto;

/**
 * 上层接口。游戏逻辑的接口，针对游戏选择逻辑，同时避免耦合
 * @author xwjdsh
 */
public interface GameLogic {
	/**
	 * 处理上键
	 */
	boolean disposeUp();
	
	/**
	 * 处理下键
	 */
	boolean disposeDown();
	
	/**
	 * 处理左键
	 */
	boolean disposeRight();
	
	/**
	 * 处理右键
	 */
	boolean disposeLeft();
	
	/**
	 * 设置游戏dto
	 * @param gameDto 游戏数据对象
	 */
	void setGameDto(GameDto gameDto);
	
	/**
	 * 处理游戏的线程
	 */
	void disposeGameThread();
	
	/**
	 * 处理功能键
	 */
	boolean disposeFunctionUp();
	boolean disposeFunctionDown();
	boolean disposeFunctionLeft();
	boolean disposeFunctionRight();
	
	
}
