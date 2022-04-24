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
import kenh.xscript.Environment;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Primal;
import kenh.xscript.annotation.Processing;
import kenh.xscript.annotation.Text;
import kenh.xscript.beans.ParamBean;
import kenh.xscript.impl.NoChildElement;

/**
 * Provide the parameter for parent element.
 * 
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Param extends NoChildElement {

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_LINK = "link";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_DEFAULT = "default";
	
	
	private String name = null;
	private String value = null;
	private String defaultValue = null;
	private boolean link = false;
	private String linkParam = null;
	
	
	@Processing
	public void processLink(@Attribute(ATTRIBUTE_NAME) String name, @Attribute(ATTRIBUTE_LINK) String linkParam) throws UnsupportedScriptException {
		this.name = name;
		link = true;
		this.linkParam = linkParam;
	}
	
	public void process(@Attribute(ATTRIBUTE_NAME) String name) throws UnsupportedScriptException {
		this.name = name;
		this.value = this.getText();
	}
	
	public void process(@Attribute(ATTRIBUTE_NAME) String name, @Primal@Attribute(ATTRIBUTE_VALUE) String value) throws UnsupportedScriptException {
		this.name = name;
		this.value = value;
	}
	
	public void process(@Attribute(ATTRIBUTE_NAME) String name, @Primal@Attribute(ATTRIBUTE_VALUE) String value, @Primal@Attribute(ATTRIBUTE_DEFAULT) String defaultValue) throws UnsupportedScriptException {
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		if(isLink()) return null;
		return value;
	}
	
	public static Object getParsedValue(Param param) throws UnsupportedScriptException {
		try {
			ParamBean bean = param.getParamBean();
			return getParsedValue(bean, param.getEnvironment());
		} catch(UnsupportedScriptException e) {
			e.setElement(param);
			throw e;
		}
	}
	public static Object getParsedValue(ParamBean bean, Environment env) throws UnsupportedScriptException {
		if(!bean.isLink()) {
			String value = bean.getValue();
			if(value == null || value.equals("")) {
				if(bean.getDefaultValue() != null) value = bean.getDefaultValue();
			}
			
			try {
				return env.parse(value);
			} catch(UnsupportedExpressionException e) {
				throw new UnsupportedScriptException(null, e);
			}
		} else {
			return null;
		}
	}
	
	public boolean isLink() {
		return link;
	}
	
	public String getLinkName() {
		return this.linkParam;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * @see kenh.xscript.beans.ParamBean
	 * @return
	 */
	public ParamBean getParamBean() {
		ParamBean p = new ParamBean();
		p.setName(this.getName());
		p.setValue(this.getValue());
		p.setLink(this.isLink());
		p.setLinkName(this.getLinkName());
		p.setDefaultValue(getDefaultValue());
		
		return p;
	}
}
