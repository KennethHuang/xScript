package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import java.io.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Creates a file bean.
 * @author Kenneth
 *
 */
public class File extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_PARENT = "parent";
	private static final String ATTRIBUTE_CHILD = "child";
	
	/**
	 * Creates an empty file in the default temporary-file directory, delete on exists. 
	 * @param var
	 * @throws UnsupportedScriptException
	 */
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		try {
			java.io.File file = java.io.File.createTempFile("xscript", null);
			file.deleteOnExit();
			
			kenh.xscript.os.beans.File f = new kenh.xscript.os.beans.File(new $File$(file));
			this.saveVariable(var, f, null);
			
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			kenh.xscript.os.beans.File f_ = new kenh.xscript.os.beans.File(f.getCanonicalPath());
			this.saveVariable(var, f_, null);
			
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PARENT) String parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(parent)) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		java.io.File f = Utils.getFile(this, parent);
		
		try {
			kenh.xscript.os.beans.File f_ = new kenh.xscript.os.beans.File(f.getCanonicalPath(), child);
			this.saveVariable(var, f_, null);
			
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PARENT) kenh.xscript.os.beans.File parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(parent == null) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		try {
			kenh.xscript.os.beans.File f = new kenh.xscript.os.beans.File(parent, child);
			this.saveVariable(var, f, null);
			
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	class $File$ extends kenh.xscript.os.beans.File {
		$File$(java.io.File file) throws IOException {
			super(file);
		}
	}

}
