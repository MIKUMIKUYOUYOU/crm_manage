package net.wanho.service;

import java.util.List;

import net.wanho.exception.ServiceException;

public interface PositionMenuRelationsService {
	public boolean changeThePermissions(Integer POSITION_ID, List<Integer> MENU_ID_List) throws ServiceException;
}
