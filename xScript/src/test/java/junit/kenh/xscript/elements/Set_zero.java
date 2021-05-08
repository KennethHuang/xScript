package junit.kenh.xscript.elements;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

import java.util.*;

@IgnoreSuperClass
public class Set_zero extends kenh.xscript.elements.Set {

	private static final String ATTRIBUTE_VARIABLE = "var";
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		saveVariable(var, "0", "0");
	}
	
}
