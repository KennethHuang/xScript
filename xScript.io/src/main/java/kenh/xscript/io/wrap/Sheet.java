package kenh.xscript.io.wrap;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/*
import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;
*/


/**
 * The wrapper class for org.apache.poi.ss.usermodel.Sheet
 * @author Kennethh
 *
 */
public class Sheet implements Iterable<String[]> {
	
	private jxl.Sheet sheet = null;
	
	public Sheet(jxl.Sheet sheet) {
		this.sheet = sheet;
	}
	
	public int getRowNum() {
		return sheet.getRows();
	}
	
	public int getColNum() {
		return sheet.getColumns();
	}
	
	public String getName() {
		return sheet.getName();
	}
	
	@Override
	public Iterator<String[]> iterator() {
		return new RIterator();
	}
	
	public Iterable<String[]> cols() {
		return (new Iterable<String[]>() {

			@Override
			public Iterator<String[]> iterator() {
				return new CIterator();
			}
			
		});
	}
	
	class CIterator implements Iterator<String[]> {
		
		private int i = 0;
		
		@Override
		public boolean hasNext() {
			return i < sheet.getColumns();
		}

		@Override
		public String[] next() {
			int no = i++;
			
			Vector<String> v = new Vector();
			for(int j=0; j< sheet.getRows(); j++) {
				jxl.Cell c = sheet.getCell(no, j);
				if(c == null) v.add("");
				else v.add(c.getContents());
			}
			
			return v.toArray(new String[] {});
		}
		
	}

	class RIterator implements Iterator<String[]> {
		
		int i = 0;
		
		@Override
		public boolean hasNext() {
			return i < sheet.getRows();
		}

		@Override
		public String[] next() {
			int no = i++;
			
			jxl.Cell[] cs = sheet.getRow(no);
			if(cs == null) return new String[] {};
			
			Vector<String> v = new Vector();
			for(jxl.Cell c: cs) {
				v.add(c.getContents());
			}
			
			return v.toArray(new String[] {});
		}
	}
	

}
