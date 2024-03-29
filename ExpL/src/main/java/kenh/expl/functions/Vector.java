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

import kenh.expl.UnsupportedExpressionException;
import kenh.expl.impl.BaseFunction;
import java.util.Arrays;

/**
 * Return a vector.
 * 
 * @author Kenneth
 * @since 1.0.1
 * 
 */
public class Vector extends BaseFunction {
	
	@Override
	public java.util.Vector<Object> invoke(Object... params) throws UnsupportedExpressionException {
		if(params.length > 0) {
			java.util.Vector<Object> vector = null;
			
			Object obj = params[0];
			int start = 0;
			if(obj instanceof java.util.Vector) {
				vector = (java.util.Vector<Object>)obj;
				start = 1;
			} else {
				vector = new java.util.Vector<Object>();
			}
			vector.addAll(Arrays.asList(params).subList(start, params.length));

			return vector;
			
		} else {
			return new java.util.Vector<Object>();
		}
	}
}
