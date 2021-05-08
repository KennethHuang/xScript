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

package kenh.xscript.database.beans;

import java.util.List;
import java.util.LinkedList;

import kenh.xscript.beans.ParamBean;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL bean, use to store SQL and parameter(s).
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public class SQLBean {
	
	private String sql = null;  // SQL
	private List<ParamBean> parameters = new LinkedList();  // the parameter(s)
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public List<ParamBean> getParameters() {
		return parameters;
	}
	public void setParameter(ParamBean p) {
		if(p != null) parameters.add(p);
	}
	
}
