package net.wanho.service.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.PositionDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.ServiceException;
import net.wanho.service.PositionService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;

public class PositionServiceImpl implements PositionService {
	private PositionDao positionDao=(PositionDao) ObjectFactory.getObject("PositionDao");
	@Override
	public List<Map<String, Object>> showAll() throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			list=positionDao.queryAllPosition();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}
	@Override
	public List<Map<String, Object>> showPositionsByPageWithLike(Map<String, Object> param, PageBean pageBean)
			throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			list=positionDao.queryPositionsWithLike(param, pageBean);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}
	@Override
	public int countForPositionWithLike(Map<String, Object> param) throws ServiceException {
		int count=0;
		try {
			count=positionDao.positionCountWithLike(param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public int countForPosition() throws ServiceException {
		int count=0;
		try {
			count=positionDao.positionCount();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public boolean isExist(String name) throws ServiceException {
		//存在则为true
		boolean flag=false;
		try {
			if(positionDao.queryPositionByName(name)!=null) {
				flag=true;
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public boolean addPosition(String name, String level) throws ServiceException {
		boolean flag=false;
		try {
			flag=positionDao.insertPosition(name, level);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public boolean updatePosition(Integer id, Map<String, Object> param) throws ServiceException {
		boolean flag=false;
		try {
			flag=positionDao.updatePositionById(id, param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return flag;
	}
	@Override
	public List<Map<String, Object>> showZtree(Integer id) throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			list=positionDao.queryPositionByIdWithMenu(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}

}
