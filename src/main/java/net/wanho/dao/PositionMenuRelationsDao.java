package net.wanho.dao;

import net.wanho.exception.DaoException;

public interface PositionMenuRelationsDao {
	public boolean insertPositionMenuRelations(Integer POSITION_ID, Integer MENU_ID) throws DaoException;
	public boolean deleteByPositionId(Integer POSITION_ID) throws DaoException;
}
