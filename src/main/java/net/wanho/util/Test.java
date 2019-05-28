package net.wanho.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.wanho.dao.PositionDao;
import net.wanho.dao.impl.PositionDaoImpl;
import net.wanho.exception.DaoException;

public class Test {
	
	public static void main(String[] args) {
		PositionDao positionDao=new PositionDaoImpl();
		try {
			Set<Map.Entry<String,Object>> positions=positionDao.queryPositionByName("鼓励师").entrySet();
			for (Map.Entry<String,Object> position: positions) {
				System.out.println(position);

			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

}
