/*
 * ExpL (Expression Language)
 * Copyright 2014 and beyond, Kenneth Huang
 * 
 * This file is part of ExpL.
 * 
 * ExpL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * ExpL is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with ExpL.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.expl.functions;

import kenh.expl.impl.BaseFunction;

/**
 * Checks if the Object is null.
 * 
 * @author Kenneth
 * @since 1.0
 * 
 */
public class IsNull extends BaseFunction {
	
	public boolean process(Object obj) {
		if(obj == null) return true;
		
		if(obj instanceof String) {
			String var;

			var = (String)obj;
			if(var.equals("")) return true;
			if(!this.getEnvironment().containsVariable(var)) return true;
			
			obj = this.getEnvironment().getVariable(var);
			return (obj == null);
		} else {
			return false;
		}
		
	}
}
