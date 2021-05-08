package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import org.apache.commons.lang3.StringUtils;

/**
 *  List the files or directories.
 * @author Kenneth
 *
 */
public class Dir extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_VARIABLE = "var";
	private static final String ATTRIBUTE_DIR_ONLY = "dir-only";
	private static final String ATTRIBUTE_FILE_ONLY = "file-only";
	private static final String ATTRIBUTE_FILE_EXT = "type";
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		
		String[] names = path.list();
		this.saveVariable(var, names, null);
		
		logger.trace(this.getClass().getName().toUpperCase() + " " + path.getPath());
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()), var);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DIR_ONLY) boolean dirOnly, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		
		if(dirOnly) {
			String[] names = path.list(true);
			this.saveVariable(var, names, null);
			
			logger.trace(this.getClass().getName().toUpperCase() + " " + path.getPath());
			
		} else {
			process(path, var);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DIR_ONLY) boolean dirOnly, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()), dirOnly, var);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_FILE_ONLY) boolean fileOnly, @Attribute(ATTRIBUTE_FILE_EXT) String ext, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		
		if(fileOnly) {
			String[] names = path.listFiles(ext);
			this.saveVariable(var, names, null);
			
			logger.trace(this.getClass().getName().toUpperCase() + " " + path.getPath());
			
		} else {
			process(path, var);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_FILE_ONLY) boolean fileOnly, @Attribute(ATTRIBUTE_FILE_EXT) String ext, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()), fileOnly, ext, var);
		} catch(java.io.IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	
	@Processing
	public void processFile(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_FILE_ONLY) boolean fileOnly, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path, fileOnly, null, var);
	}
	
	@Processing
	public void processFile(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_FILE_ONLY) boolean fileOnly, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path, fileOnly, null, var);
	}
	
	@Processing
	public void processFileExt(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_FILE_EXT) String ext, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path, true, ext, var);
	}
	
	@Processing
	public void processFileExt(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_FILE_EXT) String ext, @Attribute(ATTRIBUTE_VARIABLE) String var) throws UnsupportedScriptException {
		process(path, true, ext, var);
	}
	
}
