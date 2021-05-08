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

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

import java.util.*;

import javax.sql.DataSource;

import kenh.xscript.elements.Param;
import kenh.xscript.elements.Set;


/**
 * Datasource element, create a datasource.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
@kenh.xscript.annotation.Include(Param.class)
@IgnoreSuperClass
public class Datasource extends Set {

	private static final String ATTRIBUTE_VARIABLE = "var"; // variable name
	
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		Properties properties = new Properties();
		
		this.invokeChildren();
		Vector<Element> children = this.getChildren();
		
		for(Element child: children) {
			if(child instanceof Param) {
				Param c = (Param)child;
				String name = c.getName();
				Object value = Param.getParsedValue(c);
				if(c.isLink()) {
					String linkName = c.getLinkName();
					if(!properties.containsKey(linkName)) new UnsupportedScriptException(this, "Could not find the parameter to link. [" + name + ", " + linkName + "]");
					value = properties.get(linkName);
				}
				
				properties.put(name, value);
			}
		}
		
		try {
			DataSource datasource = BasicDataSourceFactory.createDataSource(properties);
			this.saveVariable(var, datasource, null);
			
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
}
