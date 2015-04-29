/*
 * xScript (XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.
 * 
 * xScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript.elements;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

import java.util.*;

/**
 * Store the variable.
 * 
 * @author Kenneth
 *
 */
@kenh.xscript.annotation.Include(Condition.class)
public class Set extends NormalElement {

	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_DEFAULT = "default";  // default value 
	
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_VALUE) Object value) throws UnsupportedScriptException {
		process(var, value, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_DEFAULT) Object defaultValue) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		this.invokeChildren();
		Vector<Element> children = this.getChildren();
		
		for(Element child: children) {
			if(child instanceof Condition) {
				Condition c = (Condition)child;
				if(c.getCondition()) value = c.getValue();
			}
		}
		
		saveVariable(var, value, defaultValue);
	}
	
}
