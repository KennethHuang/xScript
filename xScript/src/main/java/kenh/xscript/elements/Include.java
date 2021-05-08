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

import kenh.xscript.annotation.*;
import kenh.xscript.impl.BaseElement;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

import java.io.*;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

/**
 * Include element, use to include another xScript.
 * 
 * @author Kenneth
 *
 */
public class Include extends NoChildElement {
	
	private static final String ATTRIBUTE_FILE = "file";
	private static final String ATTRIBUTE_URL = "url";
	
	private static final String ATTRIBUTE_LOAD_METHOD = "load-methods";
	private static final String ATTRIBUTE_LOAD_PUBLIC = "load-publics";
	private static final String ATTRIBUTE_METHODS = "methods";  // list the methods need to include
	
	@Processing
	public void processF(@Attribute(ATTRIBUTE_FILE) String file) throws UnsupportedScriptException {
		processFile(file, true, true, null);
	}
	
	@Processing
	public void processPublicsFile(@Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics) throws UnsupportedScriptException {
		processFile(file, loadPublics, false, null);
	}
	
	@Processing
	public void processMethodsFile(@Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods) throws UnsupportedScriptException {
		processFile(file, false, loadMethods, null);
	}
	
	@Processing
	public void processPublicsMethodsFile(@Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods) throws UnsupportedScriptException {
		processFile(file, loadPublics, loadMethods, null);
	}
	
	@Processing
	public void processMethodNamesFile(@Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods, @Attribute(ATTRIBUTE_METHODS) String methodNames) throws UnsupportedScriptException {
		processFile(file, false, loadMethods, methodNames);
	}
	
	@Processing
	public void processFile(@Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods, @Attribute(ATTRIBUTE_METHODS) String methodNames) throws UnsupportedScriptException {
		File f = null;
		try {
			f = new File(file);
			if(!f.isAbsolute() && this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
				Object obj = this.getEnvironment().getVariable(Constant.VARIABLE_HOME);
				if(obj instanceof String) f = new File((String)obj, file);
			}
			f = f.getCanonicalFile();
			
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		Element element = ScriptUtils.getInstance(f, this.getEnvironment());
		if(element == null) throw new UnsupportedScriptException(this, "Unabled to load script. [" + f.getPath() + "]");
		
		handleScript(element, loadPublics, loadMethods, methodNames);
	}
	
	
	@Processing
	public void processU(@Attribute(ATTRIBUTE_URL) String url) throws UnsupportedScriptException {
		processUrl(url, true, true, null);
	}
	
	@Processing
	public void processPublicsUrl(@Attribute(ATTRIBUTE_URL) String url, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics) throws UnsupportedScriptException {
		processUrl(url, loadPublics, false, null);
	}
	
	@Processing
	public void processPublicsMethodsUrl(@Attribute(ATTRIBUTE_URL) String url, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods) throws UnsupportedScriptException {
		processUrl(url, loadPublics, loadMethods, null);
	}
	
	@Processing
	public void processMethodsUrl(@Attribute(ATTRIBUTE_URL) String url, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods) throws UnsupportedScriptException {
		processUrl(url, false, loadMethods, null);
	}
	
	@Processing
	public void processMethodNamesUrl(@Attribute(ATTRIBUTE_URL) String url, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods, @Attribute(ATTRIBUTE_METHODS) String methodNames) throws UnsupportedScriptException {
		processUrl(url, false, loadMethods, methodNames);
	}
	
	
	@Processing
	public void processUrl(@Attribute(ATTRIBUTE_URL) String url, @Attribute(ATTRIBUTE_LOAD_PUBLIC) boolean loadPublics, @Attribute(ATTRIBUTE_LOAD_METHOD) boolean loadMethods, @Attribute(ATTRIBUTE_METHODS) String methodNames) throws UnsupportedScriptException {
		URL u = null;
		try {
			u = new URL(url);
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		Element element = ScriptUtils.getInstance(u, this.getEnvironment());
		if(element == null) throw new UnsupportedScriptException(this, "Unabled to load script. [" + url + "]");
		
		handleScript(element, loadPublics, loadMethods, methodNames);
	}
	
	
	private void handleScript(Element element, boolean loadPublics, boolean loadMethods, String methodNames) throws UnsupportedScriptException {
		if(!(element instanceof Script)) throw new UnsupportedScriptException(this, "The root element should be <script>. [" + element.getClass().getCanonicalName() + "]");
		
		methodNames = StringUtils.trimToEmpty(methodNames);
		String[] methods = StringUtils.split(methodNames, ",");
		
		Script s = (Script)element;
		String main = StringUtils.trimToEmpty(s.getMainMethod());
		
		Vector<Element> children = element.getChildren();
		for(Element child: children) {
			
			boolean isMethod = (child instanceof Method)? true : false;
			
			if(loadPublics && !isMethod) {
				child.invoke();
			} else if(loadMethods && isMethod) {
				String methodName = StringUtils.trimToEmpty(((Method)child).getName());
				
				if(methods != null && methods.length <= 0) {
					if(!StringUtils.equals(methodName, main)) {  // Skip main method
						child.invoke();
					}
				} else {
					
					for(String m: methods) {
						if(StringUtils.equals(methodName, StringUtils.trimToEmpty(m))) {
							child.invoke();
							break;
						}
					}
					
				}
				
			}
			
		}
	}

}
