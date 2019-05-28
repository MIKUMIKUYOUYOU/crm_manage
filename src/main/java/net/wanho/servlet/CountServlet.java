package net.wanho.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wanho.service.EmployeeService;
import net.wanho.service.MenuService;
import net.wanho.service.PositionService;
import net.wanho.util.ObjectFactory;

@WebServlet("/count")
public class CountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService = (EmployeeService) ObjectFactory.getObject("EmployeeService");
	private PositionService positionService = (PositionService) ObjectFactory.getObject("PositionService");
	private MenuService menuService = (MenuService) ObjectFactory.getObject("MenuService");
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("Method");
		if ("EmpPage".equalsIgnoreCase(method)) {
			countEmp(request, response);
			return;
		}
		if ("EmpPageWK".equalsIgnoreCase(method)) {
			countEmpWithLike(request, response);
			return;
		}
		if ("PosPage".equalsIgnoreCase(method)) {
			countPosition(request, response);
			return;
		}
		if ("PosPageWK".equalsIgnoreCase(method)) {
			countPositionWithLike(request, response);
			return;
		}
		if ("MenuPage".equalsIgnoreCase(method)) {
			countMenu(request, response);
			return;
		}
		if ("MenuPageWK".equalsIgnoreCase(method)) {
			countMenuWithLike(request, response);
			return;
		}
	}

	protected void countMenuWithLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		String MENU_NAME = request.getParameter("MENU_NAME");
		if (MENU_NAME != null && !"".equals(MENU_NAME)) {
			param.put("MENU_NAME", MENU_NAME);
		}
		try {
			total=menuService.countForMenuWithLike(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		
	}

	protected void countMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		try {
			total=menuService.countForMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		return;
	}

	protected void countEmp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		try {
			total = employeeService.countForEmployee();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		return;
	}

	protected void countEmpWithLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			String EMPLOYEE_ID_Str = request.getParameter("EMPLOYEE_ID");
			if (EMPLOYEE_ID_Str != null && !"".equals(EMPLOYEE_ID_Str)) {
				try {
					param.put("EMPLOYEE_ID", Integer.parseInt(EMPLOYEE_ID_Str));
				} catch (Exception e) {
					// 2为数字错误
					response.getWriter().write("2");
					return;
				}
			}
			param.put("EMPLOYEE_NAME", request.getParameter("EMPLOYEE_NAME"));
			total = employeeService.countForEmployeeWithLike(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		return;
	}
	
	protected void countPosition(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		try {
			total = positionService.countForPosition();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		return;
	}
	protected void countPositionWithLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int total = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		String POSITION_NAME = request.getParameter("POSITION_NAME");
		if (POSITION_NAME != null && !"".equals(POSITION_NAME)) {
			param.put("POSITION_NAME", POSITION_NAME);
		}
		try {
			total=positionService.countForPositionWithLike(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("" + total);
		return;
	}
}
