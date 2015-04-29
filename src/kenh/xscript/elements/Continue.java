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
 * Like continue statement in Java, skip the element after.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Continue extends NoChildElement {

	public int process() {
		return CONTINUE;
	}
	
}
