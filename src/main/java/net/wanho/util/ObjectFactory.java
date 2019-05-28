package net.wanho.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.wanho.service.proxy.ServiceProxy;

/**
 * <工厂类>
 * 
 * @author zj
 * @version [V1.0.0,2017-4-10]
 * 
 */
public class ObjectFactory {

	private static Map<String, Object> objects = new HashMap<String, Object>();
	static {
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new InputStreamReader(ObjectFactory.class
					.getClassLoader().getResourceAsStream("bean.properties")));
			String s = null;
			while ((s = br.readLine()) != null) {
				if(!"".equals(s) && !"#".equals(s.charAt(0))){
					String[] entry = s.split("=");
					objects.put(entry[0], Class.forName(entry[1]).newInstance());
				}else{
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("ObjectFactory初始化错误" + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Object getObject(String key) {
		final Object obj = objects.get(key);
		if (key.contains("service") || key.contains("Service")) {
			return ServiceProxy.getServiceProxy(obj);
		}else{
			return obj;
		}
	}
}
