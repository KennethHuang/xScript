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

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.annotation.*;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

/**
 * For element.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class For extends NormalElement {

	private static final String ATTRIBUTE_FROM = "from";
	private static final String ATTRIBUTE_TO = "to";
	private static final String ATTRIBUTE_STEP = "step";
	
	private static final String ATTRIBUTE_REF = "ref";
	private static final String ATTRIBUTE_SUBREF = "sub-ref";
	private static final String ATTRIBUTE_SKIP = "skip";	// skip some reference on the top of Iterable
	
	
	private static final String VARIABLE_INDEX = "@index";
	private static final String VARIABLE_REF = "@ref";
	
	
	private static final int MAX_LOOP = 100000;
	
	
	public int process(@Attribute(ATTRIBUTE_REF) Object ref) throws UnsupportedScriptException {
		return process(ref, VARIABLE_REF);
	}
	
	public int process(@Attribute(ATTRIBUTE_REF) Object ref, @Attribute(ATTRIBUTE_SKIP) int skip) throws UnsupportedScriptException {
		return process(ref, VARIABLE_REF, skip);
	}
	
	public int process(@Attribute(ATTRIBUTE_REF) Object ref, @Attribute(ATTRIBUTE_SUBREF) String subRef) throws UnsupportedScriptException {
		return process(ref, subRef, 0);
	}
	
	public int process(@Attribute(ATTRIBUTE_REF) Object ref, @Attribute(ATTRIBUTE_SUBREF) String subRef, @Attribute(ATTRIBUTE_SKIP) int skip) throws UnsupportedScriptException {
		if(ref instanceof String) {
			ref = this.getEnvironment().getVariable((String)ref);
		}
		if(ref == null) throw new UnsupportedScriptException(this, "Reference is empty.");
		
		Iterable iterable = null;
		
		if(ref.getClass().isArray()) {
			// convert array to list
			if((int[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((int[])ref);
				
			} else if((float[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((float[])ref);
				
			} else if((double[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((double[])ref);
				
			} else if((short[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((short[])ref);
				
			} else if((byte[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((byte[])ref);
				
			} else if((long[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((long[])ref);
				
			} else if((char[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((char[])ref);
				
			} else if((boolean[].class).isAssignableFrom(ref.getClass())) {
				iterable = convertToList((boolean[])ref);
				
			} else {
				iterable = convertToList((Object[])ref);
				
			}
			
		} else if(ref instanceof Iterable) {
			iterable = (Iterable)ref;
		} else {
			throw new UnsupportedScriptException(this, "Can only iterate over an array or an instance of java.lang.Iterable.");
		}
		
		if(iterable == null) {
			throw new UnsupportedScriptException(this, "Iterable is empty.");
		}
		
		Object obj = null;
		if(this.getEnvironment().containsVariable(VARIABLE_INDEX)) {
			obj = this.getEnvironment().removeVariable(VARIABLE_INDEX);
		}
		Object refObj = null;
		if(this.getEnvironment().containsVariable(subRef)) {
			refObj = this.getEnvironment().removeVariable(subRef);
		}
		
		int i = 0, x = 0;
		if(skip < 0) skip = 0;
		
		try {
			
			for(Object sub: iterable) {
				
				x++;
				if(x <= skip) continue;
				
				i++;
				this.getEnvironment().setVariable(VARIABLE_INDEX, i);
				this.getEnvironment().setVariable(subRef, sub);
				if(i > MAX_LOOP) {
					throw new UnsupportedScriptException(this, "Endless Loop? ( > " + MAX_LOOP + ")");
				}
				
				int result = invokeChildren();
				if(result == BREAK) {
					break;
				} else if(result == CONTINUE) {
					continue;
				} else if(result == RETURN) {
					return RETURN;
				}
				
			}
			
		} finally {
			
			this.getEnvironment().removeVariable(VARIABLE_INDEX);
			this.getEnvironment().removeVariable(subRef);
			
			if(obj != null) {
				this.getEnvironment().setVariable(VARIABLE_INDEX, obj);
			}
			if(refObj != null) {
				this.getEnvironment().setVariable(subRef, refObj);
			}
		}
		
		return NONE;
	}
	
	private List convertToList(int[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(float[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(double[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(short[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(byte[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(long[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(char[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(boolean[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	private List convertToList(Object[] array) {
		List l = new LinkedList();
		for(Object obj: array) {
			l.add(obj);
		}
		return l;
	}
	
	
	public int process(@Attribute(ATTRIBUTE_FROM) int from, @Attribute(ATTRIBUTE_TO) int to) throws UnsupportedScriptException {
		int step = 1;
		if(from > to) step = -1;
		return process(from, to, step);
	}
	
	public int process(@Attribute(ATTRIBUTE_FROM) int from, @Attribute(ATTRIBUTE_TO) int to, @Attribute(ATTRIBUTE_STEP) int step) throws UnsupportedScriptException {
		
		if(step == 0) throw new UnsupportedScriptException(this, "Step can't be ZERO.");
		
		if(step > 0 && from > to) throw new UnsupportedScriptException(this, "Endless Loop? [" + from + ", " + to + ", " + step + "]");
		if(step < 0 && from < to) throw new UnsupportedScriptException(this, "Endless Loop? [" + from + ", " + to + ", " + step + "]");
		
		Object obj = null;
		if(this.getEnvironment().containsVariable(VARIABLE_INDEX)) {
			obj = this.getEnvironment().removeVariable(VARIABLE_INDEX);
		}
		
		int i = 0;
		
		try {
			
			for(int index=from; step>0?index<=to:index>=to; index+=step) {
				
				this.getEnvironment().setVariable(VARIABLE_INDEX, index);
				
				i++;
				if(i > MAX_LOOP) {
					throw new UnsupportedScriptException(this, "Endless Loop? ( > " + MAX_LOOP + ")");
				}
				
				int result = invokeChildren();
				if(result == BREAK) {
					break;
				} else if(result == CONTINUE) {
					continue;
				} else if(result == RETURN) {
					return RETURN;
				}
			}
			
		} finally {
			this.getEnvironment().removeVariable(VARIABLE_INDEX);
			
			if(obj != null) {
				this.getEnvironment().setVariable(VARIABLE_INDEX, obj);
			}
		}
		
		return NONE;
	}
	
}
