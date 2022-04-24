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

import kenh.xscript.Element;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Exclude;
import kenh.xscript.annotation.Reparse;
import kenh.xscript.impl.BaseElement;

import java.util.Vector;


/**
 * If element.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
@kenh.xscript.annotation.Include(value={Then.class, Else.class}, number={1,1})
@Exclude({Script.class, Method.class, Include.class})
public class If extends BaseElement {

	private static final String ATTRIBUTE_CONDITION = "cond";
	
	
	public int process(@Reparse@Attribute(ATTRIBUTE_CONDITION) boolean cond) throws UnsupportedScriptException {
		Element e = this.getChild(Then.class);
		if(e == null) {
			throw new UnsupportedScriptException(this, "Missing <then> element.");
		}
		if(cond) {
			return e.invoke();
		} else {
			e = this.getChild(Else.class);
			if(e == null) return NONE;
			else return e.invoke();
		}
		
	}
	
	private Element getChild(Class c) {
		Vector<Element> elements = this.getChildren();
		for(Element e: elements) {
			if(c.isAssignableFrom(e.getClass())) return e;
		}
		
		return null;
	}
	
}
