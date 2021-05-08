package kenh.xscript.os;

import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

public class FileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
	
	private Vector<String> vec = new Vector();
	private String desc = "";
	private boolean folder = true;
	
	public FileFilter(String exts, boolean folder) {
		this(StringUtils.split(exts, ","), folder);
	}
	
	public FileFilter(String[] exts, boolean folder) {
		this.folder = folder;
		if(exts == null || exts.length <= 0) return;
		for(String ext: exts) {
			ext = StringUtils.trimToEmpty(ext).toLowerCase();
			if(StringUtils.isNotBlank(ext)) {
				vec.add(ext);
				desc = desc + "*." + ext + ", ";
			}
		}
	}
	
	@Override
	public boolean accept(java.io.File f) {
		if (f.isDirectory()) {
			return folder;
		} else if (f.isFile()) {
			if(vec.size() <=0) return true;
			else {
				String ext = StringUtils.trimToEmpty(StringUtils.substringAfterLast(f.getName(), ".")).toLowerCase();
				return vec.contains(ext);
			}
		}
		return false;
	}
	
	@Override
	public String getDescription() {
		if(vec.size() <=0) return "*.*";
		return StringUtils.substringBeforeLast(desc, ",");
	}

}