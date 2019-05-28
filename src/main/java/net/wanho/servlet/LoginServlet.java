package net.wanho.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wanho.exception.ServiceException;
import net.wanho.service.EmployeeService;
import net.wanho.util.ObjectFactory;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService = (EmployeeService) ObjectFactory.getObject("EmployeeService");

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("Method");
		if ("LOGIN".equalsIgnoreCase(method)) {
			login(request, response);
			return;
		}
		if ("UPDATE".equalsIgnoreCase(method)) {
			update(request, response);
			return;
		}
		if ("LOGOUT".equalsIgnoreCase(method)) {
			logout(request, response);
			return;
		}
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("user");
		response.getWriter().write("0");
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//EMPLOYEE_ID OLD_EMP_PASSWORD EMP_PASSWORD
		Integer EMPLOYEE_ID=Integer.parseInt(request.getParameter("EMPLOYEE_ID"));
		String oldpassword=request.getParameter("OLD_EMP_PASSWORD");
		String newpassword=request.getParameter("EMP_PASSWORD");
		boolean flag=false;
		try {
			flag=employeeService.updatePassword(EMPLOYEE_ID, oldpassword, newpassword);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		if(flag) {
			//0为成功
			response.getWriter().write("0");
			return;
		}else {
			//1为失败
			response.getWriter().write("1");
			return;
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> user = null;
		try {
			user = employeeService.login(Integer.parseInt(request.getParameter("EMPLOYEE_ID")),
					request.getParameter("EMP_PASSWORD"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user == null) {
			response.getWriter().write("1");
			return;
		}
		if (user.get("EMP_PASSWORD").equals("123456")) {
			response.getWriter().write("2");
			return;
		}
		request.getSession().setAttribute("user", user);
		
		response.getWriter().write("0");
	}

}
