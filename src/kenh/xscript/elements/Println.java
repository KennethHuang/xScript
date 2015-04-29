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

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Processing;
import kenh.xscript.annotation.Text;
import kenh.xscript.impl.*;
import kenh.xscript.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;


/**
 * The system standard output with link break.
 * 
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Println extends NoChildElement {

	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_TRIM = "trim";
	
	
	public void process() throws UnsupportedScriptException {
		Object text = this.getParsedText();
		println(text);
	}
	
	public void process(@Attribute(ATTRIBUTE_VALUE) String value) throws UnsupportedScriptException {
		println(value);
	}
	
	public void process(@Attribute(ATTRIBUTE_VALUE) String value, @Attribute(ATTRIBUTE_TRIM) boolean trim ) throws UnsupportedScriptException {
		println(StringUtils.trimToEmpty(value));
	}
	
	public void process(@Attribute(ATTRIBUTE_TRIM) boolean trim) throws UnsupportedScriptException {
		Object text = this.getParsedText();
		if(text instanceof String) {
			println(StringUtils.trimToEmpty((String)text));
		}
		println(text);
	}
	
	
	private void println(Object express) throws UnsupportedScriptException {
		if(express == null) System.out.println();
		else System.out.println(express);
	}
	
}
