/*
 * xScript.database ( Database for XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.database.
 * 
 * xScript.database is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript.database is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.database.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript.database.functions;

import kenh.expl.impl.BaseFunction;

public class ToSQLDate extends BaseFunction {
	
	public Object process(Object obj) throws Exception {
		if(obj == null) return null;

		if(obj instanceof java.util.Date) {
			java.util.Date jDate = (java.util.Date)obj;
			java.sql.Date sDate = new java.sql.Date(jDate.getTime());
			return sDate;
		}

		return obj;
	}

}
