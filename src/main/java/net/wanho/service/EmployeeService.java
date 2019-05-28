package net.wanho.service;

import java.util.List;
import java.util.Map;

import net.wanho.exception.ServiceException;
import net.wanho.util.PageBean;

public interface EmployeeService {
	public List<Map<String, Object>> showEmployees(Map<String, Object> param, PageBean pageBean) throws ServiceException;
	public int countForEmployeeWithLike(Map<String, Object> param) throws ServiceException;
	public boolean changeStatus(Integer EMPLOYEE_ID, Map<String, Object> param) throws ServiceException;
	public boolean addEmployee(Map<String, Object> employee) throws ServiceException;
	public Map<String, Object> employeeInfo(Integer EMPLOYEE_ID) throws ServiceException;
	public boolean updateEmployeeInfo(Integer EMPLOYEE_ID, String EMPLOYEE_NAME, Map<String, Object> param) throws ServiceException;
	public int countForEmployee() throws ServiceException;
	public Map<String, Object> login(Integer EMPLOYEE_ID, String password) throws ServiceException;
	public boolean updatePassword(Integer EMPLOYEE_ID, String oldpassword, String newpassword) throws ServiceException;
}
