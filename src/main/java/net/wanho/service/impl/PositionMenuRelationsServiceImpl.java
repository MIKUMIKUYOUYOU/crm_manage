package net.wanho.service.impl;

import java.util.List;

import net.wanho.dao.PositionMenuRelationsDao;
import net.wanho.exception.DaoException;
import net.wanho.exception.ServiceException;
import net.wanho.service.PositionMenuRelationsService;
import net.wanho.util.ObjectFactory;

public class PositionMenuRelationsServiceImpl implements PositionMenuRelationsService {
	private PositionMenuRelationsDao positionMenuRelationsDao = (PositionMenuRelationsDao) ObjectFactory
			.getObject("PositionMenuRelationsDao");

	@Override
	public boolean changeThePermissions(Integer POSITION_ID, List<Integer> MENU_ID_List) throws ServiceException {
		boolean flag = false;
		if (!MENU_ID_List.isEmpty()) {
			try {
				flag = positionMenuRelationsDao.deleteByPositionId(POSITION_ID);
				for (Integer MENU_ID : MENU_ID_List) {
					flag = positionMenuRelationsDao.insertPositionMenuRelations(POSITION_ID, MENU_ID);
				}
			} catch (Exception e) {
				e.printStackTrace();
				flag=false;
				throw new ServiceException();
			}
		} else {
			try {
				flag=positionMenuRelationsDao.deleteByPositionId(POSITION_ID);
			} catch (Exception e) {
				e.printStackTrace();
				flag=false;
				throw new ServiceException();
			}
		}
		return flag;
	}

}
