package net.wanho.dao;

import java.util.List;
import java.util.Map;

import net.wanho.exception.DaoException;
import net.wanho.util.PageBean;

public interface EmployeeDao {
	public List<Map<String, Object>> queryAll();
	public List<Map<String, Object>> queryEmployeesWithLike(Map<String, Object> param, PageBean pageBean) throws DaoException;
	public int employeeCountWithLike(Map<String, Object> param) throws DaoException;
	public Integer insertEmployee(Map<String, Object> employee) throws DaoException;
	public boolean deleteEmployeeById(Integer EMPLOYEE_ID);
	public boolean updateEmployeeById(Integer EMPLOYEE_ID, Map<String, Object> param) throws DaoException;
	public boolean updateEmployeeByIdAndName(Integer EMPLOYEE_ID, String EMPLOYEE_NAME, Map<String, Object> param) throws DaoException;
	public Map<String, Object> queryEmployeeById(Integer EMPLOYEE_ID) throws DaoException;
	public int employeeCount() throws DaoException;
	public Map<String, Object> queryEmployeeByIdAndPassword(Integer EMPLOYEE_ID, String password) throws DaoException;
	public boolean updateEmployeeLoginPasswordByIdAndPassword(Integer EMPLOYEE_ID, String oldpassword, String newpassword) throws DaoException;
	public boolean insertLoginByid(Integer id) throws DaoException;
}
