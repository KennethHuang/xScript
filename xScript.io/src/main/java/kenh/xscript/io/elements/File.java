package kenh.xscript.io.elements;

import kenh.xscript.Constant;
import kenh.xscript.Element;
import kenh.xscript.UnsupportedScriptException;
import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Exclude;
import kenh.xscript.annotation.IgnoreSuperClass;
import kenh.xscript.annotation.Processing;
import kenh.xscript.elements.Set;

/**
 * Set a reference to point at a object of Text file
 * @author Kenneth
 *
 */
@Exclude(Element.class)
@IgnoreSuperClass
public class File extends Set {

	private static final String ATTRIBUTE_FILE = "file";
	private static final String ATTRIBUTE_VARIABLE = "var";

	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) String file) throws UnsupportedScriptException {
		java.io.File f = null;
		try {
			f = new java.io.File(file);
			if(!f.isAbsolute() && this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
				Object obj = this.getEnvironment().getVariable(Constant.VARIABLE_HOME);
				if(obj instanceof String) f = new java.io.File((String)obj, file);
			}
			f = f.getCanonicalFile();

		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}

		this.saveVariable(var, f, null);
	}
	
}
