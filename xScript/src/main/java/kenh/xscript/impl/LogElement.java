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

import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Text;
import org.apache.commons.lang3.StringUtils;

/**
 * The element for logger.
 * 
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public abstract class LogElement extends NoChildElement {

    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_TRIM = "trim";
    private static final String ATTRIBUTE_EXCEPTION = "exception";

    public void process() throws UnsupportedScriptException {
        Object text = this.getParsedText();
        log(text, null);
    }

    public void process(@Attribute(ATTRIBUTE_EXCEPTION) Throwable e) throws UnsupportedScriptException {
        Object text = this.getParsedText();
        log(text, e);
    }



    public void process(@Attribute(ATTRIBUTE_VALUE) String value) throws UnsupportedScriptException {
        log(value, null);
    }

    public void process(@Attribute(ATTRIBUTE_VALUE) String value, @Attribute(ATTRIBUTE_EXCEPTION) Throwable e) throws UnsupportedScriptException {
        log(value, e);
    }


    public void process(@Attribute(ATTRIBUTE_VALUE) String value, @Attribute(ATTRIBUTE_TRIM) boolean trim ) throws UnsupportedScriptException {
        log(StringUtils.trimToEmpty(value), null);
    }
    public void process(@Attribute(ATTRIBUTE_VALUE) String value, @Attribute(ATTRIBUTE_TRIM) boolean trim, @Attribute(ATTRIBUTE_EXCEPTION) Throwable e) throws UnsupportedScriptException {
        log(StringUtils.trimToEmpty(value), e);
    }


    public void process(@Attribute(ATTRIBUTE_TRIM) boolean trim) throws UnsupportedScriptException {
        Object text = this.getParsedText();
        if(text instanceof String) {
            log(StringUtils.trimToEmpty((String)text), null);
        }
        log(text, null);
    }
    public void process(@Attribute(ATTRIBUTE_TRIM) boolean trim, @Attribute(ATTRIBUTE_EXCEPTION) Throwable e) throws UnsupportedScriptException {
        Object text = this.getParsedText();
        if(text instanceof String) {
            log(StringUtils.trimToEmpty((String)text), null);
        }
        log(text, e);
    }

    protected abstract void log(Object express, Throwable e);

}
