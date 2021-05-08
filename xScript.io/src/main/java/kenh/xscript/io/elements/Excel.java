package kenh.xscript.io.elements;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.*;
import kenh.xscript.elements.*;
import kenh.xscript.*;

import java.io.File;

/**
 * Set a reference to point at a object of Excel
 * @author Kenneth
 *
 */
@Exclude(Element.class)
@IgnoreSuperClass
public class Excel extends Set {

	private static final String ATTRIBUTE_FILE = "file";	// the location of file
	private static final String ATTRIBUTE_VARIABLE = "var";	// the variable to store the reference.
	
	private static final String ATTRIBUTE_APPEND = "append";	// false, it will cover the file if it exist. Default is false
	private static final String ATTRIBUTE_READ_ONLY = "read-only";	// true/false value, refer to read only. Default is false
	
	
	//Part I--------- Use String as attribute 'file'
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) String file) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, false, false);
	}
	
	@Processing
	public void processReadOnly(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_READ_ONLY) boolean readOnly) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, readOnly, false);
	}
	
	@Processing
	public void processAppend(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_APPEND) boolean append) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, false, append);
	}
	
	@Processing
	public void processReadOnlyAppend(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) String file, @Attribute(ATTRIBUTE_READ_ONLY) boolean readOnly, @Attribute(ATTRIBUTE_APPEND) boolean append) throws UnsupportedScriptException {
		File f = null;
		try {
			f = new File(file);
			if(!f.isAbsolute() && this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
				Object obj = this.getEnvironment().getVariable(Constant.VARIABLE_HOME);
				if(obj instanceof String) f = new File((String)obj, file);
			}
			f = f.getCanonicalFile();
			
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
		
		processReadOnlyAppend(var, f, readOnly, append);
	}
	
	
	
	//Part II--------- Use java.io.File as attribute 'file'
	
	public void process(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) File file) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, false, false);
	}
	
	@Processing
	public void processReadOnly(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) File file, @Attribute(ATTRIBUTE_READ_ONLY) boolean readOnly) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, readOnly, false);
	}
	
	@Processing
	public void processAppend(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) File file, @Attribute(ATTRIBUTE_APPEND) boolean append) throws UnsupportedScriptException {
		processReadOnlyAppend(var, file, false, append);
	}
	
	@Processing
	public void processReadOnlyAppend(@Attribute(ATTRIBUTE_VARIABLE) String var, @Attribute(ATTRIBUTE_FILE) File file, @Attribute(ATTRIBUTE_READ_ONLY) boolean readOnly, @Attribute(ATTRIBUTE_APPEND) boolean append) throws UnsupportedScriptException {
		createExcel(var, file, readOnly, append);
	}
	
	
	/**
	 * Create excel file and save to reference.
	 * @param var
	 * @param file
	 * @param readyOnly
	 * @param append
	 * @throws UnsupportedScriptException
	 */
	private void createExcel(String var, File file, boolean readyOnly, boolean append) throws UnsupportedScriptException {
		if(file == null) {
			throw new UnsupportedScriptException(this, "File is empty.");
		}
		
		try {
			file = file.getCanonicalFile();
			
			kenh.xscript.io.impl.Excel excel = new kenh.xscript.io.impl.Excel(file, readyOnly, append);
			this.saveVariable(var, excel, null);
		} catch(Exception e) {
			throw new UnsupportedScriptException(this, e);
		}
	}
	
}
