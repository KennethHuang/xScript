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

package kenh.xscript.impl;

import kenh.xscript.*;
import kenh.xscript.annotation.Exclude;
import kenh.xscript.elements.Else;
import kenh.xscript.elements.Include;
import kenh.xscript.elements.Method;
import kenh.xscript.elements.Script;
import kenh.xscript.elements.Then;

/**
 * Normal element, it can't have {@code Script}, {@code Method}, {@code Include} as child element
 * and without text.
 * @author Kenneth
 *
 */
@Exclude({Script.class, Method.class, Include.class, Then.class, Else.class})
public abstract class NormalElement extends BaseElement {

}
