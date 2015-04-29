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

package kenh.xscript.beans;

/**
 * The bean of {@code Param}.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class ParamBean {
	
	private String name;
	private String value;
	private boolean link;
	private String linkName;
	private String defaultValue;
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public boolean isLink() {
		return link;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setLink(boolean link) {
		this.link = link;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
