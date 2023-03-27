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
 * Return a date.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class GetDate extends BaseFunction {
	
	public java.util.Date process(java.util.Date d) {
		return d;
	}

	public java.util.Date process() {
		return new java.util.Date();
	}

	public java.util.Date process(java.util.Calendar c) {
		return c.getTime();
	}

	public java.util.Date process(java.sql.Date d) {
		return new java.util.Date(d.getTime());
	}

	public java.util.Date process(java.sql.Time t) {
		return new java.util.Date(t.getTime());
	}

	public java.util.Date process(java.sql.Timestamp t) {
		return new java.util.Date(t.getTime());
	}
}
