package com.github.xwjdsh.snack;

import com.github.xwjdsh.dto.GameDto;
import com.github.xwjdsh.logic.GameLogic;


/**
 * 贪吃蛇游戏的逻辑实现类
 * @author xwjdsh
 */
public class GameLogic2Snack implements GameLogic{

	/**
	 * 贪吃蛇的数据对象
	 */
	private GameDto2Snack dtoSnack;
	
	/**
	 * 将蛇对象从dto中取出
	 */
	private Snack snack;
	
	@Override
	public boolean disposeUp() {
		return changeDirection(Snack.Direction.UP);
	}

	@Override
	public boolean disposeDown() {
		return changeDirection(Snack.Direction.DOWN);
	}

	@Override
	public boolean disposeRight() {
		return changeDirection(Snack.Direction.RIGHT);
	}

	@Override
	public boolean disposeLeft() {
		return changeDirection(Snack.Direction.LEFT);
	}
	
	private boolean changeDirection(Snack.Direction dir){
		//加入ischange判断是为了解决连续按方向造成蛇死亡的bug
		if(this.snack.isChange()){
			if(!this.snack.canChangeDir(dir))
				this.snack.setDirection(dir);
		}
		this.snack.setChange(false);
		return true;
	}
	
	@Override
	public void setGameDto(GameDto gameDto) {
		this.dtoSnack=(GameDto2Snack)gameDto;
		this.snack=this.dtoSnack.getSnack();
	}

	@Override
	public boolean disposeFunctionUp() {
		this.dtoSnack.changeShowShadow();
		return true;
	}

	@Override
	public boolean disposeFunctionDown() {
		this.dtoSnack.changeSuspend();
		return true;
	}

	@Override
	public boolean disposeFunctionLeft() {
		//改变地图
		snack.changeGameMap(dtoSnack.getMap());
		return true;
	}

	@Override
	public boolean disposeFunctionRight() {
		this.dtoSnack.setScore(dtoSnack.getScore()+this.dtoSnack.getLevel());
		this.dtoSnack.setAlreadyDone(this.dtoSnack.getAlreadyDone()+1);
		return true;
	}

	@Override
	public void disposeGameThread() {
		Boolean bl = this.snack.move(this.dtoSnack.getMap());
		if(bl==null){
			this.dtoSnack.setStart(false);
		}else if(bl){
			//加分操作
			this.dtoSnack.setScore(dtoSnack.getScore()+this.dtoSnack.getLevel());
			//改变已完成的
			this.dtoSnack.setAlreadyDone(this.dtoSnack.getAlreadyDone()+1);
			//升级操作
			if(this.dtoSnack.getScore()>SnackCfg.getSnackCfg().getLevelScore()*dtoSnack.getLevel()){
				dtoSnack.setLevel(dtoSnack.getLevel()+1);
				snack.changeGameMap(dtoSnack.getMap());
			}
		}
	}
}
