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

import java.lang.reflect.*;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.impl.NoChildElement;

/**
 * Debug the reference.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Debug extends NoChildElement {
	
	private static final String ATTRIBUTE_VALUE = "value";
	
	public void process(@Attribute(ATTRIBUTE_VALUE) Object obj) {
		
		System.out.println("------------------");
		System.out.println(obj.getClass().getCanonicalName());
		
		System.out.println("1) Fields:");
		Field[] fields = obj.getClass().getFields();
		
		for(Field field: fields) {
			int i = field.getModifiers();
			String retval = Modifier.toString(i);
			if(StringUtils.contains(retval, "public")) {
				System.out.println("\t" + field.getName() + " - " + retval);
			}
		}
		
		System.out.println("2) Method:");
		java.lang.reflect.Method[] methods = obj.getClass().getMethods();
		
		for(java.lang.reflect.Method method: methods) {
			int i = method.getModifiers();
			String retval = Modifier.toString(i);
			if(StringUtils.contains(retval, "public")) {
				Class[] pcs = method.getParameterTypes();
				StringBuffer sb = new StringBuffer();
				
				for(Class c: pcs) {
					String s = c.getSimpleName();
					sb.append(s + ", ");
				}
				
				String p = StringUtils.trimToEmpty(StringUtils.substringBeforeLast(sb.toString(), ","));
				
				System.out.println("\t" + method.getName() + "(" + p + ") - " + retval);
			}
		}
		
		System.out.println("------------------");
		
	}
	
	/**
	 * TODO - Swing support, a dialog use to debug.
	 */
	public void process() {	
	}
}
