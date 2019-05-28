package net.wanho.service.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.DepartmentDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.ServiceException;
import net.wanho.service.DepartmentService;
import net.wanho.util.ObjectFactory;

public class DepartmentServiceImpl implements DepartmentService {
	private DepartmentDao departmentDao= (DepartmentDao) ObjectFactory.getObject("DepartmentDao");
	@Override
	public List<Map<String, Object>> showAll() throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			
			list=departmentDao.queryAllDepartment();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}

}
