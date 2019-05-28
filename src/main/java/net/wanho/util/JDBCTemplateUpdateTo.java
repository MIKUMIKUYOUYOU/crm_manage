package net.wanho.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wanho.exception.DataException;

public class JDBCTemplateUpdateTo {

	/**
	 * 输入QL查询语句
	 * 
	 * RowMapper控制输入对象类型(已被抛弃)
	 * 
	 * obj 参数列表
	 * 
	 * version 2.1 NO Mappring NO Entity 2.0去除了实体类将存储结构改为类表集合嵌套:Map IN List
	 * 2.1更改了之前只能取的原列名 现在能够取别名
	 * 
	 * @throws DataException
	 */
	public static List<Map<String, Object>> queryForList(final String QL_SQL, Object... parma) throws DataException {
		List<Map<String, Object>> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = JDBCUtil.getConnection();
		try {
			ps = conn.prepareStatement(QL_SQL);
			for (int i = 0; i < parma.length; i++) {
				ps.setObject(i + 1, parma[i]);
			}
			rs = ps.executeQuery();
			String strName = null;
			Object value = null;
			ResultSetMetaData rsmd = rs.getMetaData();
			int index;
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				index = 1;
				while (true) {
					try {
//						rsmd.getColumnName()此方法获取的是最原始的列名 并不能获取别名
						// getColumnLabel能够获取别名
						strName = rsmd.getColumnLabel(index);
						value = rs.getObject(index++);
						// 以后根据自己的需求自行转换
//							if (value instanceof BigDecimal) {
//								BigDecimal bd = (BigDecimal) value;
//								try {
//									map.put(strName, bd.doubleValue());
//								} catch (Exception e) {
//									map.put(strName, bd.intValue());
//								}
//							} else
						map.put(strName, value);
					} catch (Exception e) {
						break;
					}
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException();
		}
		JDBCUtil.close(ps, rs);
		return list;
	}

	/**
	 * pick the first
	 * 
	 * @throws DataException
	 */
	public static Map<String, Object> queryForObject(final String QL_SQL, Object... parma) throws DataException {
		List<Map<String, Object>> list = JDBCTemplateUpdateTo.queryForList(QL_SQL, parma);
		return list.isEmpty() ? null : list.get(0);
	}

	public static boolean update(final String DML_SQL, Object... parma) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = JDBCUtil.getConnection();
		boolean tf = false;
		try {
			ps = conn.prepareStatement(DML_SQL);
			for (int i = 0; i < parma.length; i++) {
				ps.setObject(i + 1, parma[i]);
			}
			tf = ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException();
		}
		JDBCUtil.close(ps, null);
		return tf;
	}

	public static Object updateAndBackPK(final String DML_SQL, Object... parma) throws DataException {
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
			throw new DataException();
		}
		JDBCUtil.close(ps, null);
		return obj;
	}

	/**
	 * 此方法可以输出一行一列的数字 可以用于判定搜索用户是否存在于列表
	 * 
	 * @throws DataException
	 * 
	 */
	public static int queryForInt(final String QL_SQL, Object... objs) throws DataException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result=0;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(QL_SQL);
			for (int i = 0; i < objs.length; i++) {
				ps.setObject(i + 1, objs[i]);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataException();
		}
		JDBCUtil.close(ps, rs);
		return result;
	}

}
