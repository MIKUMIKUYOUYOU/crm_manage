package net.wanho.dao.impl;

import net.wanho.dao.PositionMenuRelationsDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.DataException;
import net.wanho.util.JDBCTemplateUpdateTo;

public class PositionMenuRelationsDaoImpl implements PositionMenuRelationsDao {
	
	@Override
	public boolean insertPositionMenuRelations(Integer POSITION_ID, Integer MENU_ID) throws DaoException {
		final String INSERT_INTO_POSITION_MENU_RELATIONS_SQL="INSERT INTO position_menu_relations(POSITION_ID,MENU_ID) VALUES(?,?)";
		boolean flag=false;
		try {
			flag=JDBCTemplateUpdateTo.update(INSERT_INTO_POSITION_MENU_RELATIONS_SQL, POSITION_ID,MENU_ID);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

	@Override
	public boolean deleteByPositionId(Integer POSITION_ID) throws DaoException {
		final String DELETE_POSOTION_MENU_RELATIONS_SQL="DELETE FROM position_menu_relations WHERE POSITION_ID=?";
		boolean flag=false;
		try {
			flag=JDBCTemplateUpdateTo.update(DELETE_POSOTION_MENU_RELATIONS_SQL, POSITION_ID);
		} catch (DataException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return flag;
	}

}
