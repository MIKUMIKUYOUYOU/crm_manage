package net.wanho.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JDBCTemplateUpdate {
	/**
	 * 输入QL查询语句
	 * 
	 * RowMapper控制输入对象类型(已被抛弃)
	 * 
	 * obj 参数列表
	 * 
	 * 使用反射取出
	 * 
	 * version 1.0
	 */
	public static List queryForList(final String QL_SQL, Class clazz, Object... objs) {
		List list = new ArrayList<>();
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
			Field[] fields = clazz.getDeclaredFields();
			Object object = null;
			Object take = null;
			while (rs.next()) {
				object = clazz.newInstance();
				for (Field field : fields) {
					field.setAccessible(true);
					try {
						take = rs.getObject(rs.findColumn(field.getName()));
					} catch (Exception e) {
						take=null;
						System.err.println("未找到" + field.getType().getName() + " " + field.getName() + "属性");
					}
					if (take instanceof BigDecimal) {
						BigDecimal bd = (BigDecimal) take;
						try {
							field.set(object, bd.doubleValue());
						} catch (Exception e) {
							field.set(object, bd.intValue());
						}
					} else
						field.set(object, take);
				}
				list.add(object);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
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
		return null;
	}

	/**
	 * 搜出一个对象并将其转为T(暂时未知)类型 使用反射取出
	 */
	public static <T> T queryForObject(final String QL_SQL, Class clazz, Object... objs) {
		List list = JDBCTemplateUpdate.queryForList(QL_SQL, clazz, objs);
		return (T) (list.isEmpty() ? null : list.get(0));
	}

	public static boolean update(final String DML_SQL, Object... objs) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(DML_SQL);
			for (int i = 0; i < objs.length; i++) {
				ps.setObject(i + 1, objs[i]);
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