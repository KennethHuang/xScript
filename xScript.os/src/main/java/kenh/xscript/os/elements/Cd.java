package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Select a home directory.
 * @author Kenneth
 *
 */
public class Cd extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_PARENT = "parent";
	private static final String ATTRIBUTE_CHILD = "child";
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		
		if(!path.exists()) throw new UnsupportedScriptException(this, "Path doesn't exists. [" + path.getPath() + "]");
		
		if(!path.isDirectory()) throw new UnsupportedScriptException(this, "Path is not a directory. [" + path.getPath() + "]");
		
		String home = path.getPath();
		this.getEnvironment().setPublicVariable(Constant.VARIABLE_HOME, home, false);
		
		this.getLogger().trace("[XSCRIPT] " + this.getClass().getName().toUpperCase() + " " + path.getPath());
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()));
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	@Processing
	public void processVar(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
			Object obj = this.getEnvironment().getVariable(Constant.VARIABLE_HOME);
			this.saveVariable(var, obj, null);
		} else {
			this.saveVariable(var, "", null);
		}
	}
	
	@Processing
	public void processVar(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path);
		processVar(var);
	}
	
	@Processing
	public void processVar(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path);
		processVar(var);
	}
	
	@Processing
	public void processVar(@Attribute(ATTRIBUTE_PARENT) String parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(parent)) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		java.io.File f = Utils.getFile(this, parent);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath(), child));
			processVar(var);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	@Processing
	public void processVar(@Attribute(ATTRIBUTE_PARENT) kenh.xscript.os.beans.File parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(parent == null) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		try {
			process(new kenh.xscript.os.beans.File(parent, child));
			processVar(var);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
}
