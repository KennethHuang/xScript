package kenh.xscript.io.impl;

import java.io.*;
import java.util.*;

import kenh.expl.Callback;
import kenh.xscript.io.Reader;
import kenh.xscript.io.Utils;
import kenh.xscript.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * For text file reading and writing. line no start with 1.
 * @author Kenneth
 *
 */
public class Text implements Reader, Writer, Callback, Iterable {
	
	private static final String ATTRIBUTE_LINE = "line";	// used by write/read
	private static final String ATTRIBUTE_OPTS = "opt";		// used by write only
	
	private static final String OPT_INSERT = "insert";	// insert a new line and add the word in this line
	private static final String OPT_DELETE = "delete";	// delete the whole line
	private static final String OPT_APPEND = "append";	// add the word at the end of the certain line
	private static final String OPT_UPDATE = "update";	// update the word of the certain line
	private static final String OPT_NEW_LINE = "new-line";	// add the word at the end of the certain line and add the empty line next.
	
	private static final String OPT_INSERT_INITIAL = "i";
	private static final String OPT_DELETE_INITIAL = "d";
	private static final String OPT_APPEND_INITIAL = "a";
	private static final String OPT_UPDATE_INITIAL = "u";
	private static final String OPT_NEW_LINE_INITIAL = "n";

	private List<String> context = new LinkedList();
	private File file = null;
	private boolean isReadOnly = false;
	
	public Text(boolean readOnly, File f, boolean append) throws FileNotFoundException, IOException {
		isReadOnly = readOnly;
		file = f;
		
		if(isReadOnly && !file.exists()) throw new IOException("File is not exist");
		if(!file.exists()) file.getParentFile().mkdirs();
		
		if(isReadOnly) context = FileUtils.readLines(file);
		else if(append && file.exists()) {
			context = FileUtils.readLines(file);
		}
		
		if(context.size() <= 0) context.add("");
	}
	
	public Text(boolean readOnly, File f) throws FileNotFoundException, IOException {
		this(readOnly, f, false);
	}
	
	public Text(File f, boolean append) throws FileNotFoundException, IOException {
		this(false, f, append);
	}
	
	public Text(File f) throws FileNotFoundException, IOException {
		this(false, f, false);
	}
	
	public int getLineNumber() {
		return context.size();
	}
	
	
	public void write(Object obj, Map<String, Object> attributes) throws IOException, UnsupportedOperationException {
		if(this.isReadOnly) throw new UnsupportedOperationException("The file is read only.");
		
		if(!Utils.checkAttributes(attributes, true, ATTRIBUTE_LINE, ATTRIBUTE_OPTS)) {
			throw new UnsupportedOperationException("Only support <" + ATTRIBUTE_LINE + "> and <" + ATTRIBUTE_OPTS + ">.");
		}
		
		String str = "";
		if(obj != null) str = obj.toString();
		
		int line = getLine(attributes);
		if(line < 0) line = context.size();
		if(line == 0) line = 1;
		
		String opt = OPT_APPEND_INITIAL;
		if(attributes.containsKey(ATTRIBUTE_OPTS)) {
			opt = StringUtils.trimToEmpty(attributes.get(ATTRIBUTE_OPTS).toString());
			
			if(!Utils.match(opt, OPT_INSERT, OPT_DELETE, OPT_APPEND, OPT_UPDATE, OPT_NEW_LINE, OPT_INSERT_INITIAL, OPT_DELETE_INITIAL, OPT_APPEND_INITIAL, OPT_UPDATE_INITIAL, OPT_NEW_LINE_INITIAL )) {
				throw new UnsupportedOperationException("Unknown opt [" + opt + "].");
			}
		}
		
		if(StringUtils.equalsIgnoreCase(opt, OPT_APPEND_INITIAL) || StringUtils.equalsIgnoreCase(opt, OPT_APPEND) ) {
			context.set(line - 1, context.get(line - 1) + str);
			
		} else if(StringUtils.equalsIgnoreCase(opt, OPT_NEW_LINE_INITIAL) || StringUtils.equalsIgnoreCase(opt, OPT_NEW_LINE) ) {
			context.set(line - 1, context.get(line - 1) + str);
			if(line >= context.size()) context.add("");
			else context.add(line, "");
			
		} else if(StringUtils.equalsIgnoreCase(opt, OPT_INSERT_INITIAL) || StringUtils.equalsIgnoreCase(opt, OPT_INSERT) ) {
			context.add(line - 1, str);
			
		} else if(StringUtils.equalsIgnoreCase(opt, OPT_DELETE_INITIAL) || StringUtils.equalsIgnoreCase(opt, OPT_DELETE) ) {
			context.remove(line - 1);
			
		} else if(StringUtils.equalsIgnoreCase(opt, OPT_UPDATE_INITIAL) || StringUtils.equalsIgnoreCase(opt, OPT_UPDATE) ) {
			context.set(line - 1, str);
			
		}

	}
	
	public String read(Map<String, Object> attributes) throws IOException, UnsupportedOperationException {
		if(attributes.containsKey(ATTRIBUTE_LINE)) {
			if(attributes.size() > 1) {
				throw new UnsupportedOperationException("Only support an integer attribute <" + ATTRIBUTE_LINE + ">.");
			}
		} else {
			throw new UnsupportedOperationException("Required an integer attribute <" + ATTRIBUTE_LINE + ">.");
		}
		
		int line = getLine(attributes);
		
		if(line <= 0) {
			throw new UnsupportedOperationException("Wrong attribute <line>.");
		}
		
		if(line > this.getLineNumber()) {
			throw new UnsupportedOperationException("Line number overflow.");
		}
		
		return context.get(line - 1);
	}
	
	
	private static int getLine(Map<String, Object> attributes) {
		if(!attributes.containsKey(ATTRIBUTE_LINE)) return -1;
		
		int line = -1;
		Object obj = attributes.get(ATTRIBUTE_LINE);
		if(obj instanceof Integer) {
			line = (Integer)obj;
		} else if(obj instanceof String) {
			String str = (String)obj;
			if(StringUtils.isNumeric(str)) {
				line = Integer.parseInt(str);
			}
		}
		
		return line;
	}
	
	public void callback() {
		
		if(this.isReadOnly) return;
		
		try {
			FileUtils.writeLines(file, context);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		context.clear();
	}

	public Iterator iterator() {
		return context.iterator();
	}

}
