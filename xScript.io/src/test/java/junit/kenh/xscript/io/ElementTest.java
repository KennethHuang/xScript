package junit.kenh.xscript.io;

import java.util.Arrays;

import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runners.MethodSorters;
import org.w3c.dom.Document;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElementTest {
	
	private static long curr = (new java.util.Date()).getTime();	
	
	private static kenh.xscript.Environment env = null;
	
	@After
	public void afterEachTest() {
		if(env != null) env.callback();
	}
	
	@ClassRule
	public static ExternalResource resource= new ExternalResource() {
		
		@Override
		protected void before() throws Throwable {
			env = new kenh.xscript.Environment(new kenh.expl.impl.ExpLParser());
			
			String content = 
					"<script xmlns:i=\"kenh.xscript.io.elements\">" + 
					"	<i:excel var=\"test_xls\" file=\"K_" + curr + "\\test.xls\"/>" + 
					"	<i:text var=\"test_txt\" file=\"K_" + curr + "\\test.txt\"/>" + 
					"	<method name=\"main\">" + 
					"		<set var=\"name\" value=\"KENH\"/>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet2\" row=\"2\" col=\"2\">ABCDEFG</i:write>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet3\" row=\"3\" col=\"3\">{name}</i:write>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet3\" row=\"3\" col=\"3\">--{name}</i:write>" + 
					"		<for from=\"0\" to=\"10\">" + 
					"			<i:write ref=\"test_xls\" sheet=\"sheet1\" row=\"0\" col=\"{___index___}\">X{___index___}</i:write>" + 
					"		</for>" + 
					"		<for from=\"0\" to=\"10\">" + 
					"			<i:write ref=\"test_xls\" sheet=\"sheet1\" row=\"1\" col=\"{___index___}\">Y{___index___}</i:write>" + 
					"		</for>" + 
					"		<for from=\"0\" to=\"10\">" + 
					"			<i:write ref=\"test_xls\" sheet=\"sheet1\" row=\"2\" col=\"{___index___}\">Z{___index___}</i:write>" + 
					"		</for>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet4\" row=\"0\" col=\"3\">d3</i:write>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet4\" row=\"1\" col=\"2\">c2</i:write>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet4\" row=\"2\" col=\"1\">b1</i:write>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet4\" row=\"3\" col=\"0\">a0</i:write>" + 
					"		<i:read ref=\"test_xls\" sheet=\"sheet1\" row=\"1\" col=\"1\" var=\"aaa\"/>" + 
					"		<i:write ref=\"test_xls\" sheet=\"sheet2\" row=\"3\" col=\"2\">{aaa}</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">A</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">BB</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">DD</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">E</i:write>" + 
					"		<i:write ref=\"test_txt\">F</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"a\">G</i:write>" + 
					"		<i:write ref=\"test_txt\">H</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\"/>" + 
					"		<i:write ref=\"test_txt\">{name}</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"i\">I</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"i\" line=\"3\">CCC</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"u\" line=\"8\">--{name}</i:write>" + 
					"		<i:read ref=\"test_txt\" var=\"bbb\" line=\"8\"/>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\"/>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">{name}</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"d\" line=\"8\"/>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\" line=\"7\">JK</i:write>" + 
					"		<i:write ref=\"test_txt\" opt=\"d\"/>" + 
					"		<i:write ref=\"test_txt\" opt=\"n\">{bbb}</i:write>" + 
					"	</method>" + 
					"</script>";
			
			Document doc = Main.stringToDocument(content);
			Element e = ScriptUtils.getInstance(doc, env);
			
			e.invoke();
			
			env.callback();
	    };
	    
	    @Override
	    protected void after() {
	    	env.callback();
	    	
	    	java.io.File demoFolder = new java.io.File("K_" + curr);
	    	
	    	java.io.File demo1 = new java.io.File("K_" + curr, "test.xls");
	    	demo1.delete();
	    	
	    	java.io.File demo2 = new java.io.File("K_" + curr, "test.txt");
	    	demo2.delete();
	    	
	    	demoFolder.delete();
	    	
	    };
	};
	
	@Test
	public void testExcel() throws Throwable {
		
		String content = 
				"<script xmlns:i=\"kenh.xscript.io.elements\">" + 
				"	<i:excel var=\"final excel1\" file=\"K_" + curr + "\\test.xls\" read-only=\"true\"/>" +
				"	<method name=\"main\">" + 
				"		<i:excel var=\"excel2\" file=\"K_" + curr + "\\test.xls\" read-only=\"true\"/>" +
				"		<for ref=\"{excel1.getSheet('sheet1')}\" sub-ref=\"row\">" + 
				"			<set var=\"A_{row[0]}\" value=\"{row[1]}\"/>" + 
				"			<set var=\"A_{row[1]}\" value=\"{row[2]}\"/>" + 
				"			<set var=\"A_{row[2]}\" value=\"{row[3]}\"/>" + 
				"		</for>" + 
				"		<for from=\"0\" to=\"{{excel1.getSheet('sheet1').getRowNum()-1}}\">" + 
				"			<set var=\"row\" value=\"{___index___}\"/>" +
				"			<for from=\"0\" to=\"{{excel1.getSheet('sheet1').getColNum()-1}}\">" + 
				"				<set var=\"col\" value=\"{___index___}\"/>" +
				"				<i:read ref=\"excel1\" sheet=\"sheet1\" row=\"{row}\" col=\"{col}\" var=\"B_{row}_{col}\"/>" + 
				"			</for>" + 
				"		</for>" + 
				"		<i:read ref=\"excel2\" sheet=\"sheet2\" row=\"2\" col=\"2\" var=\"C1\"/>" + 
				"		<i:read ref=\"excel2\" sheet=\"sheet2\" row=\"3\" col=\"2\" var=\"C2\"/>" + 
				"		<i:read ref=\"excel2\" sheet=\"sheet3\" row=\"3\" col=\"3\" var=\"C3\"/>" + 
				"		<for ref=\"{excel2.getSheet('sheet4').cols()}\" sub-ref=\"col\">" + 
				"			<set var=\"D_{___index___}_0\" value=\"{col[0]}\"/>" + 
				"			<set var=\"D_{___index___}_1\" value=\"{col[1]}\"/>" + 
				"			<set var=\"D_{___index___}_2\" value=\"{col[2]}\"/>" + 
				"			<set var=\"D_{___index___}_3\" value=\"{col[3]}\"/>" + 
				"		</for>" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		e.invoke();
		
		Assert.assertEquals("X1", env.getVariable("A_X0"));
		Assert.assertEquals("X2", env.getVariable("A_X1"));
		Assert.assertEquals("X3", env.getVariable("A_X2"));
		Assert.assertEquals("Y1", env.getVariable("A_Y0"));
		Assert.assertEquals("Y2", env.getVariable("A_Y1"));
		Assert.assertEquals("Y3", env.getVariable("A_Y2"));
		Assert.assertEquals("Z1", env.getVariable("A_Z0"));
		Assert.assertEquals("Z2", env.getVariable("A_Z1"));
		Assert.assertEquals("Z3", env.getVariable("A_Z2"));
		
		Assert.assertEquals("X0", env.getVariable("B_0_0"));
		Assert.assertEquals("X1", env.getVariable("B_0_1"));
		Assert.assertEquals("X2", env.getVariable("B_0_2"));
		Assert.assertEquals("X3", env.getVariable("B_0_3"));
		Assert.assertEquals("X4", env.getVariable("B_0_4"));
		Assert.assertEquals("X5", env.getVariable("B_0_5"));
		Assert.assertEquals("X6", env.getVariable("B_0_6"));
		Assert.assertEquals("X7", env.getVariable("B_0_7"));
		Assert.assertEquals("X8", env.getVariable("B_0_8"));
		Assert.assertEquals("X9", env.getVariable("B_0_9"));
		Assert.assertEquals("X10", env.getVariable("B_0_10"));
		Assert.assertEquals("Y0", env.getVariable("B_1_0"));
		Assert.assertEquals("Y1", env.getVariable("B_1_1"));
		Assert.assertEquals("Y2", env.getVariable("B_1_2"));
		Assert.assertEquals("Y3", env.getVariable("B_1_3"));
		Assert.assertEquals("Y4", env.getVariable("B_1_4"));
		Assert.assertEquals("Y5", env.getVariable("B_1_5"));
		Assert.assertEquals("Y6", env.getVariable("B_1_6"));
		Assert.assertEquals("Y7", env.getVariable("B_1_7"));
		Assert.assertEquals("Y8", env.getVariable("B_1_8"));
		Assert.assertEquals("Y9", env.getVariable("B_1_9"));
		Assert.assertEquals("Y10", env.getVariable("B_1_10"));
		Assert.assertEquals("Z0", env.getVariable("B_2_0"));
		Assert.assertEquals("Z1", env.getVariable("B_2_1"));
		Assert.assertEquals("Z2", env.getVariable("B_2_2"));
		Assert.assertEquals("Z3", env.getVariable("B_2_3"));
		Assert.assertEquals("Z4", env.getVariable("B_2_4"));
		Assert.assertEquals("Z5", env.getVariable("B_2_5"));
		Assert.assertEquals("Z6", env.getVariable("B_2_6"));
		Assert.assertEquals("Z7", env.getVariable("B_2_7"));
		Assert.assertEquals("Z8", env.getVariable("B_2_8"));
		Assert.assertEquals("Z9", env.getVariable("B_2_9"));
		Assert.assertEquals("Z10", env.getVariable("B_2_10"));
		
		Assert.assertEquals("ABCDEFG", env.getVariable("C1"));
		Assert.assertEquals("Y1", env.getVariable("C2"));
		Assert.assertEquals("--KENH", env.getVariable("C3"));
		
		Assert.assertEquals("a0", env.getVariable("D_1_3"));
		Assert.assertEquals("b1", env.getVariable("D_2_2"));
		Assert.assertEquals("c2", env.getVariable("D_3_1"));
		Assert.assertEquals("d3", env.getVariable("D_4_0"));
		
	}
		
	@Test
	public void testText() throws Throwable {
		String content = 
				"<script xmlns:i=\"kenh.xscript.io.elements\">" + 
				"	<i:text var=\"final text1\" file=\"K_" + curr + "\\test.txt\" read-only=\"true\"/>" +
				"	<method name=\"main\">" + 
				"		<i:text var=\"text2\" file=\"K_" + curr + "\\test.txt\" read-only=\"true\"/>" +
				"		<for ref=\"text1\" sub-ref=\"txt\">" + 
				"			<set var=\"A_{___index___}\" value=\"{txt}\"/>" + 
				"		</for>" + 
				"		<i:read ref=\"text2\" var=\"B1\" line=\"6\"/>" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("A", env.getVariable("A_1"));
		Assert.assertEquals("BB", env.getVariable("A_2"));
		Assert.assertEquals("CCC", env.getVariable("A_3"));
		Assert.assertEquals("DD", env.getVariable("A_4"));
		Assert.assertEquals("E", env.getVariable("A_5"));
		Assert.assertEquals("FGH", env.getVariable("A_6"));
		Assert.assertEquals("IJK", env.getVariable("A_7"));
		Assert.assertEquals("", env.getVariable("A_8"));
		Assert.assertEquals("KENH--KENH", env.getVariable("A_9"));
		//Assert.assertEquals("", env.getVariable("A_10")); 
		
		Assert.assertEquals("FGH", env.getVariable("B1"));
	}
	
}

