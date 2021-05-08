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

/**
 * The <code>Element</code> interface represents an element in a xScript document.
 * Elements may have attributes, child elements, text and have some operations to invoke.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public interface Element {
	
	/**
	 * Adds a new attribute.
	 * @param name
	 * @param value
	 * @throws UnsupportedScriptException
	 */
	public void setAttribute(String name, String value) throws UnsupportedScriptException;
	
	/**
	 * Retrieves an attribute value.
	 * @param name
	 * @return
	 */
	public String getAttribute(String name);
	
	/**
	 * Retrieves all attributes.
	 * @return
	 */
	public Map<String, String> getAttributes();
	
	
	
	
	/**
	 * Adds a new child element.
	 * @param child
	 */
	public void addChild(Element child) throws UnsupportedScriptException;
	
	/**
	 * Retrieves all child elements.
	 * @return
	 */
	public Vector<Element> getChildren();
	
	
	
	
	/**
	 * Set the text of this element
	 * @param text
	 * @throws UnsupportedScriptException
	 */
	public void setText(String text) throws UnsupportedScriptException;
	
	/**
	 * Retrieves the text
	 * @return
	 */
	public String getText();
	
	
	
	
	/**
	 * Retrieves the parent element.
	 * @return
	 */
	public Element getParent();
	
	/**
	 * Set the parent element
	 * @param e
	 */
	public void setParent(Element e);
	
	
	
	
	/**
	 * Set the environment.
	 * @param env
	 */
	void setEnvironment(Environment env);
	
	/**
	 * Retrieves the environment.
	 * @return
	 */
	Environment getEnvironment();
	
	
	
	
	/**
	 * Invokes the operations of this element.
	 * @return  should be one of NONE, RETURN, BREAK and CONTINUE
	 */
	public int invoke() throws UnsupportedScriptException;
	
	
	
	
	/**
	 * The return value of <code>invoke</code> method. 
	 * It means nothing special.
	 */
	public static final int NONE = 0;
	
	/**
	 * The return value of <code>invoke</code> method. 
	 * Like <code>return</code> statement, return by method.
	 */
	public static final int RETURN = -1;
	
	/**
	 * Return value of <code>invoke</code> method. 
	 * Like <code>break</code> statement, jump out of loop.
	 */
	public static final int BREAK = -2;
	
	/**
	 * Return value of <code>invoke</code> method. 
	 * Like <code>continue</code> statement, remain un-invoked operation in loop and continue new loop.
	 */
	public static final int CONTINUE = -3;
	
	/**
	 * Throws an exception. 
	 */
	public static final int EXCEPTION = -999;
}
