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

package kenh.expl.impl;

import kenh.expl.Environment;
import kenh.expl.UnsupportedExpressionException;
import org.apache.commons.jexl3.*;

/**
 * The parser use JEXL.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class JEXLParser extends BaseParser {
	
	private JEXLEnvironment jenv = null;
	
	private JexlEngine jexl = new JexlBuilder().strict(false).create();
	
	public JEXLParser() {	}
	
	@Override
	public Object parse(String express) throws UnsupportedExpressionException {
		
		if(jenv == null) return null;
		
		JexlExpression exp = jexl.createExpression( express );
        try {
        	
        	Object obj = exp.evaluate(jenv);
        	return obj;
    		
        } catch(JexlException e) {
        	UnsupportedExpressionException ex = new UnsupportedExpressionException(e);
        	ex.push(express);
        	throw ex;
		} catch(Exception e) {
			UnsupportedExpressionException ex = new UnsupportedExpressionException(e);
			ex.push(express);
			throw ex;
		}
        
	}
	
	@Override
	public void setEnvironment(Environment env) {
		super.setEnvironment(env);
		jenv = new JEXLEnvironment(env);
	}
	
	/**
	 * The environment for JEXL.
	 * 
	 * @author Kenneth
	 * @since 1.0
	 * @see JexlContext
	 *
	 */
	class JEXLEnvironment implements JexlContext {
		
		private Environment env;
		
		JEXLEnvironment(Environment env) {
			this.env = env;
		}
		
		@Override
		public Object get(String name) {
			return env.getVariable(name);
		}

		@Override
		public void set(String name, Object value) {
			env.setVariable(name, value);
		}

		@Override
		public boolean has(String name) {
			return env.containsVariable(name);
		}
		
	}
}
