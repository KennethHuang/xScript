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

import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Reparse;
import kenh.xscript.impl.NormalElement;

/**
 * If and Then combination.
 * 
 * @author Kenneth
 *
 */
public class Ifthen extends NormalElement {

	private static final String ATTRIBUTE_CONDITION = "cond";
	
	
	public int process(@Reparse@Attribute(ATTRIBUTE_CONDITION) boolean cond) throws UnsupportedScriptException {
		if(cond) {
			return invokeChildren();
		}
		return this.NONE;
	}
	
}
