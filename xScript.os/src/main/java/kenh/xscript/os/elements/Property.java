package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.elements.Set;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Set/Get system property.
 * @author Kenneth
 *
 */
@IgnoreSuperClass
public class Property extends Set {
	
	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_VALUE = "value";
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		String var_ = StringUtils.trimToEmpty(var);
		String value = System.getProperty("XSCRIPT." + var_, "");
		this.saveVariable(var, value, null);
	}

	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_VALUE) String value) throws UnsupportedScriptException {
		String var_ = StringUtils.trimToEmpty(var);
		System.setProperty("XSCRIPT." + var_, value);
		this.saveVariable(var, value, null);
	}
}
