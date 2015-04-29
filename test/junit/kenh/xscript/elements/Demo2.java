package junit.kenh.xscript.elements;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Primal;
import kenh.xscript.annotation.Processing;
import kenh.xscript.annotation.Reparse;

public class Demo2 extends kenh.xscript.impl.NoChildElement {

	private String s1, s2;
	
	public void process(@Primal@Attribute("one") String s1, @Attribute("two") String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	@Processing@Primal
	public void process2(@Attribute("s1") String s1, @Attribute("s2") String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	
	@Processing
	public void process3(@Reparse@Attribute("a") String s1, @Attribute("b") String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	
	public String getS1() {
		return s1;
	}
	
	public String getS2() {
		return s2;
	}
	
}
