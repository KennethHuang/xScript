package kenh.xscript.io.functions;

import kenh.expl.impl.BaseFunction;
import kenh.xscript.io.ExcelDataLoader;

import java.io.File;
import java.util.Vector;

public class GetSheetDataByIndex extends BaseFunction {

	/**
	 *
	 * @param file
	 * @param index   start from 0
	 * @return
	 * @throws Exception
	 */
	public Vector<String[]> process(String file, int index) throws Exception {
		File f = new File(file);
		return process(f, index);
	}

	/**
	 *
	 * @param file
	 * @param index   start from 0
	 * @return
	 * @throws Exception
	 */
	public Vector<String[]> process(File file, int index) throws Exception {
		if(! file.exists()) return new Vector<String[]>();
		try {
			Vector<String[]> datas = new ExcelDataLoader().process(file, index).getDatas();
			return datas;

		} catch(Exception e) {
			throw e;
		}

	}

}
