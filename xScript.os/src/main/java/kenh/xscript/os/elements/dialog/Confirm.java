package kenh.xscript.os.elements.dialog;

import kenh.xscript.annotation.*;
import kenh.xscript.impl.NormalElement;
import kenh.xscript.*;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Shows a confirm dialog.
 * @author Kenneth
 *
 */
public class Confirm extends NormalElement {
	
	private static final String ATTRIBUTE_MESSAGE = "message";
	private static final String ATTRIBUTE_TITLE = "title";
	
	public int process(@Attribute(ATTRIBUTE_MESSAGE) String message) throws UnsupportedScriptException {
		return process(message, UIManager.getString("OptionPane.titleText"));
	}
	public int process(@Attribute(ATTRIBUTE_MESSAGE) String message, @Attribute(ATTRIBUTE_TITLE) String title) throws UnsupportedScriptException {
		int value = javax.swing.JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		int result = this.NONE;
		if(value == javax.swing.JOptionPane.OK_OPTION) {
			result = invokeChildren();
		}
		
		return result;
	}

}
