package junit.kenh.xscript.elements;

import kenh.xscript.Constant;

public class Exception extends kenh.xscript.impl.NoChildElement {

	public int process() {
		java.lang.Exception e = new java.lang.Exception("Oh~! Exception!!!!!!");
		
		this.getEnvironment().setVariable(Constant.VARIABLE_EXCEPTION, e);
		
		return EXCEPTION;
	}
	
}
