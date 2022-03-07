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
 * Abbreviates a String.
 * 
 * @author Kenneth
 * @since 1.0
 * 
 * @see StringUtils#abbreviate(String, int)
 * @see StringUtils#abbreviate(String, int, int)
 *
 */
public class Abbreviate extends BaseFunction {
	
	public String process(String str, int offset, int maxWidth) {
		return StringUtils.abbreviate(str, offset, maxWidth);
	}

	public String process(String str, int maxWidth) {
		return StringUtils.abbreviate(str, maxWidth);
	}
	
}
