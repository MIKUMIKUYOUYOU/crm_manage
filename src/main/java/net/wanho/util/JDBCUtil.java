package net.wanho.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class JDBCUtil {
	private static DataSource ds;
	private static ThreadLocal<Connection> threadlocal=new ThreadLocal<Connection>();
	
	static {
		Properties prop=new Properties();
		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");
			prop.load(JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			ds=BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	public static void close(PreparedStatement ps,ResultSet rs) {
		try {
			if(ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param tableName 表名
	 * @param param 列参数
	 * @return 返回一个不带WHERE条件的UPDATE语句
	 */
	public static StringBuffer updateStringBuffer(String tableName,Map<String, Object> param) {
		StringBuffer SQL=new StringBuffer("UPDATE "+tableName+" SET ");
		if(param!=null) {
			if(!param.isEmpty()) {
				for (String paramName : param.keySet()) {
					SQL.append(paramName+"='"+param.get(paramName)+"',");
				}
			}
		}
		SQL.deleteCharAt(SQL.length()-1);
		return SQL;
	}
//	public static Connection getConnection() {
//		try {
//			Connection conn=threadlocal.get();
//			
//			if(conn==null||conn.isClosed()){
//				Properties properties=new Properties();
//				properties.load(JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties"));
//				conn=DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
//				threadlocal.set(conn);
//			}
//			return conn;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	public static Connection getConnection() {
		try {
			Connection conn=threadlocal.get();
			if(conn==null||conn.isClosed()){
//				System.out.println("ok");
				conn=ds.getConnection();
				//装饰着模式修改过的连接
//				System.out.println(conn.getClass().getName() + '@' + Integer.toHexString(conn.hashCode()));
				threadlocal.set(conn);
				
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
