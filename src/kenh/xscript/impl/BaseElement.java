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

package kenh.xscript.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.*;
import kenh.xscript.annotation.*;
import kenh.xscript.elements.Script;

/**
 * Provide base function for all elements
 * 
 * @author Kenneth
 *
 */
public abstract class BaseElement implements Element {

	private Map<String, String> attributes = new LinkedHashMap(); // store attributes
	
	private Vector<Element> children = new Vector(); // store child elements
	
	private String text = null;
	
	private Environment env = null;
	
	private Element parent = null;
	
	/**
	 * the attribute group that this element support
	 * if element have two attribute group: (a, b), (a, c), then this element
	 * is support a & c, not support b & c or a only.
	 * 
	 */
	private Vector<String[]> allAttributeAnnotation = null; 
	
	protected static final Log logger = LogFactory.getLog(Element.class.getName());
	
	/**
	 * Find attribute annotations for all process method
	 */
	private void readAttributeAnnotation() {
		if(allAttributeAnnotation != null) return;
		
		allAttributeAnnotation = new Vector();
		
		Method[] methods = this.getClass().getMethods();
		
		for(Method method: methods) {
			String name = method.getName();
			Annotation process = method.getAnnotation(Processing.class);
			Annotation[][] annotations = method.getParameterAnnotations();
			
			if(process != null || name.equals(METHOD)) {
				
				boolean find = true;
				String[] group = new String[annotations.length];
				for(int i=0; i< annotations.length; i++) {
					Annotation[] anns = annotations[i];
					if(anns == null) {
						find = false;
						break;
					}
					String attributeName = null;
					for(Annotation ann: anns) {
						if(ann instanceof Attribute) {
							attributeName = ((Attribute)ann).value();
							break;
						}
					}
					
					if(StringUtils.isBlank(attributeName)) {
						find = false;
						break;
					}
					
					group[i] = attributeName;
					
				}
				if(find) {
					
					allAttributeAnnotation.add(group);
					
				}
			}
		}
	}
	
	@Override
	public void setAttribute(String name, String value) throws UnsupportedScriptException {
		if(StringUtils.isBlank(name)) throw new UnsupportedScriptException(this, "Attribute is empty.");
		if(attributes.containsKey(name)) throw new UnsupportedScriptException(this, "Reduplicate attribute. [" + name + "]");
		
		attributes.put(name, value);
		
		readAttributeAnnotation();
		
		// Check attributes, if current attributes combination is suit for process method's attribute group.
		for(String[] group: allAttributeAnnotation) {
			int i = 0;
			for(String key: group) {
				
				if(attributes.containsKey(key)) {
					i++;
				}
			}
			if(i == attributes.size()) return;
		}
		
		Set<String> keys = attributes.keySet();
		String keyStr = "";
		for(String key: keys) {
			keyStr = keyStr + key +  ",";
		}
		keyStr = StringUtils.substringBeforeLast(keyStr, ",");
		
		throw new UnsupportedScriptException(this, "Do not support this attribute group. [" + keyStr + "]");
	}
	
	@Override
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	
	/**
	 * Check child element is allowed to add.
	 * @param e
	 * @return
	 */
	private boolean checkChildElement(Element e) {
		if(e == null) return false;
		
		Class class_ = e.getClass();
		
		if(isExcludedChildElement(this, e.getClass())) {
			return false;
		}
		
		if(isIncludeChildElement(this, e.getClass())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check {@code Exclude} annotation, if child element's class is in exclude
	 * list, this child element is not allowed to add.
	 * If {@code Exclude} annotation do not define, all child element is allowed.
	 * 
	 * @param c
	 * @return true  this child element is not allowed to add.
	 */
	private static boolean isExcludedChildElement(Element el, Class c) {
		if(!isElement(c)) return true;  // if not an Element
		
		Exclude e = el.getClass().getAnnotation(Exclude.class);
		if(e == null) return false;
		
		Class[] classes = e.value();
		if(classes == null || classes.length <= 0) return false;
		
		for(Class class_: classes) {
			if(class_.isAssignableFrom(c)) return true;
		}
		
		return false;
	}
	
	/**
	 * Check {@code Include} annotation, if child element's class is in include
	 * list, this child element is allowed to add.
	 * If {@code Include} annotation do not define, all child element is allowed.
	 * 
	 * @param c
	 * @return true  this child element is allowed to add.
	 */
	private static boolean isIncludeChildElement(Element el, Class c) {
		if(!isElement(c)) return false;  // if not an Element
		
		Include e = el.getClass().getAnnotation(Include.class);
		if(e == null) return true;
		
		Class[] classes = e.value();
		if(classes == null || classes.length <= 0) return true;
		
		int[] numbers = e.number();
		int i = 0;
		for(Class class_: classes) {
			int number = 0;
			if(i < numbers.length) number = numbers[i];
			i++;
			if(class_.isAssignableFrom(c)) return checkChildrenElementAmount(el, number, class_);
		}
		
		return false;
	}
	
	/**
	 * Check amount of child element with certain class type.
	 * 
	 * @see kenh.xscript.annotation.Include#number()
	 * @param number 
	 * @param c
	 * @return  true, this amount is less than {@code number}; false, this amount is more than {@code number}
	 */
	private static boolean checkChildrenElementAmount(Element el, int number, Class c) {
		if(number <= 0) return true;
		
		int i = number;
		Vector<Element> children = el.getChildren();
		for(Element child: children) {
			if(c.isAssignableFrom(child.getClass())) i--;
		}
		
		if(i <=0) return false;
		
		return true;
	}
	
	/**
	 * Check element is a sub class of {@code Element}.
	 * @param c
	 * @return
	 */
	private static boolean isElement(Class c) {
		if(c == null) return false;
		
		return Element.class.isAssignableFrom(c);
	}
	
	
	@Override
	public void addChild(Element child) throws UnsupportedScriptException {
		if(child == null) throw new UnsupportedScriptException(this, "Element is empty.");
		if(children.contains(child)) throw new UnsupportedScriptException(this, "Reduplicate element. [" + child.toString() + "]");
		
		if(checkChildElement(child)) {
			children.add(child);
			child.setParent(this);
		} else {
			throw new UnsupportedScriptException(child, "Do not support this child element. [" + child.getClass().getCanonicalName() + "]");
		}
		
	}

	@Override
	public Vector<Element> getChildren() {
		return children;
	}

	@Override
	public void setText(String text) throws UnsupportedScriptException {
		Text t = this.getClass().getAnnotation(Text.class);
		if(t != null && t.value() != Text.Type.NONE) {
			this.text = text;
			return;
		}
		throw new UnsupportedScriptException(this, "Do not support text.");
	}

	@Override
	public String getText() {
		Text t = this.getClass().getAnnotation(Text.class);
		if(t != null) {
			Text.Type type = t.value();
			if(type == Text.Type.FULL) {
				return text;
			} else if(type == Text.Type.TRIM) {
				return StringUtils.trimToEmpty(text);
			}
			
		}
		return null;
	}
	
	/**
	 * Get the parsed text.
	 * @return
	 */
	protected Object getParsedText() throws UnsupportedScriptException {
		if(this.getText() == null) return "";
		
		String text = this.getText();
		Text t = this.getClass().getAnnotation(Text.class);
		if(t != null) {
			Text.Type type = t.value();
			
			if(type == Text.Type.FULL) {
				try {
					return this.getEnvironment().parse(text);
				} catch(UnsupportedExpressionException e) {
					throw new UnsupportedScriptException(this, e);
				}
				
			} else if(type == Text.Type.TRIM) {
				try {
					text = StringUtils.trimToEmpty(text);
					Object obj = this.getEnvironment().parse(text);
					if(obj instanceof String) {
						return StringUtils.trimToEmpty((String)obj);
					} else {
						return obj;
					}
				} catch(UnsupportedExpressionException e) {
					throw new UnsupportedScriptException(this, e);
				}
			}
			
			return "";
		} else {
			return "";
		}
		
	}

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}

	@Override
	public Environment getEnvironment() {
		return env;
	}
	
	/**
	 * the name of default process method
	 */
	private static final String METHOD = "process";	
	
	@Override
	public int invoke() throws UnsupportedScriptException {
		
		logger.info(getInfo());
		
		Annotation ignoreSuperClass = this.getClass().getAnnotation(IgnoreSuperClass.class);
		
		Method[] methods = this.getClass().getMethods();
		if(ignoreSuperClass != null) methods = this.getClass().getDeclaredMethods();
		
		for(Method method: methods) {
			
			String name = method.getName();
			Class[] classes = method.getParameterTypes();
			Annotation primal = method.getAnnotation(Primal.class);
			Annotation process = method.getAnnotation(Processing.class);
			Annotation[][] annotations = method.getParameterAnnotations();
			
			if(process == null && !name.equals(METHOD)) continue;
			
			boolean parsedAll = true;
			if(primal != null) parsedAll = false;
			
			
			boolean find = true; // true, find the suitable method to invoke
			
			Object[] objs = new Object[annotations.length];
			
			if(annotations.length == 0) { // non-parameter method
				if(attributes.size() == 0) find = true;
				else {
					logger.trace("Failure(no parameter required): " + method.toGenericString());
					find = false;
				}
			} else { 
				
				if(annotations.length != attributes.size()) continue;
				
				for(int i=0; i< annotations.length; i++) {
					
					boolean parsed = parsedAll; // parse all attribute
					boolean reparse = false;  // has Reparse annotation
					
					Annotation[] anns = annotations[i];
					Class class2 = classes[i];
					
					if(anns == null) {
						logger.trace("Failure(parameter without annotation): " + method.toGenericString());
						find = false;
						break;
					}
					String attributeName = null;
					for(Annotation ann: anns) {
						if(ann instanceof Attribute) {
							attributeName = ((Attribute)ann).value();
						}
						if(ann instanceof Primal) {
							parsed = false;
						}
						if(ann instanceof Reparse) {
							reparse = true;
						}
					}
					
					if(StringUtils.isBlank(attributeName)) {
						logger.trace("Failure(annotation value is empty): " + method.toGenericString());
						find = false;
						break;
					}
					if(!attributes.containsKey(attributeName)) {
						logger.trace("Failure(can't find attribute[" + attributeName + "]): " + method.toGenericString());
						find = false;
						break;
					}
					
					Object attrValue = null;
					try {
						// parse the parameter of process method
						if(parsed) {
							if(reparse) {
								attrValue = env.parse("{" + attributes.get(attributeName) + "}");
							} else {
								attrValue = env.parse(attributes.get(attributeName));
							}
						}
						else {
							attrValue = attributes.get(attributeName);
						}
						
					} catch(UnsupportedExpressionException e) {
						logger.trace("Failure(error[" + attributeName + ", " + e.getMessage() + "]): " + method.toGenericString());
						find = false;
						//break;
						throw new UnsupportedScriptException(this, e);
					}
					Class class1 = attrValue.getClass();
					
					if(class2.isAssignableFrom(class1) || class2 == Object.class) {
						objs[i] = attrValue;
						
					} else if(class1 == String.class) {
						try {
							Object obj = Environment.convert((String)attrValue, class2);
							if(obj == null) {
								logger.trace("Failure(Convert failure[" + attributeName + ", null]): " + method.toGenericString());
								find = false;
								break;
							} else {
								objs[i] = obj;
							}
						} catch(Exception e) {
							logger.trace("Failure(Convert exception[" + attributeName + "]): " + method.toGenericString());
							find = false;
							break;
							//UnsupportedScriptException ex = new UnsupportedScriptException(this, e);
							//throw ex;
						}
					} else {
						logger.trace("Failure(Unsupported class[" + attributeName + ", " + class1 + ", " + class2 + "]): " + method.toGenericString());
						find = false;
						break;
					}
				}
			}
					
			if(find) {
				try {
					logger.debug("Invoke: " + method.toGenericString());
					if( method.getReturnType() == int.class ) {
						return (Integer)method.invoke(this, objs);
					} else if (  method.getReturnType() == Integer.class ) {
						return (Integer)method.invoke(this, objs);
					} else {
						method.invoke(this, objs);
						return NONE;
					}
				} catch(InvocationTargetException e) {
					Throwable t = e.getTargetException();
					if(t instanceof UnsupportedScriptException) {
						throw (UnsupportedScriptException)t;
					} else {
						UnsupportedScriptException ex = new UnsupportedScriptException(this, e);
						throw ex;
					}
					
				} catch(Exception e) {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, e);
					throw ex;
				}
			}
		}
		
		throw new UnsupportedScriptException(this, "Can't fine the method to process.");
	}

	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element e) {
		this.parent = e;
	}
	
	/**
	 * Invoke all child elements
	 * @return
	 * @throws UnsupportedScriptException
	 */
	protected int invokeChildren() throws UnsupportedScriptException {
		Vector<Element> children = this.getChildren();
		
		for(Element child: children) {
			int r = child.invoke();
			if(r != 0) return r;
		}
		
		return NONE;
	}
	
	/**
	 * Get the element information ( name and attribute ), this method is used to debug.
	 * @return
	 */
	protected String getInfo() {
		StringBuffer info = new StringBuffer();
		
		info.append("<" + this.getClass().getCanonicalName());
		
		// Êä³öÊôÐÔ
		Map<String, String> attributes = this.getAttributes();
		if(attributes != null && attributes.size() > 0) {
			Set<String> keys = attributes.keySet();
			for(String key: keys) {
				info.append(" " + key + "=\"" + attributes.get(key) + "\"");
			}
		}
		
		info.append(">");
		
		return info.toString();
	}
	
	
	public static final String MODI_FINAL = "final";	// modifier, like java key word 'final', mean constant.
	public static final String MODI_PUBLIC = "public"; // modifier, like java key word 'public'
	
	protected void saveVariable(String var, Object value, Object defaultValue) throws UnsupportedScriptException {
		
		boolean isFinal = false;
		boolean isPublic = false;
		if(this.getParent() instanceof Script) isPublic = true;
		
		String varName = var;
		if(StringUtils.contains(var, " ")) {
			String[] all = StringUtils.split(var, " ");
			varName = StringUtils.trimToEmpty(all[all.length - 1]);
			if(StringUtils.isBlank(varName)) {
				UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable format incorrect. [" + var + "]");
				throw ex;
			}
			
			for(int i = 0; i< all.length - 1; i++) {
				String s = StringUtils.trimToEmpty(all[i]);
				if(StringUtils.isBlank(s)) continue;
				if(s.equals(MODI_FINAL)) {
					isFinal = true;
				}else if(s.equals(MODI_PUBLIC)) {
					isPublic = true;
				} else {
					UnsupportedScriptException ex = new UnsupportedScriptException(this, "Unsupported modifier. [" + var + ", " + s + "]");
					throw ex;
				}
			}
		}
		
		
		// if value if empty, use default value.
		if(value == null || ( ( value instanceof String) && ((String)value).equals("") )) {
			if(defaultValue != null) value = defaultValue;
		}
		
		setVariable(varName, value, isFinal, isPublic);
	}
	
	/**
	 * Store the variable
	 * 
	 * @param var
	 * @param value
	 * @param isFinal
	 * @param isPublic
	 * @throws UnsupportedScriptException
	 */
	protected void setVariable(String var, Object value, boolean isFinal, boolean isPublic) throws UnsupportedScriptException {
		try {
			if(isPublic) this.getEnvironment().setPublicVariable(var, value, isFinal);
			else this.getEnvironment().setVariable(var, value, isFinal);
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	
}
