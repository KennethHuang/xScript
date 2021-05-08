package junit.kenh.xscript.elements;

import kenh.xscript.annotation.Text;

@Text(Text.Type.TRIM)
public class Text4 extends kenh.xscript.impl.BaseElement {

	public int process() {
		return NONE;
	}
	
	public Object getContext() throws java.lang.Exception {
		return this.getParsedText();
	}
}
