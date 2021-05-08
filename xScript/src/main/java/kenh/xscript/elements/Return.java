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

import kenh.xscript.annotation.*;
import kenh.xscript.impl.BaseElement;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

import java.io.*;
import java.net.URL;
import java.util.Vector;

/**
 * Return from {@code Method}, and store return value.
 * 
 * @author Kenneth
 *
 */
public class Return extends NoChildElement {
	
	private static final String ATTRIBUTE_VALUE = "value";
	
	public static final String VARIABLE_RETURN = "{return}";
	
	public int process(@Attribute(ATTRIBUTE_VALUE) Object value) throws UnsupportedScriptException {
		if(value != null) {
			this.getEnvironment().setVariable(VARIABLE_RETURN, value);
		}
		return RETURN;
	}
	
	public int process() throws UnsupportedScriptException {
		return process(null);
	}
	
	
}
