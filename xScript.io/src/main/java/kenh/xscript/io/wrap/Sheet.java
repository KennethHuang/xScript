package kenh.xscript.io.wrap;

import kenh.xscript.io.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Kennethh
 *
 */
public class Sheet implements Iterable<Object[]> {
	
	private org.apache.poi.ss.usermodel.Sheet sheet = null;
	
	public Sheet(org.apache.poi.ss.usermodel.Sheet sheet) {
		this.sheet = sheet;
	}

	public Object[] getRow(int r) {
		Vector vector = new Vector();

		if(sheet != null) {
			Row row = sheet.getRow(r);
			if(row != null) {
				for(int i=0; i<= row.getLastCellNum(); i++) {
					vector.add(Utils.getCellValue(row.getCell(i), ""));
				}
			}
		}

		return vector.toArray(new Object[0]);
	}

	public Object[] getCol(int c) {
		Vector vector = new Vector();

		if(sheet != null) {
			for(int i=0; i<= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if(row == null) {
					vector.add("");
				} else if(c > row.getLastCellNum()) {
					vector.add("");
				} else {
					vector.add(Utils.getCellValue(row.getCell(c), ""));
				}
			}
		}

		return vector.toArray(new Object[0]);
	}

	public int getRowNum() {
		return sheet.getLastRowNum() + 1;
	}
	
	public int getColNum() {
		int maxColsNo = 0;
		for(int i=0; i<= sheet.getLastRowNum(); i++) {
			if(sheet.getRow(i)!=null) {
				int curColsNo = sheet.getRow(i).getLastCellNum();
				if(curColsNo > maxColsNo) maxColsNo = curColsNo;
			}
		}
		return maxColsNo + 1;
	}
	
	public String getName() {
		return sheet.getSheetName();
	}
	
	public Iterator<Object[]> iterator() {
		return new RIterator();
	}
	
	public Iterable<Object[]> cols() {
		return (new Iterable<Object[]>() {
			public Iterator<Object[]> iterator() {
				return new CIterator();
			}
		});
	}
	
	class CIterator implements Iterator<Object[]> {
		
		private int i = 0;
		private int colNum = Sheet.this.getColNum();
		
		public boolean hasNext() {
			return i < colNum;
		}

		public Object[] next() {
			int no = i++;

			Vector vector = new Vector();
			for(int j=0; j<= sheet.getLastRowNum(); j++) {
				Row curRow = sheet.getRow(j);
				if(curRow == null) vector.add("");
				else {
					Cell cell = curRow.getCell(no);
					vector.add(Utils.getCellValue(cell, ""));
				}
			}
			return vector.toArray();
		}

		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}

	class RIterator implements Iterator<Object[]> {
		
		int i = 0;
		
		public boolean hasNext() {
			return i < sheet.getLastRowNum() + 1;
		}

		public Object[] next() {
			int no = i++;

			Vector vector = new Vector();
			Row curRow = sheet.getRow(no);
			if(curRow != null) {
				for(int j=0; j<= curRow.getLastCellNum(); j++) {
					Cell cell = curRow.getCell(j);
					vector.add(Utils.getCellValue(cell, ""));
				}
			}
			return vector.toArray();
		}

		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}
	

}
