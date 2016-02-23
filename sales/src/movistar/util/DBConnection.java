package movistar.util;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;



public class DBConnection {
	
	private static DataSource ds = null;
	static {
		try {
			ds = (DataSource)new InitialContext().lookup("java:/comp/env/jdbc/sales");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws Exception{
		return ds.getConnection();
	}

}
