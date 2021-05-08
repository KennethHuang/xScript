package kenh.xscript.os.functions;

import kenh.expl.impl.BaseFunction;
import kenh.xscript.os.Utils;

public class GetFile extends BaseFunction {
	
	public kenh.xscript.os.beans.File process(String s1) throws java.io.IOException {
		java.io.File f = Utils.getFile(this, s1);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(String s1, String s2) throws java.io.IOException {
		java.io.File f = Utils.getFile(this, s1);
		if(f == null) return null;
		
		f = Utils.getFile(f, s2);
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}

	public kenh.xscript.os.beans.File process(String s1, String s2, String s3) throws java.io.IOException {
		java.io.File f = Utils.getFile(this, s1);
		if(f == null) return null;
		
		f = Utils.getFile(f, s2, s3);
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}

	public kenh.xscript.os.beans.File process(String s1, String s2, String s3, String s4) throws java.io.IOException {
		java.io.File f = Utils.getFile(this, s1);
		if(f == null) return null;
		
		f = Utils.getFile(f, s2, s3, s4);
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(String s1, String s2, String s3, String s4, String s5) throws java.io.IOException {
		java.io.File f = Utils.getFile(this, s1);
		if(f == null) return null;
		
		f = Utils.getFile(f, s2, s3, s4, s5);
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	
	
	
	public kenh.xscript.os.beans.File process(kenh.xscript.os.beans.File f1, String s1) throws java.io.IOException {
		java.io.File f = Utils.getFile(f1.getPath(), s1);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(kenh.xscript.os.beans.File f1, String s1, String s2) throws java.io.IOException {
		java.io.File f = Utils.getFile(f1.getPath(), s1, s2);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(kenh.xscript.os.beans.File f1, String s1, String s2, String s3) throws java.io.IOException {
		java.io.File f = Utils.getFile(f1.getPath(), s1, s2, s3);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(kenh.xscript.os.beans.File f1, String s1, String s2, String s3, String s4) throws java.io.IOException {
		java.io.File f = Utils.getFile(f1.getPath(), s1, s2, s3, s4);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
	
	public kenh.xscript.os.beans.File process(kenh.xscript.os.beans.File f1, String s1, String s2, String s3, String s4, String s5) throws java.io.IOException {
		java.io.File f = Utils.getFile(f1.getPath(), s1, s2, s3, s4, s5);
		if(f == null) return null;
		
		return new kenh.xscript.os.beans.File(f.getCanonicalPath()); 
	}
}
