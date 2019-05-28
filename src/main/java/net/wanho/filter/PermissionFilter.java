package net.wanho.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter("/*")
public class PermissionFilter implements Filter {



	public void destroy() {
		
	}


	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) req;
		Map<String, Object> user=(Map<String, Object>) request.getSession().getAttribute("user");
		String uri=request.getRequestURI();
		
		if(uri.contains("login")||uri.contains("updatepassword.html")) {
			chain.doFilter(request, response);
			return;
		}
		if(uri.contains(".css")||uri.contains(".js")||uri.contains(".png")||uri.contains(".jpg")) {
			chain.doFilter(request, response);
			return;
		}
		if(user==null) {
			System.out.println(uri);
			request.getRequestDispatcher(request.getContextPath()+"/404.html").forward(request, response);
			return;
		}
//		System.out.println(uri);
//		System.out.println(uri.contains("index.html"));
//		System.out.println(uri.contains("menu"));
		chain.doFilter(request, response);
		
	}


	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
