package kenh.xscript.io.functions;

import kenh.expl.impl.BaseFunction;
import kenh.xscript.io.ExcelDataLoader;

import java.io.File;
import java.util.Vector;

public class GetSheetDataByName extends BaseFunction {
	
	public Vector<String[]> process(String file, String sheetName) throws Exception {
		File f = new File(file);
		return process(f, sheetName);
	}

	public Vector<String[]> process(File file, String sheetName) throws Exception {
		if(! file.exists()) return new Vector<String[]>();
		try {
			Vector<String[]> datas = new ExcelDataLoader().process(file, sheetName).getDatas();
			return datas;

		} catch(Exception e) {
			throw e;
		}

	}

}
