package kenh.xscript.io.impl;

import java.io.*;
import java.util.*;

import kenh.expl.Callback;
import kenh.xscript.io.Reader;
import kenh.xscript.io.Utils;
import kenh.xscript.io.Writer;
import kenh.xscript.io.wrap.*;

import kenh.xscript.io.wrap.Sheet;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * For excel reading and writing.
 * @author Kenneth
 *
 */
public class Excel implements Reader, Writer, Callback, Iterable {
	
	private static final String ATTRIBUTE_SHEET = "sheet";
	private static final String ATTRIBUTE_ROW = "row";	// start from 0
	private static final String ATTRIBUTE_COL = "col";	// start from 0
	private static final String ATTRIBUTE_NUMBERIC = "numeric";	// only for reading numeric field

	private Workbook wb = null;

	private boolean isReadOnly = false;
	private File file = null;
	
	public Excel(File f, boolean readOnly, boolean append) throws Exception {
		this.file = f;
		isReadOnly = readOnly;
		if(isReadOnly) append = true;

		if(isReadOnly && !file.exists()) throw new IOException("File is not exist");
		if(!isReadOnly && !file.exists()) file.getParentFile().mkdirs();
		String fileName = f.getName();

		if(append) {
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				if (StringUtils.endsWith(fileName, ".xls")) {
					wb = new HSSFWorkbook(inputStream);
				} else {
					wb = new XSSFWorkbook(inputStream);
				}
			} finally {
				if(inputStream != null) inputStream.close();
			}

		} else {
			if (StringUtils.endsWith(fileName, ".xls")) {
				wb = new HSSFWorkbook();
			} else {
				wb = new XSSFWorkbook();
			}
			if(wb.getNumberOfSheets() <= 0) {
				wb.createSheet("Sheet1");
			}
		}

	}
	
	public kenh.xscript.io.wrap.Sheet getSheet() {
		return getSheet(0);
	}
	
	public kenh.xscript.io.wrap.Sheet getSheet(int index) {
		org.apache.poi.ss.usermodel.Sheet s = wb.getSheetAt(index);
		if(s == null) return null;
		else return new kenh.xscript.io.wrap.Sheet(s);
	}
	
	public kenh.xscript.io.wrap.Sheet getSheet(String sheet) {
		org.apache.poi.ss.usermodel.Sheet s = wb.getSheet(sheet);
		if(s == null) return null;
		else return new kenh.xscript.io.wrap.Sheet(s);
	}
	
	public kenh.xscript.io.wrap.Sheet[] getSheets() {
		Vector<kenh.xscript.io.wrap.Sheet> v = new Vector();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			v.add(new kenh.xscript.io.wrap.Sheet(wb.getSheetAt(i)));
		}
		return v.toArray(new kenh.xscript.io.wrap.Sheet[] {});
	}
	
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

		Cell cell = Utils.getCell(wb, sheetName, row, col);
		if(object instanceof java.util.Date) {
			cell.setCellValue((java.util.Date)object);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat((short) 14);
			cell.setCellStyle(cellStyle);
		} else if(object instanceof java.util.Calendar) {
			cell.setCellValue(((java.util.Calendar)object).getTime());
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat((short) 14);
			cell.setCellStyle(cellStyle);
		} else if(object instanceof java.sql.Date) {
			java.util.Date date = new java.util.Date();
			date.setTime(((java.sql.Date)object).getTime());
			cell.setCellValue(date);
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat((short) 14);
			cell.setCellStyle(cellStyle);
		} else if(object instanceof Boolean || object.getClass() == boolean.class ) {
			cell.setCellValue((Boolean)object);
		} else if(object instanceof Double || object.getClass() == double.class ) {
			cell.setCellValue((Double)object);
		} else if(object instanceof Float || object.getClass() == float.class ) {
			cell.setCellValue((Float)object);
		} else if(object instanceof Integer || object.getClass() == int.class ) {
			cell.setCellValue((Integer)object);
		} else {
			cell.setCellValue(object.toString());
		}
	}

	public Object read(Map<String, Object> attributes) throws Exception {
		
		String sheetName = null;
		int row = -1;
		int col = -1;
		boolean numeric = false;
		
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
				
			} else if(StringUtils.equals(key, ATTRIBUTE_NUMBERIC)) {
				numeric = true;
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

		org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet(sheetName);
		if(sheet == null) {
			throw new UnsupportedOperationException("The sheet do not exist. [" + sheetName + "]");
		}

		if(row >= 0 && col >= 0) {
			Cell cell = Utils.getCell(wb, sheetName, row, col, false);
			return Utils.getCellValue(cell);
		}

		if(row >= 0 && col < 0) {
			Row curRow = sheet.getRow(row);
			if(curRow == null) return null;
			Object[] objects = new Object[curRow.getLastCellNum() + 1];
			for(int i=0; i< objects.length; i++) {
				Cell cell = curRow.getCell(i);
				objects[i] = Utils.getCellValue(cell, "", numeric);
			}
			return objects;
		}

		if(row < 0 && col >= 0) {
			Object[] objects = new Object[sheet.getLastRowNum() + 1];
			for(int i=0; i< objects.length; i++) {
				Row curRow = sheet.getRow(i);
				if(curRow == null) objects[i] = null;
				else if(col > curRow.getLastCellNum()) objects[i] = null;
				else {
					Cell cell = curRow.getCell(col);
					objects[i] = Utils.getCellValue(cell, "", numeric);
				}
			}
			return objects;
		}

		return null;
	}
	
	public void callback() {
		if(!isReadOnly) {
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				wb.write(outputStream);
				outputStream.close();
			} catch(Exception e) {
			} finally {
				if(outputStream != null) try { outputStream.close(); } catch(Exception e_) {}
			}
		}

		try {
			wb.close();
		} catch(Exception e) {
		} finally {
			if(wb != null) try { wb.close(); } catch(Exception e_) {}
		}
	}

	public Iterator iterator() {
		Vector<String> allSheetNames = new Vector();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			allSheetNames.add(wb.getSheetAt(i).getSheetName());
		}
		return allSheetNames.iterator();
	}


}