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

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Primal;
import kenh.xscript.annotation.Processing;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

/**
 * Method element, it can be invoked by {@code Call} element.
 * example:
 * <code>&lt;method name="getName"&gt;
 * ...
 * &lt;/method&gt;
 * &lt;method name="getName" parameter="arg1, final required arg2, required arg3"&gt;
 * ...
 * &lt;/method&gt;
 * </code>
 * 
 * @author Kenneth
 *
 */
public class Method extends NormalElement {

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_PARA = "param";
	
	
	public static final String MODI_FINAL = "final";	// modifier, like java key word 'final', mean constant.
	public static final String MODI_REQUIRED = "required"; // modifier, can't be blank
	public static final String MODI_DEFAULT = "default"; // modifier, set the default value if blank
	
	
	private String name = null;
	private String[][] parameters = null;
	
	
	public void process(@Attribute(ATTRIBUTE_NAME) String name) throws UnsupportedScriptException {
		process(name, null);
	}
	
	/**
	 * Process method of {@code Method} element.
	 * 
	 * @param name
	 * @param parameter
	 * @throws UnsupportedScriptException
	 */
	@Processing
	public void process(@Attribute(ATTRIBUTE_NAME) String name, @Primal@Attribute(ATTRIBUTE_PARA) String parameter) throws UnsupportedScriptException {
		this.name = name;
		
		if(StringUtils.isBlank(name)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "The method name is empty.");
			throw ex;
		}
		
		Map<String, Element> methods = this.getEnvironment().getMethods();
		if(methods.containsKey(name)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Reduplicate method. [" + name + "]");
			throw ex;
		}
		
		if(StringUtils.isNotBlank(parameter)) {
			String[] paras = StringUtils.split(parameter, ",");
			Vector<String[]> allParas = new Vector();
			Vector<String> existParas = new Vector(); // exist parameters
			for(String para: paras) {
				if(StringUtils.isBlank(para)) {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, "Parameter format incorrect. [" + name + ", " + parameter + "]");
					throw ex;
				}
				
				String[] all = StringUtils.split(para, " ");
				String paraName = StringUtils.trimToEmpty(all[all.length - 1]);
				if(StringUtils.isBlank(paraName) || StringUtils.contains(paraName, "{")) {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, "Parameter format incorrect. [" + name + ", " + parameter + "]");
					throw ex;
				}
				if(existParas.contains(paraName)) {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, "Reduplicate parameter. [" + name + ", " + paraName + "]");
					throw ex;
				} else {
					existParas.add(paraName);
				}
				
				Vector<String> one = new Vector();
				one.add(paraName);
				for(int i = 0; i< all.length - 1; i++) {
					String s = StringUtils.trimToEmpty(all[i]);
					if(StringUtils.isBlank(s)) continue;
					if(s.equals(MODI_FINAL) || s.equals(MODI_REQUIRED) || (s.startsWith(MODI_DEFAULT + "(") && s.endsWith(")"))) {
						one.add(s);
					} else {
						UnsupportedScriptException ex = new UnsupportedScriptException(this, "Unsupported modifier. [" + name + ", " + paraName + ", " + s + "]");
						throw ex;
					}
				}
				
				String[] one_ = one.toArray(new String[]{});
				allParas.add(one_);
			}
			
			parameters = allParas.toArray(new String[][]{});
		}
		
		// store into {methods}
		methods.put(name, this);
	}
	
	/**
	 * Invoke all children
	 * @return
	 * @throws UnsupportedScriptException
	 */
	public int processChildren() throws UnsupportedScriptException {
		return this.invokeChildren();
	}
	
	/**
	 * Get the name of method
	 * @return
	 */
	public String getName() {
		return StringUtils.isNotBlank(name)? name: this.getAttribute(ATTRIBUTE_NAME);
	}
	
	/**
	 * Retrieve the parameter. Each parameter have two parts, modifier ( final, required etc ) and parameter name.
	 * if {@code arg} without modifier, {"arg"} is return;
	 * if {@code arg} with modifier final and required, {"arg", "final", "required"} is return;
	 * 
	 * @return
	 */
	public String[][] getParameters() {
		return parameters != null ? parameters: new String[][] {};
	}
}
