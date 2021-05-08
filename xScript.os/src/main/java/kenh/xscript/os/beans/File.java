package kenh.xscript.os.beans;

import java.io.*;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

public class File {
	
	private java.io.File file = null;
	
	public File(String path) throws IOException {
		this(new java.io.File(path));
	}
	
	public File(File parent, String child) throws IOException {
		this(new java.io.File(parent.getIOFile(), child));
	}
	
	public File(String parent, String child) throws IOException {
		this(new java.io.File(parent, child));
	}

	protected File(java.io.File f) throws IOException {
		file = f.getCanonicalFile();
	}
	
	public File(File f) {
		this.file = f.getIOFile();
	}
	
	protected java.io.File getIOFile() {
		return file;
	}
	
	public File getParent() throws IOException {
		if(file.getParentFile() == null) return null;
		return new File(file.getParentFile());
	}
	
	public String getName() {
		return file.getName();
	}
	
	public String getPath() {
		return file.getPath();
	}
	
	public boolean isFile() {
		return file.isFile();
	}
	
	public boolean isDirectory() {
		return file.isDirectory();
	}
	
	public boolean exists() {
		return file.exists();
	}
	
	public String[] list() {
		String[] ls = file.list();
		if(ls == null) return new String[] {};
		return ls;
	}
	
	public String[] list(boolean directoryOnly) {
		java.io.File[] files = file.listFiles();
		Vector<String> result = new Vector();
		
		if(directoryOnly) {
			for(java.io.File f: files) {
				if(f.isDirectory()) result.add(f.getName());
			}
		} else {
			for(java.io.File f: files) {
				if(f.isFile()) result.add(f.getName());
			}
		}
		
		return result.toArray(new String[] {});
	}
	
	/**
	 * Only list files with specified extension
	 * @param ext
	 * @return
	 */
	public String[] listFiles(String exts) {
		if(StringUtils.isBlank(exts)) return list(false);
		
		java.io.File[] files = file.listFiles(new kenh.xscript.os.FileFilter(exts, false));
		Vector<String> result = new Vector();
		
		for(java.io.File f: files) {
			if(f.isFile()) result.add(f.getName());
		}
		
		return result.toArray(new String[] {});
	}
	
}
