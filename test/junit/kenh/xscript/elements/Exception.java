package junit.kenh.xscript.elements;

import kenh.xscript.elements.Catch;

public class Exception extends kenh.xscript.impl.NoChildElement {

	public int process() {
		java.lang.Exception e = new java.lang.Exception("Oh~! Exception!!!!!!");
		
		this.getEnvironment().setVariable(Catch.VARIABLE_EXCEPTION, e);
		
		return EXCEPTION;
	}
	
}
