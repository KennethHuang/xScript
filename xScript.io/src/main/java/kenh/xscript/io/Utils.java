package kenh.xscript.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.Map;

public class Utils {
	
	/**
	 * Check all attribute names that are all in the list
	 * @param atts   the map
	 * @param allowZero   allow the map with empty element
	 * @param names  all allowed attribute names
	 * @return
	 */
	public static boolean checkAttributes(Map<String, Object> atts, boolean allowZero, String ... names) {
		
		if(atts == null || atts.size() == 0) {
			if(allowZero) return true;
			else return false;
		}
		
		int i=0;
		for(String name: names) {
			if(atts.containsKey(name)) i++;
		}
		
		if(atts.size() == i) return true;
		
		return false;
	}
	
	/**
	 * Check the value that are all in the list
	 * @param value
	 * @param values
	 * @return
	 */
	public static boolean match(String value, String ... values) {
		
		for(String s: values) {
			if(StringUtils.equals(value, s)) return true;
		}
		
		return false;
	}

	/**
	 * Get certain cell in a sheet
	 * @param workbook
	 * @param sheetName
	 * @param rowId
	 * @param colId
	 * @return
	 */
	public static Cell getCell(Workbook workbook, String sheetName, int rowId, int colId) {
		return getCell(workbook, sheetName, rowId, colId, true);
	}

	/**
	 * Get certain cell in a sheet
	 * @param workbook
	 * @param sheetName
	 * @param rowId
	 * @param colId
	 * @param createNewCell
	 * @return
	 */
	public static Cell getCell(Workbook workbook, String sheetName, int rowId, int colId, boolean createNewCell) {
		org.apache.poi.ss.usermodel.Sheet sheet = null;
		if(workbook.getSheet(sheetName) == null) {
			if(StringUtils.isBlank(sheetName)) {
				int i = 1;
				while(workbook.getSheet("Sheet" + i) != null) { i++; }
				sheet = workbook.createSheet("Sheet" + i);
			} else {
				sheet = workbook.createSheet(sheetName);
			}
		} else {
			sheet = workbook.getSheet(sheetName);
		}

		Row row = sheet.getRow(rowId);
		if(row == null) {
			if(!createNewCell) return null;
			row = sheet.createRow(rowId);
		}
		Cell cell = row.getCell(colId);
		if(cell == null) {
			if(!createNewCell) return null;
			cell = row.createCell(colId);
		}

		return cell;
	}

	/**
	 * Get certain cell in a sheet
	 * @param workbook
	 * @param sheetId
	 * @param rowId
	 * @param colId
	 * @return
	 */
	public static Cell getCell(Workbook workbook, int sheetId, int rowId, int colId) {
		return getCell(workbook, sheetId, rowId, colId, true);
	}

	/**
	 * Get certain cell in a sheet
	 * @param workbook
	 * @param sheetId
	 * @param rowId
	 * @param colId
	 * @param createNewCell
	 * @return
	 */
	public static Cell getCell(Workbook workbook, int sheetId, int rowId, int colId, boolean createNewCell) {
		int numberOfSheets = workbook.getNumberOfSheets();
		Sheet sheet = null;
		if(sheetId >= numberOfSheets) {
			int i = sheetId + 1;
			while(workbook.getSheet("Sheet" + i) != null) {i++;}
			sheet = workbook.createSheet("Sheet" + i);
		} else {
			sheet = workbook.getSheetAt(sheetId);
		}

		Row row = sheet.getRow(rowId);
		if(row == null) {
			if(!createNewCell) return null;
			row = sheet.createRow(rowId);
		}
		Cell cell = row.getCell(colId);
		if(cell == null) {
			if(!createNewCell) return null;
			cell = row.createCell(colId);
		}

		return cell;
	}

	/**
	 * get the value of cell
	 * @param cell
	 * @return
	 */
	private static DataFormatter dataFormatter = new DataFormatter();
	public static Object getCellValue(Cell cell, FormulaEvaluator evaluator) {
		return getCellValue(cell, null, evaluator);
	}
	public static Object getCellValue(Cell cell, Object nullValue, FormulaEvaluator evaluator) {
		String value = System.getProperty("XSCRIPT.NUMERIC");
		if(StringUtils.equalsAnyIgnoreCase(value, "Y", "yes")) {
			return getCellValue(cell, null, true, evaluator);
		} else {
			return getCellValue(cell, null, false, evaluator);
		}
	}
	public static Object getCellValue(Cell cell, Object nullValue, boolean numeric, FormulaEvaluator evaluator) {
		if(cell == null) return nullValue;
		if(cell.getCellType() == CellType.BOOLEAN) {
			if(cell.getBooleanCellValue()) return "true";
			else return "false";
		}
		if(cell.getCellType() == CellType.NUMERIC) {
			if(DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else if(numeric) {
				return cell.getNumericCellValue();
			} else {
				return dataFormatter.formatCellValue(cell);
			}
		}
		if (cell.getCellType() == CellType.FORMULA) {
			if(evaluator == null) return cell.getCellFormula();
			switch (evaluator.evaluateFormulaCell(cell)) {
				case BOOLEAN:
					return cell.getBooleanCellValue();
				case NUMERIC:
					if(DateUtil.isCellDateFormatted(cell)) {
						return cell.getDateCellValue();
					} else if(numeric) {
						return cell.getNumericCellValue();
					} else {
						return dataFormatter.formatCellValue(cell);
					}
				case STRING:
					return cell.getStringCellValue();
			}
		}
		return cell.getStringCellValue();
	};

}
