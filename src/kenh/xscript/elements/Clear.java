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

import kenh.xscript.annotation.Attribute;
import kenh.xscript.impl.NoChildElement;

/**
 * Remove the variable from environment.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Clear extends NoChildElement {
	
	private static final String ATTRIBUTE_VARIABLE = "var";
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) {
		if(this.getEnvironment().containsVariable(var)) {
			this.getEnvironment().removeVariable(var);
		}
	}
	
}
