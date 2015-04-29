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

package kenh.xscript.annotation;

import java.lang.annotation.*;

/**
 * List all legal child elements.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Include {
	
	/**
	 * All legal child elements.
	 * @return
	 */
	Class[] value();
	
	/**
	 * List amount of each child allowed, etc, 
	 * 1 means this type of child only allow one.
	 * 0 or negative means do not have amount limit.
	 * 
	 * @return
	 */
	int[] number() default {}; 
	
}
