package net.wanho.dao;

import java.util.List;
import java.util.Map;

import net.wanho.exception.DaoException;
import net.wanho.util.PageBean;

public interface MenuDao {
	/**
	 * 
	 * @param param 参数 规定的条件查询
	 * @param pageBean 分页对象
	 * @return 所有菜单的伪表
	 * @throws DaoException
	 */
	public List<Map<String, Object>> queryMenusWithLikeByPage(Map<String, Object> param, PageBean pageBean) throws DaoException;
	/**
	 * 
	 * @param param 参数 规定条件查询
	 * @return 根据条件查询的所有条目数
	 * @throws DaoException
	 */
	public int menuCountWithLike(Map<String, Object> param) throws DaoException;
	/**
	 * 
	 * @return 所有的menu条目数
	 * @throws DaoException
	 */
	public int menuCount() throws DaoException;
	/**
	 * 
	 * @return 插入结果
	 * @throws DaoException
	 */
	public boolean insertMenu() throws DaoException;
	
	public List<Map<String, Object>> queryMenuWithPosition(Integer positionId) throws DaoException;
}
