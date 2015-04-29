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

import java.io.PrintStream;
import java.util.Stack;

/**
 * The exception of xScript.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class UnsupportedScriptException extends Exception {
	
	private Element element = null;
	
    public UnsupportedScriptException(Element e) {
        super();
        setElement(e);
    }
    
    public UnsupportedScriptException(Element e, String message) {
        super(message);
        setElement(e);
    }
    
    public UnsupportedScriptException(Element e, Throwable t) {
        super(t);
        setElement(e);
    }
    
    /**
     * Set the element
     * @param e
     */
    public void setElement(Element e) {
    	element = e;
    }
    
    /**
     * Get the element
     * @return
     */
    public Element getElement() {
    	return element;
    }
    
}
