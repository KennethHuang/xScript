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
import kenh.xscript.impl.*;
import kenh.xscript.*;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.*;

/**
 * First element of xScript document.
 * 
 * @author Kenneth
 *
 */
@kenh.xscript.annotation.Include({Set.class, Method.class, Include.class})
public class Script extends BaseElement {
	
	private static final String ATTRIBUTE_SCRIPT_NAME = "name";
	private static final String ATTRIBUTE_MAIN_METHOD = "main-method";
	
	
	private static final String VARIABLE_SCRIPT_NAME = "@xScript";
	
	
	private static final String DEFAULT_MAIN_METHOD = "main";  // default name of main method
	
	
	private String name = null;  // the name of xScript document
	private String main = null; // the name of main method. The main method is the first method to invoke
	
	
	public void process(@Attribute(ATTRIBUTE_SCRIPT_NAME) String scriptName, @Attribute(ATTRIBUTE_MAIN_METHOD) String main) throws UnsupportedScriptException {
		
		if(!this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
			String cur_dir = System.getProperty("user.dir");
			this.getEnvironment().setPublicVariable(Constant.VARIABLE_HOME, cur_dir, true);
		}
		
		if(StringUtils.isNotBlank(scriptName)) {
			this.name = scriptName;
			this.getEnvironment().setPublicVariable(VARIABLE_SCRIPT_NAME, name, true);
		}
		
		main = StringUtils.trimToEmpty(main);
		if(StringUtils.isBlank(main)) {
			main = DEFAULT_MAIN_METHOD;
		}
		
		this.main = main;
		
		processChildren(false);
		
		// find main method
		Map<String, Element> methods = this.getEnvironment().getMethods();
		Element e = methods.get(main);
		if(e == null) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Could't find the method to invoke. [" + main + "]");
			throw ex;
		}
		if(!(e instanceof Method)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Could't find the method to invoke. [" + main + "(" + e.getClass().getCanonicalName() + ")]");
			throw ex;
		}
		
		int i = ((Method)e).processChildren();
		if(i != NONE && i != RETURN) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Unsupported value is returned. [" + i + "]");
			throw ex;
		}
	}
	
	@Processing
	public void processScriptName(@Attribute(ATTRIBUTE_SCRIPT_NAME) String scriptName) throws UnsupportedScriptException {
		process(scriptName, DEFAULT_MAIN_METHOD);
	}
	
	
	public void process() throws UnsupportedScriptException {
		process(null, DEFAULT_MAIN_METHOD);
	}
	
	@Processing
	public void processMain(@Attribute(ATTRIBUTE_MAIN_METHOD) String main) throws UnsupportedScriptException {
		process(null, main);
	}
	
	/**
	 * Get the name of xScript document
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	
	private String getMain() {
		String main_ = this.getAttribute(ATTRIBUTE_MAIN_METHOD);
		return StringUtils.isNotBlank(main)? main: (StringUtils.isNotBlank(main_)? main_:DEFAULT_MAIN_METHOD);
	}
	
	/**
	 * Invoke all child elements
	 * @param skipMain  skip main method
	 * @throws UnsupportedScriptException
	 */
	public void processChildren(boolean skipMain) throws UnsupportedScriptException {
		
		String main = this.getMain();
		
		Vector<Element> children = this.getChildren();
		for(Element child: children) {
			if(child instanceof Method) {
				Method m = (Method)child;
				String methoName = m.getName();
				if(methoName != null && m.getName().equals(main)) 
					if(skipMain) continue;
			}
			child.invoke();
		}
		
	}
}
