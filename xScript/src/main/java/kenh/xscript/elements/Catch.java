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

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Processing;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

/**
 * 
 * Catch element. When any element return <code>EXCEPTION</code>, this element will be ask to handle.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Catch extends NormalElement {
	
	public void process() {
		
	}
	
	public int processException() throws UnsupportedScriptException {
		int result = invokeChildren();
		
		if(this.getEnvironment().containsVariable(Constant.VARIABLE_EXCEPTION)) {
			this.getEnvironment().removeVariable(Constant.VARIABLE_EXCEPTION);
		}
		
		return result;
	}
	

}
