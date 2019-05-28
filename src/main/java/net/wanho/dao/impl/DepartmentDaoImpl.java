package net.wanho.dao.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.DepartmentDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.DataException;
import net.wanho.util.JDBCTemplateUpdateTo;

public class DepartmentDaoImpl implements DepartmentDao {

	@Override
	public List<Map<String, Object>> queryAllDepartment() throws DaoException {
		final String SELECT_ALL_DEPARTMENT_SQL="SELECT DEPARTMENT_ID,DEPARTMENT_NAME FROM department";
		List<Map<String, Object>> list=null;
		try {
			list=JDBCTemplateUpdateTo.queryForList(SELECT_ALL_DEPARTMENT_SQL);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

}
