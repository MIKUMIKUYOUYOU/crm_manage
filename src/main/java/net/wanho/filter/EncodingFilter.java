package net.wanho.filter;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" }, initParams = { @WebInitParam(name = "charset", value = "utf-8") })
public class EncodingFilter implements Filter {
	private FilterConfig filterConfig;

	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		// 1.强转
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setContentType("text/html;charset=utf-8");
		
		// 2.放行
		String encoding = filterConfig.getInitParameter("charset");
		chain.doFilter(new MyRequest(request, encoding), response);
	}

	public void destroy() {
	}

}

class MyRequest extends HttpServletRequestWrapper {
	private HttpServletRequest request;
	private boolean flag = true;
	private String encoding;

	public MyRequest(HttpServletRequest request, String encoding) {
		super(request);
		this.request = request;
		this.encoding = encoding;
	}

	@Override
	public String getParameter(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		String[] values = getParameterValues(name);
		if (values == null || values.length == 0) {
			return null;
		}

		return values[0];
	}

	@Override
	/**
	 * hobby=[eat,drink]
	 */
	public String[] getParameterValues(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		Map<String, String[]> map = getParameterMap();
		if (map == null || map.size() == 0) {
			return null;
		}

		return map.get(name);
	}

	@Override
	/**
	 * map{ username=[tom],password=[123],hobby=[eat,drink]}
	 */
	public Map<String, String[]> getParameterMap() {

		/**
		 * 首先判断请求方式 若为post request.setchar...(utf-8) 若为get 将map中的值遍历编码就可以了
		 */
		String method = request.getMethod();
		if ("post".equalsIgnoreCase(method)) {
			try {
				request.setCharacterEncoding(encoding);
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if ("get".equalsIgnoreCase(method)) {
			Map<String, String[]> map = request.getParameterMap();
			if (flag) {
				for (String key : map.keySet()) {
					String[] arr = map.get(key);
					// 继续遍历数组
					for (int i = 0; i < arr.length; i++) {
						// 编码
						try {
							arr[i] = new String(arr[i].getBytes("iso8859-1"), encoding);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				flag = false;
			}
			// 需要遍历map 修改value的每一个数据的编码

			return map;
		}

		return super.getParameterMap();
	}

}