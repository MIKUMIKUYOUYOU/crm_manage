package net.wanho.dao.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.MenuDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.DataException;
import net.wanho.util.JDBCTemplateUpdateTo;
import net.wanho.util.PageBean;

public class MenuDaoImpl implements MenuDao {

	@Override
	public List<Map<String, Object>> queryMenusWithLikeByPage(Map<String, Object> param, PageBean pageBean)
			throws DaoException {
		StringBuffer SELECT_MENU_WITH_LIKE_BY_PAGE_SQL=new StringBuffer("SELECT m1.MENU_ID,m1.MENU_NAME,m1.PICTURES,m1.MENU_URL,m2.MENU_NAME 'PARENT_NAME',m1.CREATE_TIME,m1.UPDATE_TIME FROM menu m1 LEFT JOIN menu m2 ON m1.PARENT_MENU_ID=m2.MENU_ID WHERE 1=1 ");
		String nemuName = null;
		if (param != null) {
			nemuName = (String) param.get("MENU_NAME");
		}
		List<Map<String, Object>> list = null;
		if (nemuName != null) {
			SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.append(" AND m1.MENU_NAME LIKE '%" + nemuName + "%'");
		}
		SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.append(" ORDER BY m2.MENU_NAME");
		SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.append(" LIMIT "+pageBean.getStart()+","+pageBean.getPageSize());
		try {
			list = JDBCTemplateUpdateTo.queryForList(SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

	@Override
	public int menuCountWithLike(Map<String, Object> param) throws DaoException {
		StringBuffer SELECT_MENU_WITH_LIKE_BY_PAGE_SQL=new StringBuffer("SELECT COUNT(1) FROM menu m1 LEFT JOIN menu m2 ON m1.PARENT_MENU_ID=m2.MENU_ID WHERE 1=1 ");
		String nemuName = null;
		if (param != null) {
			nemuName = (String) param.get("MENU_NAME");
		}
		if (nemuName != null) {
			SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.append(" AND m1.MENU_NAME LIKE '%" + nemuName + "%'");
		}
		int count=0;
		try {
			count=JDBCTemplateUpdateTo.queryForInt(SELECT_MENU_WITH_LIKE_BY_PAGE_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public int menuCount() throws DaoException {
		final String SELECT_MENU_COUNT_SQL="SELECT COUNT(1) FROM menu m1 LEFT JOIN menu m2 ON m1.PARENT_MENU_ID=m2.MENU_ID";
		int count=0;
		try {
			count=JDBCTemplateUpdateTo.queryForInt(SELECT_MENU_COUNT_SQL);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public boolean insertMenu() throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> queryMenuWithPosition(Integer positionId) throws DaoException {
		final String SELECT_MENU_POSITION_RELATIONS_SQL="SELECT t.POSITION_ID,t.POSITION_NAME,m.MENU_ID,m.MENU_NAME,m.PARENT_MENU_ID,m.PICTURES,m.MENU_URL FROM (SELECT ep.POSITION_ID,ep.POSITION_NAME,pm.MENU_ID FROM emm_position ep JOIN position_menu_relations pm ON ep.POSITION_ID=pm.POSITION_ID WHERE ep.POSITION_ID=?) t JOIN menu m ON m.MENU_ID=t.MENU_ID ORDER BY m.MENU_ID DESC";
		List<Map<String, Object>> list=null;
		try {
			list=JDBCTemplateUpdateTo.queryForList(SELECT_MENU_POSITION_RELATIONS_SQL, positionId);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

	
}
