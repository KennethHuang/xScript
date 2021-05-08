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
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

/**
 * Call the method, invoke the elements in method.
 * example£º
 * <code>&lt;method name="methodName" param="one, two"&gt;
 * ...
 * &lt;/method&gt;
 * &lt;call name="methodName" one="ONE" two="TWO"/&gt;
 * </code>
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Call extends NoChildElement {
	
	private static final String ATTRIBUTE_METHOD_NAME = "name";
	private static final String ATTRIBUTE_RETURN_NAME = "var"; // use to save the return value
	
	
	private Map<String, String> attributes = new LinkedHashMap();
	
	
	@Override
	public void setAttribute(String name, String value) throws UnsupportedScriptException {
		attributes.put(name, value);
	}
	
	@Override
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	
	@Override
	public int invoke() throws UnsupportedScriptException {
		
		logger.info(getInfo());
		
		this.getEnvironment().removeVariable(Return.VARIABLE_RETURN); // clear {return}
		
		// 1) find Method
		Map<String, Element> methods = this.getEnvironment().getMethods();
		String name = getAttribute(ATTRIBUTE_METHOD_NAME);
		if(StringUtils.isBlank(name)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "The method name is empty.");
			throw ex;
		}
		try {
			name = (String)this.getEnvironment().parse(name);
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		String var = getAttribute(ATTRIBUTE_RETURN_NAME);
		if(StringUtils.isNotBlank(var)) {
			try {
				var = StringUtils.trimToNull((String)this.getEnvironment().parse(var));
			} catch(Exception e) {
				throw new UnsupportedScriptException(this, e);
			}
		}
		
		Element e = methods.get(name);
		if(e == null || !(e instanceof Method)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Could't find the method to invoke. [" + name + "]");
			throw ex;
		}
		
		
		// 2) handle the Method's parameter
		Method m = (Method)e;
		String[][] parameters = m.getParameters();
		
		Map<String, Object> new_vars = new LinkedHashMap();
		List<String> new_cons = new LinkedList();
		Vector parameterCallback = new Vector();
		
		for(String[] parameter: parameters) {
			String paraName = StringUtils.trimToEmpty(parameter[0]);
			if(StringUtils.isBlank(paraName)) continue;
			
			boolean required = false;
			Object defaultValue = null;
			
			if(parameter.length > 1) {
				for(int i=1; i< parameter.length; i++) {
					String modi = StringUtils.trimToEmpty(parameter[i]);
					if(modi.equals(Method.MODI_FINAL)) {
						new_cons.add(paraName);
					}
					if(modi.equals(Method.MODI_REQUIRED)) {
						required = true;
					}
					if((modi.startsWith(Method.MODI_DEFAULT + "(") && modi.endsWith(")"))) {
						String defaultValue_ = StringUtils.substringAfter(modi, Method.MODI_DEFAULT + "(");
						defaultValue_ = StringUtils.substringBeforeLast(defaultValue_, ")");
						try {
							defaultValue = this.getEnvironment().parse(defaultValue_);
						} catch(UnsupportedExpressionException e_) {
							UnsupportedScriptException ex = new UnsupportedScriptException(this, e_);
							throw ex;
						}
					}
				}
			}
				
			String paraValue = this.getAttribute(paraName);
			Object paraObj = null;
			if(paraValue == null) {
				
				if(required) {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, "Missing parameter. [" + paraName + "]");
					throw ex;
				} else {
					if(defaultValue != null) {
						new_vars.put(paraName, defaultValue);
					}
				}
				
			} else {
				try {
					paraObj = this.getEnvironment().parse(paraValue);
					new_vars.put(paraName, paraObj);
					if(paraObj instanceof kenh.expl.Callback) {
						parameterCallback.add(paraObj);						
					}
					
				} catch(UnsupportedExpressionException ex) {
					throw new UnsupportedScriptException(this, ex);
				}
			}
			
		}
		
		if(this.getAttributes().size() > new_vars.size() + (this.getAttributes().containsKey(ATTRIBUTE_RETURN_NAME)? 2 : 1)) {
			java.util.Set<String> keys = this.getAttributes().keySet();
			String additional = "";
			for(String key: keys) {
				if(key.equals(ATTRIBUTE_METHOD_NAME)) continue;
				if(key.equals(ATTRIBUTE_RETURN_NAME)) continue;
				if(new_vars.containsKey(key)) continue;
				additional = additional + key + ", ";
			}
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Unknown parameter. [" + StringUtils.substringBeforeLast(additional, ",") + "]");
			throw ex;
		}
		
		
		// 3) remove non-public variable in Environment. save Method's parameter in Environment.
		List<String> publics = this.getEnvironment().getPublics();
		List<String> constant = this.getEnvironment().getContants();
		
		Map<String, Object> keep_vars = new LinkedHashMap();
		List<String> keep_cons = new LinkedList();
		List<String> keep_pubs = new LinkedList();
		java.util.Set<String> keys = this.getEnvironment().getVariables().keySet();
		for(String key: keys) {
			if(!publics.contains(key)) {
				keep_vars.put(key, this.getEnvironment().getVariable(key));
				if(constant.contains(key)) keep_cons.add(key);
			}
		}

		keys = keep_vars.keySet();
		for(String key: keys) {
			if(constant.contains(key)) constant.remove(key);
			this.getEnvironment().removeVariable(key, false);
		}
		
		// public variable in Environment have the same name with Method's parameter
		for(String[] parameter: parameters) {
			String key = StringUtils.trimToEmpty(parameter[0]);
			if(this.getEnvironment().containsVariable(key)) {
				if(constant.contains(key)) {
					constant.remove(key);
					keep_cons.add(key);
				}
				publics.remove(key);
				keep_pubs.add(key);
				keep_vars.put(key, this.getEnvironment().removeVariable(key, false));
			}
			this.getEnvironment().setVariable(key, new_vars.get(key));
			if(new_cons.contains(key)) constant.add(key);
		}
		
		
		// 4)invoke the Method's child elements.
		int r = m.processChildren();
		if(r != RETURN && r != NONE) {
			if(r == EXCEPTION) {
				Object ex = this.getEnvironment().getVariable(Constant.VARIABLE_EXCEPTION);
				if(ex instanceof Exception) {
					if(ex instanceof UnsupportedScriptException) {
						throw (UnsupportedScriptException)ex;
					} else {
						UnsupportedScriptException ex_ = new UnsupportedScriptException(this, (Exception)ex);
						throw ex_;
					}
				}
				
			}
			
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Unsupported value is returned. [" + r + "]");
			throw ex;
		}
		
		Object returnObj = null;
		if(StringUtils.isNotBlank(var)) {
			if(r == RETURN) {
				returnObj = this.getEnvironment().getVariable(Return.VARIABLE_RETURN);
			} else {
				UnsupportedScriptException ex = new UnsupportedScriptException(this, "The method does not have return value. [" + name + "]");
				throw ex;
			}
		}
		
		
		// 5) remove non-public variable from Environment. restore original variables
		List<String> remove_vars = new LinkedList();
		keys = this.getEnvironment().getVariables().keySet();
		for(String key: keys) {
			if(!publics.contains(key)) {
				remove_vars.add(key);
			}
		}
		
		for(String key: remove_vars) {
			if(constant.contains(key)) constant.remove(key);
			Object obj = this.getEnvironment().getVariable(key);
			if(parameterCallback.contains(obj)) {
				this.getEnvironment().removeVariable(key, false);
			} else {
				this.getEnvironment().removeVariable(key);
			}
		}
		
		keys = keep_vars.keySet();
		for(String key: keys) {
			if(!constant.contains(key)) {
				if(!this.getEnvironment().containsVariable(key)) this.getEnvironment().setVariable(key, keep_vars.get(key));
			}
			if(keep_cons.contains(key)) constant.add(key);
			if(keep_pubs.contains(key)) publics.add(key);
		}
		
		
		// 6) store {return}
		if(returnObj != null) {
			this.saveVariable(var, returnObj, null);
		}
		
		return NONE;
	}

}
