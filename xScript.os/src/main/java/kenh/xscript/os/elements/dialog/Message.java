package kenh.xscript.os.elements.dialog;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;

/**
 * Shows a message dialog.
 * @author Kenneth
 *
 */
@Text(Text.Type.TRIM)
public class Message extends NoChildElement {
	
	private static final String ATTRIBUTE_MESSAGE = "message";
	private static final String ATTRIBUTE_TITLE = "title";
	
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message) throws UnsupportedScriptException {
		javax.swing.JOptionPane.showMessageDialog(null, message);
	}
	
	public void process(@Attribute(ATTRIBUTE_MESSAGE) Object message, @Attribute(ATTRIBUTE_TITLE) String title) throws UnsupportedScriptException {
		javax.swing.JOptionPane.showMessageDialog(null, message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void process() throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message);
	}
	
	public void process(@Attribute(ATTRIBUTE_TITLE) String title) throws UnsupportedScriptException {
		Object message = this.getParsedText();
		process(message, title);
	}
	
}
