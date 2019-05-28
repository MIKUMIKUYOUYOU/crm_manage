package net.wanho.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JDBCTemplateUpdateTr {

	/**
	 * 输入QL查询语句
	 * 
	 * RowMapper控制输入对象类型(已被抛弃)
	 * 
	 * obj 参数列表
	 * 
	 * version 3.0 保持了2.0的NO Mappring NO Entity的特性 
	 * 3.0继承了2.1能够取别名的效果
	 * 3.0将类表集合嵌套存储结构更改成List IN List更符合数据库中的存储样式
	 */
	public static List<List<Object>> queryForList(final String QL_SQL, Object... parma) {
		List<List<Object>> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(QL_SQL);
			for (int i = 0; i < parma.length; i++) {
				ps.setObject(i + 1, parma[i]);
			}
			rs = ps.executeQuery();
			String strName = null;
			Object value = null;
			ResultSetMetaData rsmd = rs.getMetaData();
			int index = 1;
			// 第一个List存储列名
			List<Object> columnNameList = new ArrayList<Object>();
			while (true) {
				try {
					// rsmd.getColumnName()此方法获取的是最原始的列名 并不能获取别名
					// getColumnLabel能够获取别名
					strName = rsmd.getColumnLabel(index);
					index++;
					columnNameList.add(strName);
				} catch (Exception e) {
					break;
				}
			}
			list.add(columnNameList);
			// 每一列为一个List
			while (rs.next()) {
				index = 1;
				List<Object> valueList = new ArrayList<Object>();
				while (true) {
					try {
						value = rs.getObject(index++);
						// 以后根据自己的需求自行转换
//						if (value instanceof BigDecimal) {
//							BigDecimal bd = (BigDecimal) value;
//							try {
//								map.put(strName, bd.doubleValue());
//							} catch (Exception e) {
//								map.put(strName, bd.intValue());
//							}
//						} else
						valueList.add(value);
					} catch (Exception e) {
						break;
					}
				}
				list.add(valueList);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * pick the first
	 */
	public static List<Object> queryForObject(final String QL_SQL, Object... parma) {
		List<List<Object>> list = JDBCTemplateUpdateTr.queryForList(QL_SQL, parma);
		return list.isEmpty() ? null : list.get(1);
	}

	public static boolean update(final String DML_SQL, Object... parma) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(DML_SQL);
			for (int i = 0; i < parma.length; i++) {
				ps.setObject(i + 1, parma[i]);
			}
			boolean tf = ps.executeUpdate() > 0;
			return tf;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static Object updateAndBackPK(final String DML_SQL, Object... parma) {
		Connection conn = null;
		PreparedStatement ps = null;
		Object obj = null;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(DML_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < parma.length; i++) {
				ps.setObject(i + 1, parma[i]);
			}
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 此方法可以输出一行一列的数字 可以用于判定搜索用户是否存在于列表
	 * 
	 */
	public static int queryForInt(final String QL_SQL, Object... objs) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(QL_SQL);
			for (int i = 0; i < objs.length; i++) {
				ps.setObject(i + 1, objs[i]);
			}
			rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				i = rs.getInt(1);
				break;
			}
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
