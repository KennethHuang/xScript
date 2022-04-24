package kenh.xscript.os.elements;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.os.Utils;
import kenh.xscript.*;

import java.io.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Copy the file.
 * @author Kenneth
 *
 */
public class Copy extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_DEST = "dest";
	private static final String ATTRIBUTE_CUT = "cut";
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DEST) kenh.xscript.os.beans.File dest, @Attribute(ATTRIBUTE_CUT) boolean cut ) throws UnsupportedScriptException {
		if(path == null) throw new UnsupportedScriptException(this, "Path is empty.");
		if(dest == null) throw new UnsupportedScriptException(this, "The destination is empty.");
		
		$File$ f1 = new $File$(path);
		$File$ f2 = new $File$(dest);
		
		String folderName = f1.getName();
		
		java.io.File file1 = f1.getIOFile();
		java.io.File file2 = f2.getIOFile();
		
		if(file1.isDirectory()) {
			
			if(file2.isFile()) {
				throw new UnsupportedScriptException(this, "The destination is required to be a folder.");
			}
			
			file2 = new java.io.File(file2, folderName);
			file2.mkdirs();
			
			try {
				copyFolder(file1, file2, cut);
			} catch(IOException e) {
				throw new UnsupportedScriptException(this, e);
			}
			
		} else if(file1.isFile()) {
			if(file2.isDirectory()) {
				file2 = new java.io.File(file2, file1.getName());
			}
			
			if(!file2.getParentFile().exists()) file2.getParentFile().mkdirs();
			
			try {
				copyFile(file1, file2, cut);
			} catch(IOException e) {
				throw new UnsupportedScriptException(this, e);
			}
		}
		
		this.getLogger().trace("[XSCRIPT] " + this.getClass().getName().toUpperCase() + " " + path.getPath() + "->" + dest.getPath());
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DEST) kenh.xscript.os.beans.File dest, @Attribute(ATTRIBUTE_CUT) boolean cut ) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()), dest, cut);
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DEST) String dest, @Attribute(ATTRIBUTE_CUT) boolean cut ) throws UnsupportedScriptException {
		if(StringUtils.isBlank(dest)) throw new UnsupportedScriptException(this, "The destination is empty.");		
		
		java.io.File f = Utils.getFile(this, dest);
		
		try {
			process(path, new kenh.xscript.os.beans.File(f.getCanonicalPath()), cut);
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DEST) String dest, @Attribute(ATTRIBUTE_CUT) boolean cut ) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) throw new UnsupportedScriptException(this, "Path is empty.");
		if(StringUtils.isBlank(dest)) throw new UnsupportedScriptException(this, "The destination is empty.");		
		
		java.io.File f = Utils.getFile(this, path);
		
		try {
			process(new kenh.xscript.os.beans.File(f.getCanonicalPath()), dest, cut);
		} catch(IOException e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DEST) kenh.xscript.os.beans.File dest ) throws UnsupportedScriptException {
		process(path, dest, false);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DEST) kenh.xscript.os.beans.File dest) throws UnsupportedScriptException {
		process(path, dest, false);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_DEST) String dest ) throws UnsupportedScriptException {
		process(path, dest, false);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_DEST) String dest ) throws UnsupportedScriptException {
		process(path, dest, false);
	}
	
	
	
	
	private static byte[] buffer = new byte[1024]; 
	
	private static void copyFile( java.io.File source, java.io.File dest, boolean cut) throws IOException {
		if (source.exists()) {
			
			InputStream in = null;
			FileOutputStream out = null;
			int no = 0; 
			
			try {
				in = new FileInputStream(source); 
				out = new FileOutputStream(dest); 
			
				while((no = in.read(buffer)) != -1){ 
					out.write(buffer,0,no); 
				}
			} catch(IOException e) {
				throw e;
				
			} finally {
				if(in != null) {
					try { in.close(); } catch(Exception e_) {}
					in = null;
				}
				if(out != null) {
					try { out.close(); } catch(Exception e_) {}
					out = null;
				}
			}
			
			if(cut) source.delete();
		} 
	}
	
	private static void copyFolder(java.io.File src, java.io.File dest, boolean cut) throws IOException {  
	    if (src.isDirectory()) {  
	        if (!dest.exists()) {  
	            dest.mkdir();  
	        }  
	        String files[] = src.list();  
	        for (String file : files) {  
	        	java.io.File srcFile = new java.io.File(src, file);  
	        	java.io.File destFile = new java.io.File(dest, file);  
	            copyFolder(srcFile, destFile, cut);  
	        }  
	        
	        if(cut) src.delete();
	        
	    } else {  
	    	copyFile(src, dest, cut);
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
