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

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Primal;
import kenh.xscript.impl.NormalElement;

/**
 * While element.
 * 
 * @author Kenneth
 *
 */
public class While extends NormalElement {

	private static final String ATTRIBUTE_CONDITION = "cond";
	private static final String ATTRIBUTE_AFTER = "after";
	
	
	private static final String VARIABLE_INDEX = "___index___";  // current loop times 
	
	
	private static final int MAX_LOOP = 100000;
	
	
	public int process(@Primal@Attribute(ATTRIBUTE_CONDITION) String cond) throws UnsupportedScriptException {
		return process(cond, false);
	}
	
	public int process(@Primal@Attribute(ATTRIBUTE_CONDITION) String cond, @Attribute(ATTRIBUTE_AFTER) boolean after) throws UnsupportedScriptException {
		
		Object obj = null;
		if(this.getEnvironment().containsVariable(VARIABLE_INDEX)) {
			obj = this.getEnvironment().removeVariable(VARIABLE_INDEX);
		}
		
		int i = 0;
		
		try {
			
			while(true) {
				
				i++;
				this.getEnvironment().setVariable(VARIABLE_INDEX, i);
				if(i > MAX_LOOP) {
					throw new UnsupportedScriptException(this, "Endless Loop? ( > " + MAX_LOOP + ")");
				}
				
				if(i == 1 && after) {
					// active like do-while
				} else {
					String cond_ = "{" + cond + "}";
					if(!getCondition(cond_)) {
						break;
					}
				}
				
				int result = invokeChildren();
				if(result == BREAK) {
					break;
				} else if(result == CONTINUE) {
					continue;
				} else if(result != NONE) {
					return result;
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
	

	private boolean getCondition(String cond) throws UnsupportedScriptException {
		boolean condition = false;
		try {
			Object obj = this.getEnvironment().parse(cond);
			if(obj instanceof Boolean) {
				condition = ((Boolean)obj).booleanValue();
			} else if(obj instanceof String) {
				obj = this.getEnvironment().convert((String)obj, Boolean.class);
				if(obj != null) {
					condition = ((Boolean)obj).booleanValue();
				}
			} else {
				throw new UnsupportedScriptException(this, "Condition is incorrect. [" + obj == null? "NULL" : obj.getClass().getCanonicalName() + "]");
			}
			
			
		} catch(UnsupportedExpressionException e) {
			throw new UnsupportedScriptException(this, e);
		} catch(UnsupportedScriptException e) {
			throw e;
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		return condition;
	}
}
