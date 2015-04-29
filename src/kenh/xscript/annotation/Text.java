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
 * Text support annotation for {@code Element}.
 * 1) without {@code Text} annotation - {@code Element} does not support text.
 * 2) {@code Text} annotation with {@code Type.NONE} - {@code Element} does not support text.
 * 3) {@code Text} annotation with {@code Type.FULL} - all content, with all blank.
 * 4) {@code Text} annotation with {@code Type.TRIM} - the content skip blank
 * 
 * @author Kenneth
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Text {
	
	/**
	 * The text support type. 
	 * @return
	 */
	Type value();
	
	public enum Type {
		NONE, 	// does not support text
		FULL,	// all content, with all blank.
		TRIM,	// the content skip blank
	}
}
