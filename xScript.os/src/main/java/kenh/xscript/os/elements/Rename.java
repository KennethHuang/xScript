package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Rename the file.
 * @author Kenneth
 *
 */
public class Rename extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_DEST = "dest";
	private static final String ATTRIBUTE_PARENT = "parent";
	private static final String ATTRIBUTE_CHILD = "child";
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DEST) String dest) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		if(!path.exists()) throw new UnsupportedScriptException(this, "Path doesn't exists.");
		
		if(StringUtils.isBlank(dest)) throw new UnsupportedScriptException(this, "The destination is empty.");
		if(StringUtils.contains(dest, java.io.File.pathSeparatorChar)) throw new UnsupportedScriptException(this, "The destination can't contain '" + java.io.File.pathSeparatorChar + "'");
		
		
		$File$ f = new $File$(path);
		java.io.File file = f.getIOFile();
		
		java.io.File destFile = new java.io.File(file.getParentFile(), dest);
		if(destFile.exists()) throw new UnsupportedScriptException(this, "The destination already exists.");
		
		file.renameTo(destFile);
		
		String destPath = destFile.getPath();
		try {
			destPath = destFile.getCanonicalPath();
			
		} catch(Exception e) {}
		
		logger.trace(this.getClass().getName().toUpperCase() + " " + path.getPath() + "->" + destPath);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DEST) String dest) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		if(StringUtils.isBlank(dest)) throw new UnsupportedScriptException(this, "New name is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			kenh.xscript.os.beans.File f_ = new kenh.xscript.os.beans.File(f.getCanonicalPath());
			process(f_, dest);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
		
	}
	
	public void process(@Attribute(ATTRIBUTE_PARENT) kenh.xscript.os.beans.File parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_DEST) String dest) throws UnsupportedScriptException {
		if(parent == null) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		try {
			kenh.xscript.os.beans.File f = new kenh.xscript.os.beans.File(parent, child);
			process(f, dest);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PARENT)String parent, @Attribute(ATTRIBUTE_CHILD) String child, @Attribute(ATTRIBUTE_DEST) String dest) throws UnsupportedScriptException {
		if(StringUtils.isBlank(parent)) throw new UnsupportedScriptException(this, "Parent is empty.");
		if(StringUtils.isBlank(child)) throw new UnsupportedScriptException(this, "Child is empty.");
		
		java.io.File f = Utils.getFile(this, parent);
		
		try {
			kenh.xscript.os.beans.File f_ = new kenh.xscript.os.beans.File(f.getCanonicalPath(), child);
			process(f_, dest);
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
