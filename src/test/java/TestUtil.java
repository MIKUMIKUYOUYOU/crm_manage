import java.util.HashMap;
import java.util.Map;

import net.wanho.util.JDBCUtil;
import org.junit.Test;
import net.wanho.exception.DaoException;


public class TestUtil {

	@Test
	public void fn() throws DaoException {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("EMPLOYEE_ID", 1);
		param.put("EMPLOYEE_NAME", "aa");
		StringBuffer UPDATE_EMPLOYEE_SQL= JDBCUtil.updateStringBuffer("employee", param);
		
		System.out.println(UPDATE_EMPLOYEE_SQL.toString());
	}
}
