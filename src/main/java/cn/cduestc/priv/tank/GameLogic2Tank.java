package cn.cduestc.priv.tank;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.cduestc.priv.dto.GameDto;
import cn.cduestc.priv.logic.GameLogic;
import cn.cduestc.priv.tank.Tank.Direction;


/**
 * 坦克游戏的逻辑类
 * @author xwjdsh
 */
public class GameLogic2Tank implements GameLogic{

	/**
	 * 坦克游戏数据对象
	 */
	private GameDto2Tank dtoTank;
	
	/**
	 * 敌人坦克数组
	 */
	private Tank[] enemyTanks;
	
	/**
	 * 玩家坦克
	 */
	private Tank playerTank;
	
	/**
	 * 游戏地图
	 */
	private boolean[][] gameMap;
	
	/**
	 * 随机数生成器
	 */
	private Random random;
	
	
	
	@Override
	public boolean disposeUp() {
		synchronized (dtoTank) {
			if(!this.dtoTank.isSuspend()){
				if(playerTank.getDir()!=Direction.UP){
					playerTank.setDir(Direction.UP);
				}else{
					playerTank.move();
				}
			}
			return true;
		}
	}

	@Override
	public boolean disposeDown() {
		synchronized (dtoTank) {
			if(!this.dtoTank.isSuspend()){
				if(playerTank.getDir()!=Direction.DOWN){
					playerTank.setDir(Direction.DOWN);
				}else{
					playerTank.move();
				}
			}
			return true;
		}
	}

	@Override
	public boolean disposeRight() {
		synchronized (dtoTank) {
			if(!this.dtoTank.isSuspend()){
				if(playerTank.getDir()!=Direction.RIGHT){
					playerTank.setDir(Direction.RIGHT);
				}else{
					playerTank.move();
				}
			}
			return true;
		}
	}

	@Override
	public boolean disposeLeft() {
		synchronized (dtoTank) {
			if(!this.dtoTank.isSuspend()){
				if(playerTank.getDir()!=Direction.LEFT){
					playerTank.setDir(Direction.LEFT);
				}else{
					playerTank.move();
				}
			}
			return true;
		}
	}

	/**
	 * 为游戏逻辑增肌游戏数据对象
	 * @param gameDto
	 */
	@Override
	public void setGameDto(GameDto gameDto) {
		//向下转型
		this.dtoTank=(GameDto2Tank)gameDto;
		//取得敌人坦克数组引用
		this.enemyTanks=this.dtoTank.getEnemyTanks();
		//取得玩家坦克
		this.playerTank=this.dtoTank.getPlayerTank();
		//取得游戏地图
		this.gameMap=this.dtoTank.getMap();
		//创建随机数生成器
		this.random=new Random();
	}

	/**
	 * 游戏线程
	 */
	@Override
	public void disposeGameThread() {
		synchronized (dtoTank) {
			for(Tank enemy : this.enemyTanks){
				int i=this.random.nextInt(100);
				//百分之30改变方向
				if(i<30) enemy.setDir(Direction.values()[random.nextInt(4)]);
				//百分之80移动
				if(i<80) enemy.move();
				//百分之50射击
				if(i<50) enemy.shoot();
			}
			//移动子弹
			bullet_move();
		}
	}

	@Override
	public boolean disposeFunctionUp() {
		return false;
	}

	@Override
	public boolean disposeFunctionDown() {
		this.dtoTank.changeSuspend();
		return false;
	}

	@Override
	public boolean disposeFunctionLeft() {
		synchronized (dtoTank) {
			if(!this.dtoTank.isSuspend()){
				this.playerTank.shoot();
			}
			return false;
		}
	}

	@Override
	public boolean disposeFunctionRight() {
		return false;
	}
	
	/**
	 * 子弹移动方法
	 */
	public void bullet_move() {
		//删除重合和越界的子弹
		removeCrossCoincide();
		//检测敌人子弹
		enemyCheckHit();
		//检测玩家子弹
		playerCheckHit();
	}
	
	/**
	 * 移动子弹，出界和重合的删除 两次判断是为了避免一个为空时另一个不移动
	 */
	private void removeCrossCoincide(){
		//删除重合越界   敌人相对于玩家
		removeCrossCoincide(Tank.getEnemy_bullets(), Tank.getPlayer_bullets());
		//删除重合越界   玩家相对于敌人
		removeCrossCoincide(Tank.getPlayer_bullets(), Tank.getEnemy_bullets());
	}
	
	/**
	 * 检查敌人的坦克子弹是否击中玩家或者击中老王或障碍
	 */
	private void enemyCheckHit(){
		for(Map.Entry<Direction, List<Point>> entry : Tank.getEnemy_bullets().entrySet()){
			for(int i=entry.getValue().size()-1;i>=0;i--){
				Point bullet=entry.getValue().get(i);
				//是否击中老王
				if(bullet.x==TankCfg.getTankCfg().getGeneral().x&&bullet.y==TankCfg.getTankCfg().getGeneral().y){
					this.dtoTank.setStart(false);
				}
				//是否击中玩家
				for(Point p : this.playerTank.getTank_points()){
					if(p.x==bullet.x&&p.y==bullet.y){
						this.dtoTank.setStart(false);
					}
				}
				//是否击中障碍
				for(int j=Tank.getHindrances().size()-1;j>=0;j--){
					int x=Tank.getHindrances().get(j).x;
					int y=Tank.getHindrances().get(j).y;
					if(x==bullet.x&&y==bullet.y){
						this.gameMap[y][x]=false;
						entry.getValue().remove(i);
						Tank.getHindrances().remove(j);
					}
				}
			}
		}
	}
	
	/**
	 * 检查玩家子弹是否击中了敌人
	 */
	private void playerCheckHit(){
		for(Map.Entry<Direction,List<Point>> entry : Tank.getPlayer_bullets().entrySet()){
			Iterator<Point> it=entry.getValue().iterator();
			while(it.hasNext()){
				Point bullet=it.next();
				for(Tank enemy : this.enemyTanks){
					for(Point p : enemy.getTank_points()){
						if(bullet.x==p.x&&bullet.y==p.y){
							it.remove();
							enemy.restore();
							this.dtoTank.setScore(this.dtoTank.getScore()+1);
							//升级操作,升级的基数从配置文件中读取
							this.dtoTank.setLevel(this.dtoTank.getScore()>TankCfg.getTankCfg().getLevelScore()*dtoTank.getLevel()?dtoTank.getLevel()+1:dtoTank.getLevel());
							return;
						}
					}
				}
			}
		}
	}
	

	/**
	 * 移动子弹，出界和重合的删除
	 * @param map1  敌人或玩家
	 * @param map2 敌人或玩家
	 * 前者相对于后者
	 */
	private void removeCrossCoincide(Map<Direction, List<Point>> map1,Map<Direction,List<Point>> map2){
		for(Map.Entry<Direction,List<Point>> entry1 : map1.entrySet()){
			Direction direction=entry1.getKey();
			Iterator<Point> it=entry1.getValue().iterator();
			while(it.hasNext()){
				Point tmp=it.next();
				if(!direction.bullet_move(tmp)){
					it.remove();
					continue;
				}
				for(Map.Entry<Direction,List<Point>> entry2 : map2.entrySet()){
					for(int i=entry2.getValue().size()-1;i>=0;i--){
						Point p=entry2.getValue().get(i);
						if(tmp.x==p.x&&tmp.y==p.y){
							entry2.getValue().remove(i);
							continue;
						}
					}
				}
			}
		}
	}
}
