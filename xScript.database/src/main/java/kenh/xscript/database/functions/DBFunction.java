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
import java.sql.Statement;

import javax.sql.DataSource;

import kenh.expl.UnsupportedExpressionException;
import kenh.expl.impl.BaseFunction;
import kenh.xscript.database.beans.ResultSetBean;

/**
 * Provide the data source support and the reference name support.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public abstract class DBFunction extends BaseFunction {
	
	public abstract Object process(Connection conn, String sql) throws Exception;
	
	public Object process(DataSource source, String sql) throws Exception {
		Connection conn = null;
		try {
			conn = source.getConnection();
			return process(conn, sql);
		} catch(Exception e) {
			throw e;
		} finally {
			if(conn != null) {
				try { conn.close(); } catch(Exception e_) { }
				conn = null;
			}
		}
	}
	
	public Object process(String ref, String sql) throws Exception {
		if(this.getEnvironment().containsVariable(ref)) {
			Object obj = this.getEnvironment().getVariable(ref);
			if(obj instanceof java.sql.Connection) {
				return process((java.sql.Connection)obj, sql);
			} else if(obj instanceof DataSource) {
				return process((DataSource)obj, sql);
			} else {
				throw new UnsupportedExpressionException( "Can't conver to connection or data source to execute sql. [" + ref + ", " + obj.getClass().getCanonicalName() + "]");
			}
		} else {
			throw new UnsupportedExpressionException( "Can't find connection or data source to execute sql.");
		}
	}
	
	
}
