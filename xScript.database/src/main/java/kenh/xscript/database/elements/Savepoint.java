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

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Processing;
import kenh.xscript.annotation.Text;
import kenh.xscript.impl.*;
import kenh.xscript.*;

import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;


/**
 * Savepoint element. Creates an savepoint in the current transaction of database connection.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public class Savepoint extends NoChildElement {

	private static final String ATTRIBUTE_VARIABLE = "var";  // variable name
	private static final String ATTRIBUTE_REF = "ref";   // reference name of database connection
	
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		java.sql.Savepoint sp = null;
		try {
			sp = conn.setSavepoint();
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		this.saveVariable(var, sp, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		if(this.getEnvironment().containsVariable(ref)) {
			Object obj = this.getEnvironment().getVariable(ref);
			if(obj instanceof java.sql.Connection) {
				process(var, (java.sql.Connection)obj);
			} else {
				throw new UnsupportedScriptException(this, "Can't convert to Connection. [" + ref + ", " + obj.getClass().getCanonicalName() + "]");
			}
		} else {
			throw new UnsupportedScriptException(this, "Can't find connection to save point.");
		}
	}
}
