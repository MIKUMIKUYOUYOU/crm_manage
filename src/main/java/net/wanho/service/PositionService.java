package net.wanho.service;

import java.util.List;
import java.util.Map;

import net.wanho.exception.ServiceException;
import net.wanho.util.PageBean;

public interface PositionService {
	public List<Map<String, Object>> showAll() throws ServiceException;
	public List<Map<String, Object>> showPositionsByPageWithLike(Map<String, Object> param, PageBean pageBean) throws ServiceException;
	public int countForPositionWithLike(Map<String, Object> param) throws ServiceException;
	public int countForPosition() throws ServiceException;
	public boolean isExist(String name) throws ServiceException;
	public boolean addPosition(String name, String level)throws ServiceException;
	public boolean updatePosition(Integer id, Map<String, Object> param) throws ServiceException;
	public List<Map<String, Object>> showZtree(Integer id) throws ServiceException;
}
