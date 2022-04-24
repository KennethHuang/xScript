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

package kenh.xscript.database.elements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.Constant;
import kenh.xscript.Element;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Processing;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Text;
import kenh.xscript.beans.ParamBean;
import kenh.xscript.database.beans.ResultSetBean;
import kenh.xscript.database.beans.SQLBean;
import kenh.xscript.elements.Catch;
import kenh.xscript.elements.Param;
import kenh.xscript.impl.NormalElement;

/**
 * Execute element, execute the sql.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
@kenh.xscript.annotation.Include(Param.class)
@Text(Text.Type.TRIM)
public class Execute extends NormalElement {

	private static final String ATTRIBUTE_SQL = "sql";  // this value should be a SQLBean or a variable name refer to SQLBean
	private static final String ATTRIBUTE_VARIABLE = "var";   // variable name, to save the result.
	private static final String ATTRIBUTE_REF = "ref";  // can be a Connection, DataSource or a String refer to Connection or DataSource
	
	
	// ----------------------------------- Part1: java.sql.Connection
	
	@Processing
	public int process_Conn_Var_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		return executeSQL(sqlBean, var, conn);
	}
	
	@Processing
	public int process_Conn_Var_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		SQLBean bean = this.getEnvironment().getVariable(sqlRef, SQLBean.class);
		return process_Conn_Var_SqlBean(bean, var, conn);
	}
	
	@Processing
	public int process_Conn_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		return process_Conn_Var_SqlBean(sqlBean, null, conn);
	}
	
	@Processing
	public int process_Conn_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		return process_Conn_Var_Sql(sqlRef, null, conn);
	}
	
	@Processing
	public int process_Conn_Var(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		SQLBean bean = new SQLBean();
		bean.setSql(getTextAsSql());
		
		return process_Conn_Var_SqlBean(bean, var, conn);
	}
	
	@Processing
	public int process_Conn(@Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		return process_Conn_Var(null, conn);
	}
	
	
	// ----------------------------------- Part2: javax.sql.DataSource
	
	@Processing
	public int process_Source_Var_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		java.sql.Connection conn = null;
		try {
			conn = source.getConnection();
			conn.setAutoCommit(true);
			return executeSQL(sqlBean, var, conn);
		} catch(java.sql.SQLException sqle) {
			throw new UnsupportedScriptException(this, sqle);
		} finally {
			if(conn != null) {
				try { conn.close(); } catch(Exception e_) { }
				conn = null;
			}
		}
	}
	
	@Processing
	public int process_Source_Var_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		SQLBean bean = this.getEnvironment().getVariable(sqlRef, SQLBean.class);
		return process_Source_Var_SqlBean(bean, var, source);
	}
	
	@Processing
	public int process_Source_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		return process_Source_Var_SqlBean(sqlBean, null, source);
	}
	
	@Processing
	public int process_Source_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		return process_Source_Var_Sql(sqlRef, null, source);
	}
	
	@Processing
	public int process_Source_Var(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		SQLBean bean = new SQLBean();
		bean.setSql(getTextAsSql());
		
		return process_Source_Var_SqlBean(bean, var, source);
	}
	
	@Processing
	public int process_Source(@Attribute(ATTRIBUTE_REF) DataSource source) throws UnsupportedScriptException {
		return process_Source_Var(null, source);
	}
	
	
	// ----------------------------------- Part3: String
	
	@Processing
	public int process_StringConn_Var_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		
		try {
			java.sql.Connection conn = this.getEnvironment().getVariable(ref, java.sql.Connection.class);
			return process_Conn_Var_SqlBean(sqlBean, var, conn);
			
		} catch(NullPointerException e) {
			
			DataSource ds = this.getEnvironment().getVariable(ref, DataSource.class);
			return process_Source_Var_SqlBean(sqlBean, var, ds);
		}
	}
	
	@Processing
	public int process_StringConn_Var_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		try {
			java.sql.Connection conn = this.getEnvironment().getVariable(ref, java.sql.Connection.class);
			return process_Conn_Var_Sql(sqlRef, var, conn);
			
		} catch(NullPointerException e) {
			
			DataSource ds = this.getEnvironment().getVariable(ref, DataSource.class);
			return process_Source_Var_Sql(sqlRef, var, ds);
		}
	}
	
	@Processing
	public int process_StringConn_SqlBean(@Attribute(ATTRIBUTE_SQL) SQLBean sqlBean, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		return process_StringConn_Var_SqlBean(sqlBean, null, ref);
	}
	
	@Processing
	public int process_StringConn_Sql(@Attribute(ATTRIBUTE_SQL) String sqlRef, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		return process_StringConn_Var_Sql(sqlRef, null, ref);
	}
	
	@Processing
	public int process_StringConn_Var(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		SQLBean bean = new SQLBean();
		bean.setSql(getTextAsSql());
		
		return process_StringConn_Var_SqlBean(bean, var, ref);
	}
	
	@Processing
	public int process_StringConn(@Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		return process_StringConn_Var(null, ref);
	}
	
	
	
	
	/**
	 * Use child elements <code>Param</code> to set parameter for <code>SQLBean</code>.
	 * @param bean
	 * @throws UnsupportedScriptException
	 */
	private Map<String, String> getParameters(SQLBean bean) throws UnsupportedScriptException {
		
		List<ParamBean> parameters = bean.getParameters();
		
		Map<String, String> values = new LinkedHashMap();
		Map<String, String> links = new LinkedHashMap();
		
		for(ParamBean p: parameters) {
			String name = p.getName();
			
			if(p.isLink()) {
				String linkName = p.getLinkName();
				links.put(name, linkName);
				
			} else {
				String value = p.getValue();
				values.put(name, value);
				
			}
			
		}
		
		this.invokeChildren();
		Vector<Element> children = this.getChildren();
		
		for(Element child: children) {
			if(child instanceof Param) {
				Param c = (Param)child;
				String name = c.getName();
				
				if(!values.containsKey(name) && !links.containsKey(name)) throw new UnsupportedScriptException(this, "Unknown parameter. [" + name + "]");
				
				values.remove(name);
				links.remove(name);
				
				if(c.isLink()) {
					String linkName = c.getLinkName();
					links.put(name, linkName);
					
				} else {
					String value = c.getValue();
					values.put(name, value);
					
				}
			}
		}
		
		Map<String, String> results = new LinkedHashMap();
		
		for(ParamBean p: parameters) {
			String name = p.getName();
			
			if(values.containsKey(name)) {
				String value = values.get(name);
				results.put(name, value);
			} else if(links.containsKey(name)) {
				
				String linkName = links.get(name);
				Vector<String> linkNames = new Vector();
				linkNames.add(name);
				while(links.containsKey(linkName)) {
					if(linkNames.contains(linkName)) throw new UnsupportedScriptException(this, "Infinite Loop. [" + name + ", " + linkName + "]");
					
					linkNames.add(linkName);
					linkName = links.get(linkName);
				}
				
				if(!values.containsKey(linkName)) throw new UnsupportedScriptException(this, "Could not find the parameter to link. [" + name + ", " + linkName + "]");
				
				String value = values.get(linkName);
				results.put(name, value);
			}
		}
		
		return results;
		
	}
	
	/**
	 * Create a <code>SQLBean</code> through text content.
	 * @return
	 * @throws UnsupportedScriptException
	 */
	private String getTextAsSql() throws UnsupportedScriptException {
		
		Object sql_ = this.getParsedText();
		String sql = null;
		if(sql_ instanceof String) {
			sql = (String)sql_;
		}
		
		return sql;
	}
	
	/**
	 * Execute sql.
	 * @param sqlBean
	 * @param var   variable name of result
	 * @param conn
	 */
	private int executeSQL(SQLBean sqlBean, String var, java.sql.Connection conn) throws UnsupportedScriptException {
		if(sqlBean == null || StringUtils.isBlank(sqlBean.getSql())) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Can't find the sql to execute.");
			throw ex;
		}
		
		if(conn == null) {
			throw new UnsupportedScriptException(this, "Connection is empty.");
		}
		
		var = StringUtils.trimToNull(var);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			if(conn.isClosed()) {
				throw new UnsupportedScriptException(this, "Connection is closed.");
			}
			
			StringBuffer traceInfo = new StringBuffer();
			traceInfo.append("Execute SQL: \n" + StringUtils.trim(sqlBean.getSql()));
			
			pstmt = conn.prepareStatement(sqlBean.getSql());
			Map<String, String> parameters = getParameters(sqlBean);
			
			Iterator<String> elements = parameters.values().iterator();
			
			// set the Paramter for PreparedStatement
			int i = 1;
			while(elements.hasNext()) {
				String str = elements.next();
				Object obj = this.getEnvironment().parse(str);
				traceInfo.append("\nParam " + i + ": " + obj.toString());
				pstmt.setObject(i, obj);
				i++;
			}
			
			this.getLogger().trace("[XSCRIPT] " + traceInfo.toString());
			
			boolean result = false;
			
			result = pstmt.execute();
			
			if(result) {
				rs = pstmt.getResultSet();
				
				if(StringUtils.isNotBlank(var)) {
					ResultSetBean bean = new ResultSetBean(rs);
					this.saveVariable(var, bean, null);
				}
				
				
			} else {
				int count = pstmt.getUpdateCount();
				if(StringUtils.isNotBlank(var)) this.saveVariable(var, count, null);
			}
			
		} catch(java.sql.SQLException e) {
			this.getEnvironment().setVariable(Constant.VARIABLE_EXCEPTION, e);
			return EXCEPTION;
		} catch(IllegalAccessException e) {
			this.getEnvironment().setVariable(Constant.VARIABLE_EXCEPTION, e);
			return EXCEPTION;
		} catch(InstantiationException e) {
			this.getEnvironment().setVariable(Constant.VARIABLE_EXCEPTION, e);
			return EXCEPTION;
		} catch(UnsupportedExpressionException e) {
			throw new UnsupportedScriptException(this, e);
		} finally {
			
			if(rs != null) {
				try { rs.close(); } catch(Exception e) {}
				rs = null;
			}
			
			if(pstmt != null) {
				try { pstmt.close(); } catch(Exception e) {}
				pstmt = null;
			}
		}
		
		return NONE;
		
	}
}
