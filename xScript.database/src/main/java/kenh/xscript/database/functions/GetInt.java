/*
 * xScript.database ( Database for XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.database.
 * 
 * xScript.database is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript.database is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.database.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript.database.functions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import kenh.expl.UnsupportedExpressionException;
import kenh.expl.impl.BaseFunction;
import kenh.xscript.database.beans.ResultSetBean;

/**
 * Retrieves the integer value of first column of first row.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public class GetInt extends DBFunction {
	
	public Integer process(Connection conn, String sql) throws Exception {
		Statement s = null;
		ResultSet rs = null;
		try {
			s = conn.createStatement();
			rs = s.executeQuery(sql);
			
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
			
		} catch(Exception e) {
			throw e;
		} finally {
			if(rs != null) {
				try { rs.close(); } catch(Exception e_) { }
				rs = null;
			}
			if(s != null) {
				try { s.close(); } catch(Exception e_) { }
				s = null;
			}
		}
	}
	
}
