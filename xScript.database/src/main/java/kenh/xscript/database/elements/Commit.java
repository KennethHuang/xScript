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
 * Commit the connection
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public class Commit extends NoChildElement {

	private static final String ATTRIBUTE_REF = "ref";
	
	
	public void process(@Attribute(ATTRIBUTE_REF) java.sql.Connection conn) throws UnsupportedScriptException {
		try {
			if(!conn.isClosed()) {
				if(!conn.getAutoCommit()) conn.commit();
			}
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_REF) String ref) throws UnsupportedScriptException {
		java.sql.Connection conn = this.getEnvironment().getVariable(ref, java.sql.Connection.class);
		process(conn);
	}
}
