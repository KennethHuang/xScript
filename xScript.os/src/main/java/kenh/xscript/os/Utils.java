package kenh.xscript.os;

import org.apache.commons.lang3.StringUtils;

public class Utils {
	
	public static final java.io.File getFile(String ... names) {
		
		if(names == null || names.length <= 0) return null;
		
		java.io.File f = new java.io.File(names[0]);
		
		for(int i=1; i< names.length; i++) {
			f = new java.io.File(f, names[i]);
		}
		
		return f;
	}
	
	public static final java.io.File getFile(java.io.File f, String ... names) {
		
		if(f == null) return null;
		if(names == null || names.length <= 0) return f;
		
		for(int i=0; i< names.length; i++) {
			f = new java.io.File(f, names[i]);
		}
		
		return f;
	}
	
	public static final java.io.File getFile(kenh.expl.Function e, String path) {
		return getFile((e==null)? null:e.getEnvironment(), path);
	}
	
	public static final java.io.File getFile(kenh.xscript.Element e, String path) {
		return getFile((e==null)? null:e.getEnvironment(), path);
	}
	
	public static final java.io.File getFile(kenh.expl.Environment env, String path) {
		if(StringUtils.isBlank(path)) return null;
		
		java.io.File f = new java.io.File(path);
		if(env == null) return f;
		
		if(!f.isAbsolute() && env.containsVariable(kenh.xscript.Constant.VARIABLE_HOME)) {
			Object obj = env.getVariable(kenh.xscript.Constant.VARIABLE_HOME);
			if(obj instanceof String) f = new java.io.File((String)obj, path);
		}
		
		return f;
	}
	
}
