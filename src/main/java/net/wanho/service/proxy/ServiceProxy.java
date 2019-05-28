package net.wanho.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import net.wanho.util.JDBCUtil;

public class ServiceProxy {

	public static Object getServiceProxy(final Object target) {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Connection conn = JDBCUtil.getConnection();
						try {
							// 事务的原子性 一致性
							// 设置手动提交
							conn.setAutoCommit(false);

							// 执行业务
							Object obj = method.invoke(target, args);

							// 提交
							conn.commit();
							return obj;
						} catch (Exception e) {
							System.out.println("已回滚");
							e.printStackTrace();
							// 业务出错 回滚
							conn.rollback();
							return null;
						} finally {
							conn.close();
						}
					}
				});
	}
}
