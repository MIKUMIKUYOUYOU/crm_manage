package net.wanho.dao.impl;

import java.util.List;
import java.util.Map;

import net.wanho.dao.PositionDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.DataException;
import net.wanho.util.JDBCTemplateUpdateTo;
import net.wanho.util.JDBCUtil;
import net.wanho.util.PageBean;

public class PositionDaoImpl implements PositionDao {

	@Override
	public List<Map<String, Object>> queryAllPosition() throws DaoException {
		final String SELECT_ALL_DEPARTMENT_SQL="SELECT POSITION_ID,POSITION_NAME,POSITION_LEVEL FROM emm_position";
		List<Map<String, Object>> list=null;
		try {
			list=JDBCTemplateUpdateTo.queryForList(SELECT_ALL_DEPARTMENT_SQL);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> queryPositionsWithLike(Map<String, Object> param, PageBean pageBean)
			throws DaoException {
		StringBuffer SELECT_POSITION_SQL = new StringBuffer("SELECT POSITION_ID,POSITION_NAME,POSITION_LEVEL,CREATE_TIME,UPDATE_TIME FROM emm_position WHERE 1=1");
		String positionName = null;
		if (param != null) {
			positionName = (String) param.get("POSITION_NAME");
		}
		List<Map<String, Object>> list = null;
		if (positionName != null) {
			SELECT_POSITION_SQL.append(" AND POSITION_NAME LIKE '%" + positionName + "%'");
		}
		SELECT_POSITION_SQL.append(" ORDER BY POSITION_ID");
		SELECT_POSITION_SQL.append(" LIMIT "+pageBean.getStart()+","+pageBean.getPageSize());
		try {
			list = JDBCTemplateUpdateTo.queryForList(SELECT_POSITION_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

	@Override
	public int positionCountWithLike(Map<String, Object> param) throws DaoException {
		StringBuffer SELECT_POSITION_COUNT_SQL= new StringBuffer("SELECT COUNT(1) FROM emm_position WHERE 1=1");
		String positionName = null;
		if (param != null) {
			positionName = (String) param.get("POSITION_NAME");
		}
		if (positionName != null) {
			SELECT_POSITION_COUNT_SQL.append(" AND POSITION_NAME LIKE '%" + positionName + "%'");
		}
		int count=0;
		try {
			count=JDBCTemplateUpdateTo.queryForInt(SELECT_POSITION_COUNT_SQL.toString());
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public int positionCount() throws DaoException {
		final String SELECT_POSITION_COUNT_SQL="SELECT COUNT(1) FROM emm_position";
		int count=0;
		try {
			count=JDBCTemplateUpdateTo.queryForInt(SELECT_POSITION_COUNT_SQL);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return count;
	}

	@Override
	public boolean insertPosition(String name,String level) throws DaoException {
		final String INSERT_POSITION_SQL="INSERT INTO emm_position(POSITION_NAME,POSITION_LEVEL) VALUES(?,?)";
		boolean flag=false;
		try {
			flag=JDBCTemplateUpdateTo.update(INSERT_POSITION_SQL, name,level);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

	@Override
	public Map<String, Object> queryPositionByName(String name) throws DaoException {
		Map<String, Object> positionName=null;
		final String SELECT_POSITION_BY_NAME_SQL="SELECT POSITION_NAME FROM emm_position WHERE POSITION_NAME=?";
		try {
			positionName=JDBCTemplateUpdateTo.queryForObject(SELECT_POSITION_BY_NAME_SQL, name);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return positionName;
	}

	@Override
	public Map<String, Object> queryPositionById(Integer id) throws DaoException {
		return null;
	}

	@Override
	public boolean updatePositionById(Integer id,Map<String, Object> param) throws DaoException {
		StringBuffer UPDATE_POSITION_BY_ID_SQL=JDBCUtil.updateStringBuffer("emm_position", param);
		UPDATE_POSITION_BY_ID_SQL.append(" WHERE POSITION_ID=?");
		boolean flag=false;
		try {
			flag=JDBCTemplateUpdateTo.update(UPDATE_POSITION_BY_ID_SQL.toString(), id);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

	@Override
	public boolean deletePositionById(Integer id) throws DaoException {
		return false;
	}

	@Override
	public List<Map<String, Object>> queryPositionByIdWithMenu(Integer id) throws DaoException {
		final String SELECT_POSITION_MENU_RELATIONS_SQL="SELECT t.POSITION_ID,t.POSITION_NAME,m.MENU_ID,m.MENU_NAME,m.PARENT_MENU_ID FROM (SELECT ep.POSITION_ID,ep.POSITION_NAME,pm.MENU_ID FROM emm_position ep JOIN position_menu_relations pm ON ep.POSITION_ID=pm.POSITION_ID WHERE ep.POSITION_ID=?) t RIGHT JOIN menu m ON m.MENU_ID=t.MENU_ID ";
		List<Map<String, Object>> list=null;
		try {
			list=JDBCTemplateUpdateTo.queryForList(SELECT_POSITION_MENU_RELATIONS_SQL, id);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return list;
	}

}
