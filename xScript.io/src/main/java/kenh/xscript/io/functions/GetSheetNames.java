package kenh.xscript.io.functions;

import kenh.expl.impl.BaseFunction;
import kenh.xscript.io.ExcelDataLoader;

import java.io.File;
import java.util.Vector;

public class GetSheetNames extends BaseFunction {

	public String[] process(String file) throws Exception {
		File f = new File(file);
		return process(f);
	}

	public String[] process(File file) throws Exception {
		if(! file.exists()) return new String[] {};
		try {
			return new ExcelDataLoader().process(file, -1).getSheetNames();
		} catch(Exception e) {
			throw e;
		}
	}

}
