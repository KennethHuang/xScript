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

package kenh.xscript.elements.log;

import kenh.xscript.annotation.Text;
import kenh.xscript.impl.LogElement;


/**
 * Logs a message object with the INFO level.
 * 
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Fatal extends LogElement {

	protected void log(Object express, Throwable e) {
		if(express == null && e == null) return;
		else this.getLogger().fatal(express, e);
	}
	
}
