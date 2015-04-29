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

/**
 * Include element, use to include another xScript.
 * 
 * @author Kenneth
 *
 */
public class Include extends NoChildElement {
	
	private static final String ATTRIBUTE_FILE = "file";
	private static final String ATTRIBUTE_URL = "url";
	
	
	@Processing
	public void processFile(@Attribute(ATTRIBUTE_FILE) String file) throws UnsupportedScriptException {
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
		
		handleScript(element);
	}
	
	@Processing
	public void processUrl(@Attribute(ATTRIBUTE_URL) String url) throws UnsupportedScriptException {
		URL u = null;
		try {
			u = new URL(url);
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		Element element = ScriptUtils.getInstance(u, this.getEnvironment());
		if(element == null) throw new UnsupportedScriptException(this, "Unabled to load script. [" + url + "]");
		
		handleScript(element);
	}
	
	
	private void handleScript(Element element) throws UnsupportedScriptException {
		if(!(element instanceof Script)) throw new UnsupportedScriptException(this, "The root element should be <script>. [" + element.getClass().getCanonicalName() + "]");
		
		Script s = (Script)element;
		s.processChildren(true);
	}

}
