package net.wanho.dao;

import java.util.List;
import java.util.Map;

import net.wanho.exception.DaoException;
import net.wanho.util.PageBean;

public interface PositionDao {
	public List<Map<String, Object>> queryAllPosition() throws DaoException;
	public List<Map<String, Object>> queryPositionsWithLike(Map<String, Object> param, PageBean pageBean) throws DaoException;
	public int positionCountWithLike(Map<String, Object> param) throws DaoException;
	public int positionCount() throws DaoException;
	public boolean insertPosition(String name, String level) throws DaoException;
	public Map<String, Object> queryPositionByName(String name) throws DaoException;
	public Map<String, Object> queryPositionById(Integer id) throws DaoException;
	public boolean updatePositionById(Integer id, Map<String, Object> param) throws DaoException;
	public boolean deletePositionById(Integer id) throws DaoException;
	public List<Map<String, Object>> queryPositionByIdWithMenu(Integer id) throws DaoException;
}
