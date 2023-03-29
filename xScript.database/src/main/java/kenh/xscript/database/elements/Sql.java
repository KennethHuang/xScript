/*
 * xScript.database ( Database for XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.database.
 * 
 * xScript.database is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript.database is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.database.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript.database.elements;

import kenh.xscript.Element;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.IgnoreSuperClass;
import kenh.xscript.annotation.Text;
import kenh.xscript.beans.ParamBean;
import kenh.xscript.database.beans.SQLBean;
import kenh.xscript.elements.Param;
import kenh.xscript.elements.Set;
import org.apache.commons.lang3.StringUtils;

import java.util.Vector;


/**
 * SQL element, to initial a sql bean.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
@kenh.xscript.annotation.Include(Param.class)
@Text(Text.Type.TRIM)
@IgnoreSuperClass
public class Sql extends Set {

	private static final String ATTRIBUTE_VARIABLE = "var";  // variable name
	
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		var = StringUtils.trimToEmpty(var);
		
		if(StringUtils.isBlank(var)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Variable name is empty.");
			throw ex;
		}
		
		Object sql_ = this.getParsedText();
		String sql = null;
		if(sql_ instanceof String) {
			sql = (String)sql_;
		}
		
		if(StringUtils.isBlank(sql)) {
			UnsupportedScriptException ex = new UnsupportedScriptException(this, "Can't find the sql.");
			throw ex;
		}
		
		SQLBean bean = new SQLBean();
		bean.setSql(sql);
		
		this.invokeChildren();
		Vector<Element> children = this.getChildren();
		
		for(Element child: children) {
			if(child instanceof Param) {
				Param c = (Param)child;
				ParamBean b = c.getParamBean();
				bean.setParameter(b);
			}
		}
		
		this.saveVariable(var, bean, null);
	}
	
}
