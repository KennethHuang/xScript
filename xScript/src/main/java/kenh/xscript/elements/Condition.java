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
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

/**
 * Child element of <code>Set</code>.
 * 
 * @author Kenneth
 * @since 1.0
 * 
 *
 */
public class Condition extends NoChildElement {

	private static final String ATTRIBUTE_COND = "cond";
	private static final String ATTRIBUTE_VALUE = "value";
	
	
	private boolean cond = false;
	private Object value = null;
	
	
	public void process(@Reparse@Attribute(ATTRIBUTE_COND) boolean cond, @Attribute(ATTRIBUTE_VALUE) Object value) throws UnsupportedScriptException {
		this.cond = cond;
		this.value = value;
	}
	
	public boolean getCondition() {
		return cond;
	}
	
	public Object getValue() {
		return value;
	}
	
}
