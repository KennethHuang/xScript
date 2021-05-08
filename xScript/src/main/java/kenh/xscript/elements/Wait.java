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

package kenh.xscript.elements;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Processing;
import kenh.xscript.impl.NoChildElement;

/**
 * Sleep the current thread.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Wait extends NoChildElement {
	
	private static final String ATTRIBUTE_MICRO_SECOND = "ms";
	private static final String ATTRIBUTE_SECOND = "ss";
	private static final String ATTRIBUTE_MINUTE = "mm";
	private static final String ATTRIBUTE_HOUR = "hh";
	
	@Processing
	public void processMicoSecond(@Attribute(ATTRIBUTE_MICRO_SECOND) int ms) {
		sleep(ms);
	}
	
	@Processing
	public void processSecond(@Attribute(ATTRIBUTE_SECOND) int ss) {
		sleep(ss * 1000);
	}
	
	@Processing
	public void processMinute(@Attribute(ATTRIBUTE_MINUTE) int mm) {
		sleep(mm * 60 * 1000);
	}
	
	@Processing
	public void processHour(@Attribute(ATTRIBUTE_HOUR) int hh) {
		sleep(1l * hh * 60 * 60 * 1000);
	}
	
	private static void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch(Exception e) {
			
		}
	}
	
}
