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

import kenh.xscript.Element;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Exclude;
import kenh.xscript.annotation.IgnoreSuperClass;
import kenh.xscript.impl.NoChildElement;

/**
 * Accumulator. It will plus a number for certain variable.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
@IgnoreSuperClass
@Exclude(Element.class)
public class Accum extends Set {

	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_STEP = "step";
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(var, 1);
	}
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_STEP) int step) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		String thisVar = var;
		if(StringUtils.contains(var, " ")) {
			thisVar = StringUtils.substringAfterLast(var, " ");
		}
		
		int defaultValue = 0;
		if(this.getEnvironment().containsVariable(thisVar)) {
			Object obj = this.getEnvironment().getVariable(thisVar);
			if(obj instanceof Integer) {
				defaultValue = ((Integer)obj).intValue();
			} else if(obj instanceof String) {
				if(StringUtils.isNumeric((String)obj)) {
					defaultValue = Integer.parseInt((String)obj);
				}
				
			} else {
				UnsupportedScriptException ex = new UnsupportedScriptException(this, "Only support integer variable.");
				throw ex;
			}
		}
		defaultValue += step;
		
		saveVariable(var, defaultValue, defaultValue);
	}
}
