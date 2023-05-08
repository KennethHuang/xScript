package kenh.xscript.io.functions;

import kenh.expl.impl.BaseFunction;
import kenh.xscript.io.ExcelDataLoader;

import java.io.File;

public class GetSheetsCount extends BaseFunction {

	public int process(String file) throws Exception {
		File f = new File(file);
		return process(f);
	}

	public int process(File file) throws Exception {
		if(! file.exists()) return 0;
		try {
			String[] names = new ExcelDataLoader().process(file, -1).getSheetNames();
			return names.length;
		} catch(Exception e) {
			throw e;
		}
	}

}
