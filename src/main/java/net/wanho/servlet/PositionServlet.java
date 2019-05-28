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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.wanho.exception.ServiceException;
import net.wanho.service.PositionService;
import net.wanho.util.ObjectFactory;
import net.wanho.util.PageBean;

@WebServlet("/position")
public class PositionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PageBean pageBean = new PageBean();
	private PositionService positionService = (PositionService) ObjectFactory.getObject("PositionService");

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("page/systemSettings/position/position.html").forward(request, response);
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("Method");
		// 用于查询所有 如 被用于添加员工的职位查询
		if ("INIT".equalsIgnoreCase(method)) {
			this.showAll(request, response);
		}
		// 初始化职位表格
		if ("INITPAGE".equalsIgnoreCase(method)) {
			this.showPositionByPage(request, response);
		}
		// 添加职位
		if ("ADD".equalsIgnoreCase(method)) {
			this.addPosition(request, response);
		}
		// 是否存在相同的职位
		if ("SAME".equalsIgnoreCase(method)) {
			this.samePosition(request, response);
		}
		// 更新职位
		if ("UPDATE".equalsIgnoreCase(method)) {
			this.updatePosition(request, response);
		}
		// 获取ztree需要的数据
		if ("ZTREE".equalsIgnoreCase(method)) {
			this.ztree(request, response);
		}
		// 删除职位
		if ("DELETE".equalsIgnoreCase(method)) {
			this.deletePosition(request, response);
		}
	}

	protected void ztree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String POSITION_ID_Str=request.getParameter("POSITION_ID");
		Integer POSITION_ID=null;
		try {
			POSITION_ID=Integer.parseInt(POSITION_ID_Str);
		} catch (Exception e) {
		}
		try {
			if(POSITION_ID==null) {
				//1为空ID
				response.getWriter().write("1");
				return;
			}
			List<Map<String, Object>> ztree=positionService.showZtree(POSITION_ID);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(ztree));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			List<Map<String, Object>> departments = positionService.showAll();
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(departments));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void showPositionByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
			String POSITION_NAME = request.getParameter("POSITION_NAME");
			if (POSITION_NAME != null && !"".equals(POSITION_NAME)) {
				param.put("POSITION_NAME", POSITION_NAME);
			}
			List<Map<String, Object>> positions = positionService.showPositionsByPageWithLike(param, pageBean);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(positions));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// 1为查询出错
			response.getWriter().write("1");
			return;
		}
	}

	protected void addPosition(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("POSITION_NAME");
		String level = request.getParameter("POSITION_LEVEL");
		// 暂感觉无须判空处理
		boolean flag = false;
		try {
			flag = positionService.addPosition(name, level);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag) {
			// 插入成功
			response.getWriter().write("0");
			return;
		} else {
			// 插入失败
			response.getWriter().write("1");
			return;
		}
	}

	protected void samePosition(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean flag = false;
		String POSITION_NAME = request.getParameter("POSITION_NAME").trim();
		String updateParam = request.getParameter("updateParam").trim();
		if (!"".equals(updateParam)) {
			if (updateParam.equals(POSITION_NAME)) {
				// 更新前与准备更新后的名字相同 直接默认通过验证
				response.getWriter().write("{\"valid\":true}");
				return;
			}
		}
		try {
			if (POSITION_NAME == null) {
				// 不正确的职位名
				response.getWriter().write("{\"valid\":false}");
				return;
			}
			flag = positionService.isExist(POSITION_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			// 查询错误
			response.getWriter().write("{\"valid\":false}");
			return;
		}

		if (flag) {
			// 已有该职位
			response.getWriter().write("{\"valid\":false}");
			return;
		}
		// 无相同职位 可添加
		response.getWriter().write("{\"valid\":true}");
		return;
	}

	protected void updatePosition(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String POSITION_ID_Str = request.getParameter("POSITION_ID");
		Integer id = null;
		try {
			id = Integer.parseInt(POSITION_ID_Str);
		} catch (Exception e) {
			// 2为数字转换错误
			response.getWriter().write("2");
			return;
		}
		String POSITION_NAME = request.getParameter("POSITION_NAME");
		String POSITION_LEVEL = request.getParameter("POSITION_LEVEL");
		Map<String, Object> param = new HashMap<String, Object>();
		// 暂时感觉无需判空 除非表单验证失效不然并不会空
		param.put("POSITION_NAME", POSITION_NAME);
		param.put("POSITION_LEVEL", POSITION_LEVEL);
		boolean flag = false;
		try {
			flag = positionService.updatePosition(id, param);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (flag) {
			// 更新成功
			response.getWriter().write("0");
			return;
		} else {
			// 更新失败
			response.getWriter().write("1");
			return;
		}
	}

	protected void deletePosition(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}
}
