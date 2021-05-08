package kenh.xscript.os.elements.dialog;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

import java.util.*;

import javax.swing.UIManager;

/**
 * Shows a dialog requesting input.
 * @author Kenneth
 *
 */
@Text(Text.Type.TRIM)
public class Input extends NoChildElement {
	
	private static final String ATTRIBUTE_VAR = "var";
	private static final String ATTRIBUTE_MESSAGE = "message";
	private static final String ATTRIBUTE_TITLE = "title";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_VALUES = "values";
	
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		String inputValue = javax.swing.JOptionPane.showInputDialog(message);
		this.saveVariable(var, inputValue, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object inputValue = javax.swing.JOptionPane.showInputDialog(message, value);
		this.saveVariable(var, inputValue, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_VALUES) Object values, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		process(message, UIManager.getString("OptionPane.messageDialogTitle"), values, value, var);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		String inputValue = javax.swing.JOptionPane.showInputDialog(null, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE);
		this.saveVariable(var, inputValue, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object inputValue = javax.swing.JOptionPane.showInputDialog(null, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE, null, null, value);
		this.saveVariable(var, inputValue, null);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VALUES) Object values, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		
		if (values.getClass().isArray()) {
			Object inputValue = javax.swing.JOptionPane.showInputDialog(null, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE, null, (Object[])values, value);
			this.saveVariable(var, inputValue, null);
		} else if(values instanceof Iterable) {
			Iterable values_ = (Iterable)values;
			
			Vector vec = new Vector();
			for(Object o: values_) {
				vec.add(o);
			}
			
			Object inputValue = javax.swing.JOptionPane.showInputDialog(null, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE, null, vec.toArray(), value);
			this.saveVariable(var, inputValue, null);
		}
		
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, var);
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, value, var);
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_VALUES) Object values, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, values, value, var);
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, title, var);
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, title, value, var);
	}
	
	@Processing
	public void processMessage(@Attribute(ATTRIBUTE_TITLE) String title, @Attribute(ATTRIBUTE_VALUES) Object values, @Attribute(ATTRIBUTE_VALUE) Object value, @Attribute(ATTRIBUTE_VAR) String var) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, title, values, value, var);
	}

}
