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

import net.wanho.exception.ServiceException;
import net.wanho.service.EmployeeService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;


@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
	private PageBean pageBean=new PageBean();
	private static final long serialVersionUID = 1L;
	private EmployeeService employeeService=(EmployeeService) ObjectFactory.getObject("EmployeeService");
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//一键转发 缩减地址栏
		request.getRequestDispatcher("page/systemSettings/employee/employee.html").forward(request, response);
		return;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//请求一律用post请求
		String method=request.getParameter("Method");
		//初始化
		if("INIT".equalsIgnoreCase(method)) {
			showEmployee(request,response);
			return;
		}
		//更新状态
		if("UPDATEStatus".equalsIgnoreCase(method)) {
			UPDATEStatus(request,response);
			return;
		}
		//添加
		if("ADD".equalsIgnoreCase(method)) {
			addEmployee(request,response);
			return;
		}
		//更新员工信息
		if("UPDATE".equalsIgnoreCase(method)) {
			employeeUpdate(request, response);
			return;
		}
		//QE为query employee的简写 条件模糊查询
		if("QE".equals(method)) {
			employeeInfo(request,response);
			return;
		}
	}
	protected void showEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
			Map<String, Object> param=new HashMap<String, Object>();
			String EMPLOYEE_ID_Str=request.getParameter("EMPLOYEE_ID");
			if(EMPLOYEE_ID_Str!=null&&!"".equals(EMPLOYEE_ID_Str)) {
				try {
					param.put("EMPLOYEE_ID", Integer.parseInt(EMPLOYEE_ID_Str));
				} catch (Exception e) {
					//2为数字错误
					response.getWriter().write("2");
					return;
				}
			}
			param.put("EMPLOYEE_NAME", request.getParameter("EMPLOYEE_NAME"));
			List<Map<String, Object>> employees=employeeService.showEmployees(param,pageBean);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(employees));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			//1为出错
			response.getWriter().write("1");
			return;
		}
	}
	
	protected void UPDATEStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> param=new HashMap<String, Object>();
		String changeSTATUS="1";
		if("1".equals(request.getParameter("STATUS"))) {
			changeSTATUS="0";
		}
		param.put("STATUS", changeSTATUS);
		boolean flag=false;
		try {
			flag=employeeService.changeStatus(Integer.parseInt(request.getParameter("EMPLOYEE_ID")), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(flag) {
			//0为成功
			response.getWriter().write("0");
			return;
		}
		//1为失败
		response.getWriter().write("1");
		return;
	}
	
	protected void addEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> employee=new HashMap<String, Object>();
		try {
			employee.put("EMPLOYEE_NAME", (String)request.getParameter("EMPLOYEE_NAME"));
			employee.put("DEPARTMENT_ID", Integer.parseInt(request.getParameter("DEPARTMENT_ID")));
			employee.put("POSITION_ID", Integer.parseInt(request.getParameter("POSITION_ID")));
			employee.put("PHONE", (String)request.getParameter("PHONE"));
			employee.put("EMAIL", (String)request.getParameter("EMAIL"));
			employee.put("PARENT_EMPLOYEE_ID", Integer.parseInt(request.getParameter("PARENT_EMPLOYEE_ID")));
		} catch (Exception e) {
			//1为表单参数错误
			response.getWriter().write("1");
			return;
		}
		boolean flag=false;
		try {
			flag=employeeService.addEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!flag) {
			//2为插入数据库错误
			response.getWriter().write("2");
			return;
		}
		//0为成功
		response.getWriter().write("0");
		return;
	}

	protected void employeeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String EMPLOYEE_ID_Str=request.getParameter("EMPLOYEE_ID");
		Integer EMPLOYEE_ID=null;
		if(EMPLOYEE_ID_Str!=null) {
			try {
			EMPLOYEE_ID =Integer.parseInt(EMPLOYEE_ID_Str);
			} catch (Exception e) {
				//2为数字错误
				response.getWriter().write("2");
				return;
			}
		}
		try {
			Map<String, Object> employee=employeeService.employeeInfo(EMPLOYEE_ID);
			if(employee==null) {
				//3为未查到
				response.getWriter().write("3");
				return;
			}
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(employee));
		} catch (Exception e) {
			e.printStackTrace();
			//1为执行查询错误
			response.getWriter().write("1");
			return;
		}
	}
	
	protected void employeeUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> param=new HashMap<String, Object>();
		
		try {
			param.put("EMPLOYEE_NAME", (String)request.getParameter("EMPLOYEE_NAME"));
			param.put("DEPARTMENT_ID", Integer.parseInt(request.getParameter("DEPARTMENT_ID")));
			param.put("POSITION_ID", Integer.parseInt(request.getParameter("POSITION_ID")));
			param.put("PHONE", (String)request.getParameter("PHONE"));
			param.put("EMAIL", (String)request.getParameter("EMAIL"));
			param.put("PARENT_EMPLOYEE_ID", Integer.parseInt(request.getParameter("PARENT_EMPLOYEE_ID")));
		} catch (Exception e) {
			//1为表单参数错误
			response.getWriter().write("1");
			return;
		}
		boolean flag=false;
		try {
			flag=employeeService.updateEmployeeInfo(Integer.parseInt(request.getParameter("updateEMPID")), request.getParameter("updateEMPNAME"), param);
		} catch (Exception e) {

		}
		if(flag) {
			response.getWriter().write("0");
			return;
		}else {
			response.getWriter().write("2");
			return;
		}
		
	}
}
