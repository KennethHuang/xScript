package junit.kenh.xscript.database.functions;

import kenh.expl.UnsupportedExpressionException;
import kenh.expl.impl.BaseFunction;

import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class GetConnection extends BaseFunction {

	public static Properties LOCAL = new Properties();
	
	static {
		LOCAL.put("driverClassName", "org.hsqldb.jdbcDriver");
		LOCAL.put("url", "jdbc:hsqldb:hsql://localhost/xdb");
		LOCAL.put("user", "sa");
		LOCAL.put("password", "");
	}
	
	public static final String TABLE_NAME = "TEST_TABLE_KENH_XSCRIPT_DATABASE";

	public Connection process() throws UnsupportedExpressionException {

		try {
			String url = LOCAL.getProperty("url");
			Connection conn = DriverManager.getConnection(url, LOCAL);
			return new kenh.xscript.database.wrap.Connection(conn);
		} catch(SQLException e) {
			throw new UnsupportedExpressionException(e);
		}
	}
	
	public Connection process(int no) throws UnsupportedExpressionException {
		
		Connection conn = process();
		
		try {
			boolean autoCommit = conn.getAutoCommit();
			
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			
			st.executeUpdate("drop table " + TABLE_NAME + " if exists");
			st.executeUpdate("CREATE TABLE " + TABLE_NAME + "( id INTEGER IDENTITY, str_col VARCHAR(256), num_col DOUBLE, bo_col BOOLEAN, date_col TIMESTAMP )");
			
			if(no > 0) {
				for(int i=0; i< no; i++) {
					Date d = new Date();
					double n = Math.random();
					
					String str_col = "t_" + d.getTime();
					double num_col = n;
					boolean bo_col = n > 0.5? true : false;
					Date date_col = d;
					
					int ii = i % 5;
					String sql = "INSERT INTO " + TABLE_NAME + "(str_col,num_col,bo_col,date_col) VALUES(?, ?, ?, ?)";
					Object[] objs = new Object[]{};
					switch(ii) {
					case 0:	objs = new Object[]{ str_col, num_col, bo_col, date_col }; break;
					case 1:	objs = new Object[]{ null, num_col, bo_col, date_col }; break;
					case 2:	objs = new Object[]{ str_col, null, bo_col, date_col }; break;
					case 3:	objs = new Object[]{ str_col, num_col, null, date_col }; break;
					case 4:	objs = new Object[]{ str_col, num_col, bo_col, null }; break;
					}
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					ii = 1;
					for(Object obj: objs) {
						pstmt.setObject(ii, obj);
						ii++;
					}
					
					pstmt.execute();
				}
			}
			
			conn.commit();
			conn.setAutoCommit(autoCommit);
		} catch(Exception e) {
			try {
				conn.rollback();
				conn.close();
			} catch(Exception e_) {
				
			}
			throw new UnsupportedExpressionException(e);
		}
		
		return new kenh.xscript.database.wrap.Connection(conn);
	}
	
}
