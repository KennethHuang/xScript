package junit.kenh.xscript.elements;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Processing;

public class Demo1 extends kenh.xscript.impl.NoChildElement {

	public void process(@Attribute("a") boolean a, @Attribute("c") int c) {
		
	}
	
	public void process(@Attribute("a") boolean a, @Attribute("b") boolean b) {
		
	}
	
	@Processing
	public void process(@Attribute("s") String str) {
		
	}
	
	@Processing
	public int processA(@Attribute("x") boolean a) {
		return 1;
	}
	
	@Processing
	public Integer processB(@Attribute("x") int a) {
		return 2;
	}
	
	@Processing
	public int processC(@Attribute("y") int a) {
		return 3;
	}
	
	@Processing
	public String processD(@Attribute("z") String a) {
		return a;
	}
	
}
