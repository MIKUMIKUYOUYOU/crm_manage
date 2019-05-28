package net.wanho.service;

import java.util.List;
import java.util.Map;

import net.wanho.exception.ServiceException;

public interface DepartmentService {
	public List<Map<String, Object>> showAll() throws ServiceException;
}
