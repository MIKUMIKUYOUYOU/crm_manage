package net.wanho.servlet;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wanho.service.PositionMenuRelationsService;
import net.wanho.util.ObjectFactory;


@WebServlet("/positionMenuRelations")
public class PositionMenuRelationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PositionMenuRelationsService positionMenuRelationsService=(PositionMenuRelationsService) ObjectFactory.getObject("PositionMenuRelationsService");
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Integer> param=new ArrayList<Integer>();
		if(!"".equals(request.getParameter("linkData"))) {
			String[] MENU_ID_Arr=request.getParameter("linkData").split(",");
			for (String MENU_ID : MENU_ID_Arr) {
				param.add(Integer.parseInt(MENU_ID.split(":")[1]));
			}
		}
		boolean flag=false;
		try {
			flag=positionMenuRelationsService.changeThePermissions(Integer.parseInt(request.getParameter("POSITION_ID")), param);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		if(flag) {
			//成功
			response.getWriter().write("0");
			return;
		}else {
			//失败
			response.getWriter().write("1");
			return;
		}
	}

}
