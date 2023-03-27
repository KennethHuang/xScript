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

import java.util.Calendar;
import java.util.Date;

/**
 * change the month of date.
 * 
 * @author Kenneth
 * @since 1.0
 * 
 */
public class AddMonth extends BaseFunction {
	
	public Date process(Date d, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, i);
		return cal.getTime();
	}

	public Date process(Date d, String s) {
		if(StringUtils.isNumeric(s)) {
			return process(d, Integer.parseInt(s));
		} else {
			return d;
		}
	}

}
