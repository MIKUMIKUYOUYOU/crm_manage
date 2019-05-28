package net.wanho.service.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.EmployeeDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.ServiceException;
import net.wanho.service.EmployeeService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;

public class EmployeeServiceImpl implements EmployeeService {
	private EmployeeDao employeeDao=(EmployeeDao) ObjectFactory.getObject("EmployeeDao");
	@Override
	public List<Map<String, Object>> showEmployees(Map<String, Object> param,PageBean pageBean) throws ServiceException {
			List<Map<String, Object>> list=null;
			try {
				list=employeeDao.queryEmployeesWithLike(param,pageBean);
			} catch (DaoException e) {
				e.printStackTrace();
				throw new ServiceException();
			}
			return list;
	}
	@Override
	public boolean changeStatus(Integer EMPLOYEE_ID, Map<String, Object> param) throws ServiceException {
		boolean flag=false;
		try {
			flag=employeeDao.updateEmployeeById(EMPLOYEE_ID, param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public boolean addEmployee(Map<String, Object> employee) throws ServiceException {
		boolean flag=false;
		Integer id=null;
		try {
			id=employeeDao.insertEmployee(employee);
			flag=employeeDao.insertLoginByid(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public Map<String, Object> employeeInfo(Integer EMPLOYEE_ID) throws ServiceException {
		Map<String, Object> employee=null;
		try {
			employee=employeeDao.queryEmployeeById(EMPLOYEE_ID);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return employee;
	}
	@Override
	public boolean updateEmployeeInfo(Integer EMPLOYEE_ID, String EMPLOYEE_NAME, Map<String, Object> param)
			throws ServiceException {
		boolean flag=false;
		try {
			flag=employeeDao.updateEmployeeByIdAndName(EMPLOYEE_ID, EMPLOYEE_NAME, param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public int countForEmployee() throws ServiceException {
		int count=0;
		try {
			count=employeeDao.employeeCount();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public int countForEmployeeWithLike(Map<String, Object> param) throws ServiceException {
		int count=0;
		try {
			count=employeeDao.employeeCountWithLike(param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public Map<String, Object> login(Integer EMPLOYEE_ID, String password) throws ServiceException {
		Map<String, Object> user=null;
		try {
			user=employeeDao.queryEmployeeByIdAndPassword(EMPLOYEE_ID, password);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return user;
	}
	@Override
	public boolean updatePassword(Integer EMPLOYEE_ID, String oldpassword, String newpassword) throws ServiceException {
		boolean flag=false;
		try {
			flag=employeeDao.updateEmployeeLoginPasswordByIdAndPassword(EMPLOYEE_ID, oldpassword, newpassword);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
