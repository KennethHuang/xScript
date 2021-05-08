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

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang3.StringUtils;

import kenh.expl.Callback;
import kenh.xscript.annotation.*;
import kenh.xscript.*;

import java.util.*;
import java.util.concurrent.Executor;

import javax.sql.*;

import java.sql.*;

import kenh.xscript.elements.Set;


/**
 * Retrieves the database connection.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
@Exclude(Element.class)
@IgnoreSuperClass
public class Connection extends Set {

	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_SOURCE = "source";
	private static final String ATTRIBUTE_AUTO_COMMIT = "auto-commit";
	
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_SOURCE) String source) throws UnsupportedScriptException {
		process(var, source, true);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_SOURCE) String source, @Attribute(ATTRIBUTE_AUTO_COMMIT) boolean autoCommit) throws UnsupportedScriptException {
		DataSource ds = this.getEnvironment().getVariable(source, DataSource.class);
		process(var, ds, autoCommit);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_SOURCE) DataSource source) throws UnsupportedScriptException {
		process(var, source, true);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_SOURCE) DataSource source, @Attribute(ATTRIBUTE_AUTO_COMMIT) boolean autoCommit) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		if(source == null) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Data source is empty.");
			throw ex;
		}
		
		try {
			kenh.xscript.database.wrap.Connection conn = new kenh.xscript.database.wrap.Connection(source.getConnection());
			conn.setAutoCommit(autoCommit);
			this.saveVariable(var, conn, null);
			
		} catch(SQLException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}

}
