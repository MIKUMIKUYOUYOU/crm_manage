package net.wanho.service;

import java.util.List;
import java.util.Map;

import net.wanho.exception.ServiceException;
import net.wanho.util.PageBean;

public interface MenuService {
	public List<Map<String, Object>> showMenusWithLikeByPage(Map<String, Object> param, PageBean pageBean) throws ServiceException ;
	public int countForMenuWithLike(Map<String, Object> param) throws ServiceException;
	public int countForMenu() throws ServiceException;
	public List<Map<String, Object>> showUserMenus(Integer positionId) throws ServiceException;
}
