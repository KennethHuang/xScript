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

import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Text;
import kenh.xscript.impl.NoChildElement;
import org.apache.commons.lang3.StringUtils;


/**
 * Logs a message object with the INFO level.
 * 
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Log extends NoChildElement {

	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_TRIM = "trim";

	public void process() throws UnsupportedScriptException {
		Object text = this.getParsedText();
		log(text);
	}
	
	public void process(@Attribute(ATTRIBUTE_VALUE) String value) throws UnsupportedScriptException {
		log(value);
	}
	
	public void process(@Attribute(ATTRIBUTE_VALUE) String value, @Attribute(ATTRIBUTE_TRIM) boolean trim ) throws UnsupportedScriptException {
		log(StringUtils.trimToEmpty(value));
	}
	
	public void process(@Attribute(ATTRIBUTE_TRIM) boolean trim) throws UnsupportedScriptException {
		Object text = this.getParsedText();
		if(text instanceof String) {
			log(StringUtils.trimToEmpty((String)text));
		}
		log(text);
	}

	private void log(Object express) {
		if(express == null) return;
		else this.getLogger().info(express);
	}
	
}
