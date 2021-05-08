package kenh.xscript.os.elements.dialog;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

import java.io.*;

import javax.swing.JFileChooser;

import org.apache.commons.lang3.StringUtils;

/**
 * Show a dir chooser dialog.
 * @author Kenneth
 *
 */
public class Folder extends NoChildElement {
	
	private static final String ATTRIBUTE_PATH = "path";
	private static final String ATTRIBUTE_VAR = "var";
	private static final String ATTRIBUTE_TITLE = "title";
	private static final String ATTRIBUTE_MULTI_SELECTION = "multi-select";
	
	public void process(@Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process("", null, false, var);
	}
	
	@Processing
	public void process_(@Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process("", title, false, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_MULTI_SELECTION) boolean multiSelect, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process("", null, multiSelect, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, null, false, var);
	}
	
	@Processing
	public void process_(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, title, false, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_MULTI_SELECTION) boolean multiSelect, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, null, multiSelect, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH)  kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, null, false, var);
	}
	
	@Processing
	public void process_(@Attribute(ATTRIBUTE_PATH)  kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, title, false, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH)  kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_MULTI_SELECTION) boolean multiSelect, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(path, null, multiSelect, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) kenh.xscript.os.beans.File path, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_MULTI_SELECTION) boolean multiSelect, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process((path == null)? "": path.getPath(), title, multiSelect, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_PATH) String path, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_MULTI_SELECTION) boolean multiSelect, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		if(StringUtils.isBlank(path)) {
			path = System.getProperties().getProperty("user.home");
			if(this.getEnvironment().containsVariable(Constant.VARIABLE_HOME)) {
				Object obj = this.getEnvironment().getVariable(Constant.VARIABLE_HOME);
				if(obj instanceof String) path = (String)obj;
			}
		}
		
		JFileChooser chooser = new JFileChooser(path);
		if(title != null) chooser.setDialogTitle(title);
		chooser.setMultiSelectionEnabled(multiSelect);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
		
		if(returnVal == JFileChooser.CANCEL_OPTION) {
			if(multiSelect) this.saveVariable(var, new kenh.xscript.os.beans.File[]{ }, null);
			else this.saveVariable(var, (kenh.xscript.os.beans.File)null, null);
			return;
		}
		
		if(multiSelect) {
			java.io.File[] files = chooser.getSelectedFiles();
			kenh.xscript.os.beans.File[] refs = new kenh.xscript.os.beans.File[files.length];
			
			for(int i=0; i< files.length; i++) {
				try {
					refs[i] = new kenh.xscript.os.beans.File(new $File$(files[i]));
				} catch(IOException e) {
					throw new UnsupportedScriptException(this, e);
				}
			}
			
			this.saveVariable(var, refs, null);
		} else {
			java.io.File file = chooser.getSelectedFile();
			try {
				kenh.xscript.os.beans.File ref = new kenh.xscript.os.beans.File(new $File$(file));
				this.saveVariable(var, ref, null);
			} catch(IOException e) {
				throw new UnsupportedScriptException(this, e);
			}
			
		}
	}
	
	class $File$ extends kenh.xscript.os.beans.File {
		$File$(java.io.File file) throws IOException {
			super(file);
		}
	}
	
}
