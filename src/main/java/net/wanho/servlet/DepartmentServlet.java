package net.wanho.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import net.wanho.exception.ServiceException;
import net.wanho.service.DepartmentService;
import net.wanho.util.ObjectFactory;


@WebServlet("/department")
public class DepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DepartmentService departmentService=(DepartmentService) ObjectFactory.getObject("DepartmentService");
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method=request.getParameter("Method");
		if("INIT".equalsIgnoreCase(method)) {
			this.showAll(request, response);
		}
	}
	protected void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			List<Map<String, Object>> departments=departmentService.showAll();
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(departments));
			return;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
