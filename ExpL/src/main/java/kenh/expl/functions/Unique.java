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
import org.apache.commons.lang3.StringUtils;

/**
 * Remove duplicate
 *
 * @author Kenneth
 * @since 1.0
 *
 */
public class Unique extends BaseFunction {

	public Object[] process(Object[] array) {
		return process(array, true);
	}

	public Object[] process(Object[] array, boolean trim) {
		java.util.Vector vector = new java.util.Vector();
		for(Object obj: array) {
			if(trim && obj instanceof String) {
				String s = StringUtils.trimToEmpty((String)obj);
				obj = s;
			}
			if(!vector.contains(obj)) {
				vector.add(obj);
			}
		}
		return vector.toArray();
	}

	public java.util.Vector process(java.util.Vector vector) {
		return process(vector, true);
	}

	public java.util.Vector process(java.util.Vector vector, boolean trim) {
		java.util.Vector vector_ = new java.util.Vector();
		for(Object obj: vector) {
			if(trim && obj instanceof String) {
				String s = StringUtils.trimToEmpty((String)obj);
				obj = s;
			}
			if(!vector.contains(obj)) {
				vector.add(obj);
			}
		}
		return vector_;
	}

}
