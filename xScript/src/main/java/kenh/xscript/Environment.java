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

package kenh.xscript;

import java.util.*;

import kenh.expl.Parser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The subclass of ExpL's environment, to support xScript.
 *
 * @author Kenneth
 * @since 1.0
 *
 */
public class Environment extends kenh.expl.Environment {

	/**
	 * The element package loaded by system properties should use this prefix.
	 */
	private static final String ELEMENTS_PATH_PREFIX = "kenh.xscript.element.packages";

	/**
	 * All element packages where xScript find the <code>Element</code>.
	 */
	private Map<String, String> elementPackages =  Collections.synchronizedMap(new LinkedHashMap());

	/**
	 * The variable name of method elements.
	 * It lists all methods element.
	 */
	private static final String KEY_METHODS = "{methods}";

	/**
	 * The variable name of public variables.
	 * It lists all public variables ( the variables have public scope ).
	 */
	private static final String KEY_PUBLIC = "{public}";

	/**
	 * The variable name of constant variables.
	 * It lists all constants ( can't change the value ).
	 */
	private static final String KEY_CONSTANT = "{constant}";


	/**
	 * Constructor
	 */
	public Environment() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param parser
	 */
	public Environment(Parser parser) {
		this(parser, null);
	}

	/**
	 * Constructor
	 * @param parser
	 * @param logger
	 */
	public Environment(Parser parser, Log logger) {
		super(parser, logger);
		if(logger == null) {
			this.setLogger(LogFactory.getLog(Environment.class));
		}

		setElementPackage("xScript", "kenh.xscript.elements"); // the base package for elements
		setElementPackage("xScriptLog", "kenh.xscript.elements.log"); // the base package for elements
		setFunctionPackage("xScript", "kenh.xscript.functions"); // the function package of xScript

		loadElementPackages_SystemProperties();
		loadElementPackages_Extension();

		initial();
	}

	/**
	 * Initial method
	 */
	private void initial() {

		// store methods
		Map<String, Element> methods = Collections.synchronizedMap(new LinkedHashMap());
		this.setVariable(KEY_METHODS, methods);

		// public variables
		List<String> publics = Collections.synchronizedList(new LinkedList());
		this.setVariable(KEY_PUBLIC, publics);

		// constant
		List<String> constant = Collections.synchronizedList(new LinkedList());
		this.setVariable(KEY_CONSTANT, constant);

		constant.add(KEY_METHODS);
		constant.add(KEY_PUBLIC);
		constant.add(KEY_CONSTANT);

		publics.add(KEY_METHODS);
		publics.add(KEY_PUBLIC);
		publics.add(KEY_CONSTANT);

	}

	/**
	 * Retrieves the constants.
	 * @return
	 */
	public List<String> getContants() {
		Object obj = getVariable(KEY_CONSTANT);
		try {
			return (List<String>)obj;
		} catch(Exception e) {
			return new LinkedList<String>();
		}
	}

	/**
	 * Retrieves the public variables.
	 * @return
	 */
	public List<String> getPublics() {
		Object obj = getVariable(KEY_PUBLIC);
		try {
			return (List<String>)obj;
		} catch(Exception e) {
			return new LinkedList<String>();
		}
	}

	/**
	 * Retrieves all method elements
	 * @return
	 */
	public Map<String, Element> getMethods() {
		Object obj = getVariable(KEY_METHODS);
		try {
			return (Map<String, Element>)obj;
		} catch(Exception e) {
			return new LinkedHashMap<String, Element>();
		}
	}

	/**
	 * Load element packages from system properties.
	 * If system property has name starts with <code>kenh.xscript.element.packages</code>,
	 * it will be loaded by xScript.
	 */
	private void loadElementPackages_SystemProperties() {
		Properties p = System.getProperties();
		Set keys = p.keySet();
		for(Object key_: keys) {
			if(key_ instanceof String) {
				String key = (String)key_;
				if(StringUtils.startsWith(key, ELEMENTS_PATH_PREFIX + ".")) {
					String name = StringUtils.substringAfter(key, ELEMENTS_PATH_PREFIX + ".");
					String funcPackage = p.getProperty(key);
					setElementPackage(name, funcPackage);
				}
			}
		}
	}

	/**
	 * Load element packages through <code>Extension</code>.
	 */
	private void loadElementPackages_Extension() {
		ServiceLoader<Extension> es = ServiceLoader.load(Extension.class);
		for(Extension e: es) {
			if(e!= null) {
				Map<String, String> p = e.getElementPackages();
				if(p != null && p.size() > 0) {
					Set<String> keys = p.keySet();
					for(String key: keys) {
						String elementPackage = p.get(key);
						setElementPackage(key, elementPackage);
					}
				}
			}
		}
	}

	/**
	 * Adds a new element package.
	 * @param nameSpace  the name of element package
	 * @param elementPackage
	 * @return
	 */
	public boolean setElementPackage(String nameSpace, String elementPackage) {
		if(StringUtils.isBlank(nameSpace)) return false;
		if(StringUtils.isBlank(elementPackage)) return false;

		if(elementPackages.containsKey(nameSpace)) {
			String p = elementPackages.get(nameSpace);
			if(p.equals(elementPackage)) return true;
			else return false;

		} else {
			elementPackages.put(nameSpace, elementPackage);
			return true;
		}
	}

	/**
	 * Initials an element through element package and element name.
	 * @param elementPackage
	 * @param elementName
	 * @return
	 */
	private Element getElement_(String elementPackage, String elementName) {
		if(StringUtils.isBlank(elementPackage)) return null;
		if(StringUtils.isBlank(elementName)) return null;

		try {
			if(StringUtils.isNotBlank(elementPackage)) {
				Element element = (Element)Class.forName(elementPackage + "." + StringUtils.capitalize(elementName)).newInstance();
				return element;
			}
		} catch(Exception e) {
		}
		return null;
	}

	/**
	 * Initials an element.
	 * This mehtod will loop all element packages,
	 * retrieves the first element that matching the name.
	 * @param elementName
	 * @return
	 */
	public Element getElement(String elementName) {
		if(StringUtils.isBlank(elementName)) return null;

		Iterator<String> iterator = elementPackages.values().iterator();
		while(iterator.hasNext()) {
			String elementPackage = iterator.next();
			Element element = getElement_(elementPackage, elementName);
			if(element != null) return element;
		}
		return null;

	}

	/**
	 * Initials an element by the name space of element.
	 * @param elementNS
	 * @param elementName
	 * @return
	 */
	public Element getElement(String elementNS, String elementName) {
		if(StringUtils.isBlank(elementName)) return null;

		if(StringUtils.isNotBlank(elementNS)) {
			if(elementPackages.containsKey(elementNS)) {
				return getElement_(elementPackages.get(elementNS), elementName);
			} else {
				// use name space
				return getElement_(elementNS, elementName);
			}
		} else {
			return getElement(elementName);
		}

	}

	@Override
	public Object removeVariable(String key) {
		return removeVariable(key, true);
	}

	/**
	 * Constant variable can't be removed.
	 */
	@Override
	public Object removeVariable(String key, boolean callback) {
		List<String> constant = this.getContants();
		if(constant!=null && constant.contains(key)) throw new UnsupportedOperationException("[" + key + "] is a constant.");

		Object obj = super.removeVariable(key, callback);

		List<String> publics = this.getPublics();
		if(publics.contains(key)) {
			publics.remove(key);
			this.getLogger().trace("[XSCRIPT] Remove public variable: " + key);
		}

		return obj;
	}


	/**
	 * Adds a constant.
	 * @param key
	 * @param obj
	 * @param isConstant  is constant variable or nor.
	 */
	public void setVariable(String key, Object obj, boolean isConstant) throws UnsupportedOperationException {
		this.setVariable(key, obj);

		if(isConstant) {
			List<String> constant = (List<String>)this.getVariable(KEY_CONSTANT);
			if(!constant.contains(key)){
				constant.add(key);
				this.getLogger().trace("[XSCRIPT] Add constant variable: " + key);
			}
		}
	}

	/**
	 * Adds a public variable.
	 * @param key
	 * @param obj
	 * @param isConstant  is constant variable or nor.
	 */
	public void setPublicVariable(String key, Object obj, boolean isConstant) throws UnsupportedOperationException {
		this.setVariable(key, obj);

		List<String> publics = (List<String>)this.getVariable(KEY_PUBLIC);
		if(!publics.contains(key)) {
			publics.add(key);
			this.getLogger().trace("[XSCRIPT] Add public variable: " + key);
		}

		if(isConstant) {
			List<String> constant = (List<String>)this.getVariable(KEY_CONSTANT);
			if(!constant.contains(key)) {
				constant.add(key);
				this.getLogger().trace("[XSCRIPT] Add constant variable: " + key);
			}
		}
	}

	/**
	 * Constant variable can't change.
	 */
	@Override
	public void setVariable(String key, Object obj) throws UnsupportedOperationException {
		List<String> constant = (List<String>)this.getVariable(KEY_CONSTANT);
		if(constant!=null && constant.contains(key)) throw new UnsupportedOperationException("[" + key + "] is a constant.");

		super.setVariable(key, obj);
	}


	/**
	 * Check variable is public variable or not.
	 * @param key
	 * @return
	 */
	public boolean isPublicVariable(String key) {
		List<String> list = getPublics();
		return list.contains(key);
	}


	/**
	 * Check variable is constant or not.
	 * @param key
	 * @return
	 */
	public boolean isConstant(String key) {
		List<String> list = this.getContants();
		return list.contains(key);
	}


	@Override
	public void callback() {
		super.callback();

		initial();
	}

}