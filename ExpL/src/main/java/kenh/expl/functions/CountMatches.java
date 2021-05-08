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

import org.apache.commons.lang3.StringUtils;
import kenh.expl.impl.BaseFunction;

/**
 * Counts how many times the substring appears in the larger string.
 * 
 * @author Kenneth
 * @since 1.0
 * 
 * @see StringUtils#countMatches(CharSequence, CharSequence)
 *
 */
public class CountMatches extends BaseFunction {
	
	public int process(String str, String sub) {
		return StringUtils.countMatches(str, sub);
	}
	
}
