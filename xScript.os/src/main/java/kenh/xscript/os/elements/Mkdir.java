package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Create a dir.
 * @author Kenneth
 *
 */
public class Mkdir extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_PARENT = "parent";
	private static final String ATTRIBUTE_CHILD = "child";
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		
		new $File$(path).getIOFile().mkdirs();

		getLogger().trace("[XSCRIPT] " + this.getClass().getName().toUpperCase() + " " + path.getPath());
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
	
	
	public void process(@Attribute(ATTRIBUTE_PARENT) String parent, @Attribute(ATTRIBUTE_CHILD) String child) throws UnsupportedScriptException {
		if(StringUtils.isBlank(parent)) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		java.io.File f = Utils.getFile(this, parent);
		
		try {
			kenh.xscript.os.beans.File f_ = new kenh.xscript.os.beans.File(f.getCanonicalPath(), child);
			process(f_);
			
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PARENT) kenh.xscript.os.beans.File parent, @Attribute(ATTRIBUTE_CHILD) String child) throws UnsupportedScriptException {
		if(parent == null) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		try {
			kenh.xscript.os.beans.File f = new kenh.xscript.os.beans.File(parent, child);
			process(f);

		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	class $File$ extends kenh.xscript.os.beans.File {
		
		$File$(kenh.xscript.os.beans.File file) {
			super(file);
		}
		
		protected java.io.File getIOFile() {
			return super.getIOFile();
		}
	}

}
