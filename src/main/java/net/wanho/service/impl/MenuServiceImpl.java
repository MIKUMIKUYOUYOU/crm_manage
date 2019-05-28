package net.wanho.service.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.MenuDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.ServiceException;
import net.wanho.service.MenuService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;

public class MenuServiceImpl implements MenuService {
	private MenuDao menuDao=(MenuDao) ObjectFactory.getObject("MenuDao");
	@Override
	public List<Map<String, Object>> showMenusWithLikeByPage(Map<String, Object> param, PageBean pageBean) throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			list=menuDao.queryMenusWithLikeByPage(param, pageBean);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}
	@Override
	public int countForMenuWithLike(Map<String, Object> param) throws ServiceException {
		int count=0;
		try {
			count=menuDao.menuCountWithLike(param);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public int countForMenu() throws ServiceException {
		int count=0;
		try {
			count=menuDao.menuCount();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return count;
	}
	@Override
	public List<Map<String, Object>> showUserMenus(Integer positionId) throws ServiceException {
		List<Map<String, Object>> list=null;
		try {
			list=menuDao.queryMenuWithPosition(positionId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return list;
	}

}
