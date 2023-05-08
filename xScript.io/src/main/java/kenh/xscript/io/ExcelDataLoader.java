package kenh.xscript.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.SAXHelper;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


public class ExcelDataLoader {

    private Vector<String[]> datas;
    private Vector<String> names;

    public ExcelDataLoader process(File file, String sheetName)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        datas = new Vector();
        names = new Vector();
        OPCPackage opcpPackage = OPCPackage.open(file);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcpPackage);
        XSSFReader xssfReader = new XSSFReader(opcpPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String name = iter.getSheetName();
            names.add(name);
            if(!StringUtils.equalsIgnoreCase(name, sheetName)) continue;
            processSheet(styles, strings, getHandler(), stream);
            IOUtils.closeQuietly(stream);
        }
        try {
            IOUtils.closeQuietly(opcpPackage);
        } catch(Exception e) { }
        return this;
    }

    public ExcelDataLoader process(File file, int index)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        datas = new Vector();
        names = new Vector();
        OPCPackage opcpPackage = OPCPackage.open(file);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcpPackage);
        XSSFReader xssfReader = new XSSFReader(opcpPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        int i = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String name = iter.getSheetName();
            names.add(name);
            if(i++ != index) continue;
            processSheet(styles, strings, getHandler(), stream);
            IOUtils.closeQuietly(stream);
        }
        try {
            IOUtils.closeQuietly(opcpPackage);
        } catch(Exception e) { }
        return this;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }

    public String[] getSheetNames() {
        return names.toArray(new String[]{});
    }

    private SheetContentsHandler getHandler() {
        return new SheetContentsHandler() {

            private boolean firstCellOfRow = false;
            private int currentRow = -1;
            private int currentCol = -1;

            private Vector<String> rowValues;

            public void startRow(int rowNum) {
                firstCellOfRow = true;
                currentRow = rowNum;
                currentCol = -1;
                rowValues = new Vector<String>();
            }

            public void endRow(int rowNum) {
                int size = datas.size();
                if(size == rowNum) {
                    datas.add(rowValues.toArray(new String[]{}));
                } else if(size > rowNum) {
                    datas.remove(rowNum);
                    datas.add(rowNum, rowValues.toArray(new String[]{}));
                } else if(size < rowNum) {
                    for(int i=0; i<(rowNum - size - 1); i++) {
                        datas.add(new String[]{});
                    }
                    datas.add(rowValues.toArray(new String[]{}));
                }
            }

            public void cell(String cellReference, String formattedValue, XSSFComment comment) {
                if (firstCellOfRow) {
                    firstCellOfRow = false;
                }

                if (cellReference == null) {
                    cellReference = new CellAddress(currentRow, currentCol).formatAsString();
                }

                int thisCol = (new CellReference(cellReference)).getCol();
                currentCol = thisCol;

                cellReference = cellReference.replaceAll("\\d","");
                int row = getRow(cellReference);

                if(row >= 0) {
                    int size = rowValues.size();
                    if (size == row) {
                        rowValues.add(formattedValue);
                    } else if (size > row) {
                        rowValues.remove(row);
                        rowValues.add(row, formattedValue);
                    } else if (size < row) {
                        for (int i = 0; i < (row - size); i++) {
                            rowValues.add(null);
                        }
                        rowValues.add(formattedValue);
                    }
                }

            }

            public void headerFooter(String text, boolean isHeader, String tagName) {
            }
        };
    }

    private static int getRow(String value) {
        value = StringUtils.upperCase(value);
        byte[] bytes = value.getBytes();
        int row = 0;

        int i = 0;
        for(byte b: bytes) {
            int index = StringUtils.indexOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ", b);
            if(index < 0) {
                break;
            }
            i++;
            row = row * 26 + index + 1;
        }
        return row - 1;
    }

    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler,
                             InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
        DataFormatter formatter = new DataFormatter() {
            @Override
            public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
                if ("m/d/yy".equals(formatString)) formatString = "m/d/yyyy";
                else if ("m/d/yy h:mm".equals(formatString)) formatString = "m/d/yyyy h:mm";
                return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
            }
        };
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader(new XmlOptions());
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

}