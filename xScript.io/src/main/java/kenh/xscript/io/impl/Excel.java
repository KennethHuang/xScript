package kenh.xscript.io.impl;

import java.io.*;
import java.util.*;

import jxl.*;
import jxl.write.*;
import kenh.expl.Callback;
import kenh.xscript.io.Reader;
import kenh.xscript.io.Writer;
import kenh.xscript.io.wrap.*;

import org.apache.commons.lang3.StringUtils;

/**
 * For excel reading and writing.
 * @author Kenneth
 *
 */
public class Excel implements Reader, Writer, Callback, Iterable {
	
	private static final String ATTRIBUTE_SHEET = "sheet";
	private static final String ATTRIBUTE_ROW = "row";	// start from 0
	private static final String ATTRIBUTE_COL = "col";	// start from 0
	
	private WritableWorkbook wwb = null;
	private Workbook wb = null;
	private boolean isReadOnly = false;
	private File file = null;
	
	public Excel(File f, boolean readOnly, boolean append) throws Exception {
		this.file = f;
		isReadOnly = readOnly;
		
		if(isReadOnly) {
			if(file.exists()) wb = Workbook.getWorkbook(file);
			else throw new IOException("File is not exist");
		} else {
			if(file.exists()) {
				if(append) wwb = Workbook.createWorkbook(file, Workbook.getWorkbook(file));
				else wwb = Workbook.createWorkbook(file);
			} else {
				file.getParentFile().mkdirs();
				wwb = Workbook.createWorkbook(file);
			}
		}
	}
	
	public kenh.xscript.io.wrap.Sheet getSheet() {
		return getSheet(0);
	}
	
	public kenh.xscript.io.wrap.Sheet getSheet(int index) {
		jxl.Sheet s = isReadOnly? wb.getSheet(index) : wwb.getSheet(index);
		if(s == null) return null;
		else return new kenh.xscript.io.wrap.Sheet(s);
	}
	
	public kenh.xscript.io.wrap.Sheet getSheet(String sheet) {
		jxl.Sheet s = isReadOnly? wb.getSheet(sheet) : wwb.getSheet(sheet);
		if(s == null) return null;
		else return new kenh.xscript.io.wrap.Sheet(s);
	}
	
	public kenh.xscript.io.wrap.Sheet[] getSheets() {
		jxl.Sheet[] ss = isReadOnly? wb.getSheets() : wwb.getSheets();
		
		Vector<kenh.xscript.io.wrap.Sheet> v = new Vector();
		if(ss != null) {
			for(jxl.Sheet s: ss) {
				v.add(new kenh.xscript.io.wrap.Sheet(s));
			}
		}
		
		return v.toArray(new kenh.xscript.io.wrap.Sheet[] {});
	}
	
	@Override
	public void write(Object object, Map<String, Object> attributes) throws Exception {
		
		if(isReadOnly) throw new UnsupportedOperationException("Read only.");
		
		String sheetName = null;
		int row = -1;
		int col = -1;
		
		Set<String> keys = attributes.keySet();
		for(String key: keys) {
			if(StringUtils.equals(key, ATTRIBUTE_SHEET)) {
				sheetName = attributes.get(ATTRIBUTE_SHEET).toString();
				
			} else if(StringUtils.equals(key, ATTRIBUTE_ROW)) {
				Object obj = attributes.get(ATTRIBUTE_ROW);
				if(obj instanceof Integer) {
					row = (Integer)obj;
				} else if(obj instanceof String) {
					String str = (String)obj;
					if(StringUtils.isNumeric(str)) {
						row = Integer.parseInt(str);
					}
				}
				if(row < 0) throw new UnsupportedOperationException("Wrong value for attribute. [" + ATTRIBUTE_ROW + "]");
				
			} else if(StringUtils.equals(key, ATTRIBUTE_COL)) {
				Object obj = attributes.get(ATTRIBUTE_COL);
				if(obj instanceof Integer) {
					col = (Integer)obj;
				} else if(obj instanceof String) {
					String str = (String)obj;
					if(StringUtils.isNumeric(str)) {
						col = Integer.parseInt(str);
					}
				}
				if(col < 0) throw new UnsupportedOperationException("Wrong value for attribute. [" + ATTRIBUTE_COL + "]");
				
			} else {
				throw new UnsupportedOperationException("Unknown attribute. [" + key + "]");
			}
		}
		
		if(sheetName == null || row < 0 || col < 0) {
			throw new UnsupportedOperationException("Missing one of those attributes required. [" + ATTRIBUTE_SHEET + ", " + ATTRIBUTE_ROW + ", " + ATTRIBUTE_COL + "]");
		}
		
		WritableSheet sheet = wwb.getSheet(sheetName);
		if(sheet == null) sheet = wwb.createSheet(sheetName, 0);
		
		if(object instanceof java.util.Date) {
			DateTime datetime = new DateTime(col, row, (java.util.Date)object);
			sheet.addCell(datetime);
		} else {
			Label label = new Label(col, row, object.toString());
			sheet.addCell(label);
		}
	}

	@Override
	public Object read(Map<String, Object> attributes) throws Exception {
		
		String sheetName = null;
		int row = -1;
		int col = -1;
		
		Set<String> keys = attributes.keySet();
		for(String key: keys) {
			if(StringUtils.equals(key, ATTRIBUTE_SHEET)) {
				sheetName = attributes.get(ATTRIBUTE_SHEET).toString();
				
			} else if(StringUtils.equals(key, ATTRIBUTE_ROW)) {
				Object obj = attributes.get(ATTRIBUTE_ROW);
				if(obj instanceof Integer) {
					row = (Integer)obj;
				} else if(obj instanceof String) {
					String str = (String)obj;
					if(StringUtils.isNumeric(str)) {
						row = Integer.parseInt(str);
					}
				}
				if(row < 0) throw new UnsupportedOperationException("Wrong value for attribute. [" + ATTRIBUTE_ROW + "]");
				
			} else if(StringUtils.equals(key, ATTRIBUTE_COL)) {
				Object obj = attributes.get(ATTRIBUTE_COL);
				if(obj instanceof Integer) {
					col = (Integer)obj;
				} else if(obj instanceof String) {
					String str = (String)obj;
					if(StringUtils.isNumeric(str)) {
						col = Integer.parseInt(str);
					}
				}
				if(col < 0) throw new UnsupportedOperationException("Wrong value for attribute. [" + ATTRIBUTE_COL + "]");
				
			} else {
				throw new UnsupportedOperationException("Unknown attribute. [" + key + "]");
			}
		}
		
		if(sheetName == null) {
			throw new UnsupportedOperationException("Missing attribute. [" + ATTRIBUTE_SHEET + "]");
		}
		
		if(row < 0 && col < 0) {
			throw new UnsupportedOperationException("One of those attribute is required. [" + ATTRIBUTE_ROW + ", " + ATTRIBUTE_COL + "]");
		}
		
		jxl.Sheet sheet = isReadOnly? wb.getSheet(sheetName) : wwb.getSheet(sheetName);
		if(sheet == null) {
			throw new UnsupportedOperationException("The sheet do not exist. [" + sheetName + "]");
		}
		
		int maxRow = sheet.getRows();
		int maxCol = sheet.getColumns();
		
		if(row >= 0 && col >= 0) {
			if(row >= maxRow || col >= maxCol) {
				return null;
			}
			return sheet.getCell(col, row).getContents();
			
		} else if(row >= 0 && col < 0) {
			if(row > maxRow) {
				return null;
			}
			
			Vector<String> ss = new Vector();
			for(int i=0; i<= maxCol; i++) {
				ss.add(sheet.getCell(i, row).getContents());
			}
			
			return ss.toArray(new String[] {});
			
		} else if(row < 0 && col >= 0) {
			if(col > maxCol) {
				return null;
			}
			
			Vector<String> ss = new Vector();
			for(int i=0; i<= maxRow; i++) {
				ss.add(sheet.getCell(col, i).getContents());
			}
			
			return ss.toArray(new String[] {});
			
		} else {
			return sheet.getCell(col, row).getContents();			
		}
	}
	
	
	@Override
	public void callback() {
		
		if(!isReadOnly) {
			try {
				if(wwb.getSheets().length <= 0) {
					wwb.createSheet("Sheet1", 0);
				}
				wwb.write();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(wb != null) wb.close();
		} catch(Exception e) {
			wb = null;
		}
		
		try {
			if(wwb != null) wwb.close();
		} catch(Exception e) {
			wwb = null;
		}
		
	}

	@Override
	public Iterator iterator() {
		return Arrays.asList(wwb.getSheetNames()).iterator();
	}

}