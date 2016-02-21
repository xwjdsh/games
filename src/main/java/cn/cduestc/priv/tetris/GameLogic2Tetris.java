package cn.cduestc.priv.tetris;

import java.awt.Point;
import java.util.Random;

import cn.cduestc.priv.dto.GameDto;
import cn.cduestc.priv.logic.GameLogic;

/**
 * 俄罗斯方块的逻辑实现类，控制dto中的数据
 * @author xwjdsh
 */
public class GameLogic2Tetris implements GameLogic {

	/**
	 * 游戏数据传输对象，只能被逻辑层修改，显示层读
	 */
	private GameDto2Tetris dtoTetris;

	/**
	 * 方块的对象，从dto中取出便于使用
	 */
	private Square square;

	/**
	 * 游戏地图
	 */
	private boolean[][] gameMap;

	/**
	 * 生成随机数对象
	 */
	private Random random = new Random();

	/**
	 * 方块的种类数
	 */
	private final int SQUARE_COUNT = Square.getPoints_list().size();

	@Override
	public boolean disposeUp() {
		// 旋转
		synchronized (dtoTetris) {
			if(!dtoTetris.isSuspend())
				square.rotate(gameMap);
			return true;
		}
	}

	@Override
	public boolean disposeDown() {
		synchronized (dtoTetris) {
			if(this.dtoTetris.isStart()&&!this.dtoTetris.isSuspend()){
				if (square.move(0, 1, gameMap)) {
					return true;
				}
				// 当按键下方块不移动时，改变相应的地图
				for (Point point : square.getSquares()) {
					gameMap[point.y][point.x] = true;
				}
				// 把当前方块变成下一个方块
				square.changeSquares(this.dtoTetris.getNext());
				// 改变下一个方块的引用
				this.dtoTetris.setNext(square.nextSquares(random.nextInt(SQUARE_COUNT)));
				// 取得所消的行数
				int removeLineNum = square.RemoveLine(gameMap);
				//改变dto中的数据
				this.dtoTetris.setAlreadyDone(this.dtoTetris.getAlreadyDone()+ removeLineNum);
				// 根据所消的行数进行加分操作，加分按照简单的非1倍乘方式
				this.dtoTetris.setScore(this.dtoTetris.getScore()+(removeLineNum == 1 ? removeLineNum : removeLineNum << 1));
				//升级操作,升级的基数从配置文件中读取
				this.dtoTetris.setLevel(this.dtoTetris.getScore()>TetrisCfg.getTetrisCfg().getLevelScore()*dtoTetris.getLevel()?dtoTetris.getLevel()+1:dtoTetris.getLevel());
				//检测游戏是否结束：判断新刷新的方块是否与地图有重合，重合判负
				for(Point p : this.square.getSquares()){
					if(this.gameMap[p.y][p.x]){
						this.dtoTetris.setStart(false);
					}
				}
				
			}
			return false;
		}
	}

	@Override
	public boolean disposeRight() {
		synchronized (dtoTetris) {
			if(!this.dtoTetris.isSuspend())
				square.move(1, 0, gameMap);
			return true;
		}
	}

	@Override
	public boolean disposeLeft() {
		synchronized (dtoTetris) {
			if(!this.dtoTetris.isSuspend())
				square.move(-1, 0, gameMap);
			return true;
		}
	}

	/**
	 * 为游戏逻辑增加游戏数据对象，并做相关初始化操作
	 * @param gameDto
	 */
	@Override
	public void setGameDto(GameDto gameDto) {
		this.dtoTetris = (GameDto2Tetris) gameDto;
		// 取得方块的引用
		this.square = dtoTetris.getSqure();
		// 获取游戏地图
		this.gameMap = dtoTetris.getMap();
	}

	@Override
	public boolean disposeFunctionUp() {
		this.dtoTetris.changeShowShadow();
		return true;
	}

	@Override
	public boolean disposeFunctionDown() {
		this.dtoTetris.changeSuspend();	
		return true;
	}

	

	@Override
	public boolean disposeFunctionLeft() {
		while(disposeDown());
		return true;
	}

	@Override
	public boolean disposeFunctionRight() {
		this.dtoTetris.setScore(this.dtoTetris.getScore()+10);
		this.dtoTetris.setLevel(this.dtoTetris.getScore()>TetrisCfg.getTetrisCfg().getLevelScore()*dtoTetris.getLevel()?dtoTetris.getLevel()+1:dtoTetris.getLevel());
		return true;
	}

	@Override
	public void disposeGameThread() {
		this.disposeDown();
	}

}
