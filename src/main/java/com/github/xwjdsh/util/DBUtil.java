package com.github.xwjdsh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.xwjdsh.config.SysConfig;
import com.github.xwjdsh.entity.Player;

public class DBUtil {
	
	/**
	 * 连接数据库的用户名
	 */
	private  final String username= SysConfig.getCfg().getUsername();
	
	/**
	 * 连接数据库的密码
	 */
	private  final String password=SysConfig.getCfg().getPassword();
	
	/**
	 * url  
	 */
	private  final String url=SysConfig.getCfg().getUrl();
	
	/**
	 * 单例对象
	 */
	private static DBUtil dbUtil;
	
	/**
	 * 连接对象
	 */
	private  Connection conn;
	
	/**
	 * 通过静态代码块加载类驱动
	 */
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得连接
	 */
	private void connection() throws Exception{
		conn=DriverManager.getConnection(url, username, password);
	}
	
	/**
	 * 执行更新语句
	 * @param sql 执行的sql
	 */
	public void executeUpdate(String sql){
		try {
			connection();
			conn.createStatement().execute(sql);
			closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeConn();
		}
	}
	
	/**
	 * 执行查询语句
	 * @param sql 执行的查询sql
	 */
	public List<Player> executeQuery(String sql){
		try {
			connection();
			ResultSet rs=conn.createStatement().executeQuery(sql);
			return changeResult(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeConn();
		}
	}
	
	/**
	 * 关闭连接
	 */
	private void closeConn(){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 转换结果集
	 * @param rs 待转换的结果集
	 * @return 返回转换完成的结果集
	 * @throws Exception
	 */
	private List<Player> changeResult(ResultSet rs) throws Exception{
		List<Player> players = new ArrayList<Player>();
		while(rs.next()){
			players.add(new Player(rs.getString("name"), rs.getInt("score"), rs.getInt("done"), rs.getInt("level")));
		}
		return players;
	}

	public static DBUtil getDbUtil() {
		if(dbUtil==null){
			dbUtil=new DBUtil();
		}
		return dbUtil;
	}
	
	
}
