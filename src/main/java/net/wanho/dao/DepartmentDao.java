package net.wanho.dao;

import java.util.List;
import java.util.Map;

import net.wanho.exception.DaoException;

public interface DepartmentDao {
	public List<Map<String, Object>> queryAllDepartment() throws DaoException;
}
