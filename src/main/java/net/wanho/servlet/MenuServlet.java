package net.wanho.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;

import net.wanho.service.MenuService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PageBean pageBean = new PageBean();
	private MenuService menuService = (MenuService) ObjectFactory.getObject("MenuService");

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 一键转发 缩减地址栏
		request.getRequestDispatcher("page/systemSettings/menu/menu.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("Method");
		// 初始化菜单表格
		if ("INITPAGE".equalsIgnoreCase(method)) {
			this.showMenuByPage(request, response);
		}
		//初始化用户菜单
		if ("INITMENU".equalsIgnoreCase(method)) {
			this.initUserMenu(request, response);
		}
	}

	private void initUserMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> user=(Map<String, Object>) request.getSession().getAttribute("user");
		try {
			List<Map<String, Object>> menuList=menuService.showUserMenus((int)user.get("POSITION_ID"));
			
			if(menuList.isEmpty()) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("userName",user.get("EMPLOYEE_NAME"));
				menuList.add(map);
			}else {
				menuList.get(0).put("userName", user.get("EMPLOYEE_NAME"));
			}
			
			response.getWriter().write(JSONObject.toJSONString(menuList));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1为失败
		response.getWriter().write("1");
		return;
	}

	protected void showMenuByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("page") != null) {
			try {
				int page = Integer.parseInt(request.getParameter("page"));
				int pageSize = Integer.parseInt(request.getParameter("pageSize"));
				pageBean.setPage(page);
				pageBean.setPageSize(pageSize);
			} catch (Exception e) {
				pageBean.setPage(1);
				pageBean.setPageSize(5);
			}
		} else {
			pageBean.setPage(1);
			pageBean.setPageSize(5);
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			String MENU_NAME = request.getParameter("MENU_NAME");
			if (MENU_NAME != null && !"".equals(MENU_NAME)) {
				param.put("MENU_NAME", MENU_NAME);
			}
			List<Map<String, Object>> menus = menuService.showMenusWithLikeByPage(param, pageBean);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(menus));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// 1为查询出错
			response.getWriter().write("1");
			return;
		}
	}
}
