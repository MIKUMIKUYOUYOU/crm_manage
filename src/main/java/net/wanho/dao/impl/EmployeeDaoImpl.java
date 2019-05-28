package net.wanho.dao.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.EmployeeDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.DataException;
import net.wanho.util.JDBCTemplateUpdateTo;
import net.wanho.util.JDBCUtil;
import net.wanho.util.PageBean;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public List<Map<String, Object>> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> queryEmployeesWithLike(Map<String, Object> param, PageBean pageBean)
			throws DaoException {
		StringBuffer SELECT_EMPLOYEES_SQL = new StringBuffer(
				"SELECT e.EMPLOYEE_ID,e.EMPLOYEE_NAME,d.DEPARTMENT_NAME,p.POSITION_NAME,e.PHONE,e.EMAIL,e.STATUS,e.PARENT_EMPLOYEE_ID,e.CREATE_TIME,e.UPDATE_TIME FROM employee e LEFT JOIN emm_position p ON e.POSITION_ID=p.POSITION_ID LEFT JOIN department d ON d.DEPARTMENT_ID=e.DEPARTMENT_ID WHERE 1=1 ");
		Integer employeeId = null;
		String employeeName = null;
		if (param != null) {
			employeeId = (Integer) param.get("EMPLOYEE_ID");
			employeeName = (String) param.get("EMPLOYEE_NAME");
		}
		List<Map<String, Object>> list = null;
		if (employeeId != null) {
			SELECT_EMPLOYEES_SQL.append(" AND e.EMPLOYEE_ID=" + employeeId);
		}
		if (employeeName != null) {
			SELECT_EMPLOYEES_SQL.append(" AND e.EMPLOYEE_NAME LIKE '%" + employeeName + "%'");
		}
		SELECT_EMPLOYEES_SQL.append(" ORDER BY e.EMPLOYEE_ID");
		SELECT_EMPLOYEES_SQL.append(" LIMIT " + pageBean.getStart() + "," + pageBean.getPageSize());
		try {
			list = JDBCTemplateUpdateTo.queryForList(SELECT_EMPLOYEES_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

	@Override
	public Integer insertEmployee(Map<String, Object> employee) throws DaoException {
		final String INSERT_EMPLOYEE_SQL = "INSERT INTO employee(EMPLOYEE_NAME,DEPARTMENT_ID,POSITION_ID,PHONE,EMAIL,STATUS,PARENT_EMPLOYEE_ID) VALUES (?,?,?,?,?,'1',?)";
		Integer id = null;
		
		try {
			Long longId =  (Long)JDBCTemplateUpdateTo.updateAndBackPK(INSERT_EMPLOYEE_SQL, employee.get("EMPLOYEE_NAME"),
					employee.get("DEPARTMENT_ID"), employee.get("POSITION_ID"), employee.get("PHONE"),
					employee.get("EMAIL"), employee.get("PARENT_EMPLOYEE_ID"));
			id=longId.intValue();
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return id;
	}

	@Override
	public boolean deleteEmployeeById(Integer EMPLOYEE_ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateEmployeeById(Integer EMPLOYEE_ID, Map<String, Object> param) throws DaoException {
		StringBuffer UPDATE_EMPLOYEE_SQL = JDBCUtil.updateStringBuffer("employee", param);
		UPDATE_EMPLOYEE_SQL.append(" WHERE EMPLOYEE_ID=?");
		boolean flag = false;
		try {
			flag = JDBCTemplateUpdateTo.update(UPDATE_EMPLOYEE_SQL.toString(), EMPLOYEE_ID);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

	@Override
	public boolean updateEmployeeByIdAndName(Integer EMPLOYEE_ID, String EMPLOYEE_NAME, Map<String, Object> param)
			throws DaoException {
		StringBuffer UPDATE_EMPLOYEE_SQL = JDBCUtil.updateStringBuffer("employee", param);
		UPDATE_EMPLOYEE_SQL.append(" WHERE EMPLOYEE_ID=? AND EMPLOYEE_NAME=?");
		boolean flag = false;
		try {
			flag = JDBCTemplateUpdateTo.update(UPDATE_EMPLOYEE_SQL.toString(), EMPLOYEE_ID, EMPLOYEE_NAME);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

	@Override
	public Map<String, Object> queryEmployeeById(Integer EMPLOYEE_ID) throws DaoException {
		final String SELECT_EMPLOYEE_BY_ID_SQL = "SELECT EMPLOYEE_ID,EMPLOYEE_NAME,DEPARTMENT_ID,POSITION_ID,PHONE,EMAIL,PARENT_EMPLOYEE_ID FROM employee WHERE EMPLOYEE_ID=?";
		Map<String, Object> employee = null;
		try {
			employee = JDBCTemplateUpdateTo.queryForObject(SELECT_EMPLOYEE_BY_ID_SQL, EMPLOYEE_ID);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return employee;
	}

	@Override
	public int employeeCount() throws DaoException {
		final String SELECT_EMPLOYEE_COUNT_SQL = "SELECT COUNT(1) FROM employee";
		int count = 0;
		try {
			count = JDBCTemplateUpdateTo.queryForInt(SELECT_EMPLOYEE_COUNT_SQL);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public int employeeCountWithLike(Map<String, Object> param) throws DaoException {
		StringBuffer SELECT_EMPLOYEE_COUNT_SQL = new StringBuffer(
				"SELECT COUNT(1) FROM employee e LEFT JOIN emm_position p ON e.POSITION_ID=p.POSITION_ID LEFT JOIN department d ON d.DEPARTMENT_ID=e.DEPARTMENT_ID WHERE 1=1 ");
		Integer employeeId = null;
		String employeeName = null;
		if (param != null) {
			employeeId = (Integer) param.get("EMPLOYEE_ID");
			employeeName = (String) param.get("EMPLOYEE_NAME");
		}
		if (employeeId != null) {
			SELECT_EMPLOYEE_COUNT_SQL.append(" AND e.EMPLOYEE_ID=" + employeeId);
		}
		if (employeeName != null) {
			SELECT_EMPLOYEE_COUNT_SQL.append(" AND e.EMPLOYEE_NAME LIKE '%" + employeeName + "%'");
		}
		int count = 0;
		try {
			count = JDBCTemplateUpdateTo.queryForInt(SELECT_EMPLOYEE_COUNT_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public Map<String, Object> queryEmployeeByIdAndPassword(Integer EMPLOYEE_ID, String password) throws DaoException {
		final String SELECT_LOG_IN_EMPLOYEE_SQL = "SELECT l.EMPLOYEE_ID,l.EMP_PASSWORD,e.EMPLOYEE_NAME,e.POSITION_ID FROM log_in l,employee e WHERE l.EMPLOYEE_ID=e.EMPLOYEE_ID AND l.EMPLOYEE_ID=? AND l.EMP_PASSWORD=?";
		Map<String, Object> user = null;
		try {
			user = JDBCTemplateUpdateTo.queryForObject(SELECT_LOG_IN_EMPLOYEE_SQL, EMPLOYEE_ID, password);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return user;
	}

	@Override
	public boolean updateEmployeeLoginPasswordByIdAndPassword(Integer EMPLOYEE_ID, String oldpassword,
			String newpassword) throws DaoException {
		final String UPDATE_LOG_IN_SQL = "UPDATE log_in SET EMP_PASSWORD=? WHERE EMPLOYEE_ID=? AND EMP_PASSWORD=?";
		boolean flag = false;
		try {
			flag = JDBCTemplateUpdateTo.update(UPDATE_LOG_IN_SQL, newpassword, EMPLOYEE_ID, oldpassword);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean insertLoginByid(Integer id) throws DaoException {
		final String INSERT_INTO_LOG_IN_SQL = "INSERT INTO log_in(EMPLOYEE_ID,EMP_PASSWORD) VALUES(?,'123456')";
		boolean flag = false;
		try {
			flag = JDBCTemplateUpdateTo.update(INSERT_INTO_LOG_IN_SQL, id);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

}
