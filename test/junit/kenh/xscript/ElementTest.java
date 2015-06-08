package junit.kenh.xscript;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;
import kenh.xscript.UnsupportedScriptException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;

public class ElementTest {
	
	private static kenh.xscript.Environment env = null;
	
	@ClassRule
	public static ExternalResource resource= new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			env = new kenh.xscript.Environment(new kenh.expl.impl.ExpLParser());
			env.setElementPackage("test", "junit.kenh.xscript.elements");
	    };
	    
	    @Override
	    protected void after() {
	    	env.callback();
	    };
	};
	
	@After
	public void afterEachTest() {
		if(env != null) env.callback();
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Test
	public void testAttributeName1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Do not support this attribute group. [a]");
		
		Element e = env.getElement("test", "if");
		e.setAttribute("a", "b");
	}

	
	@Test
	public void testAttributeName2() throws Throwable {
		String value = "AbCdEf";
		
		Element e = env.getElement("test", "demo1");
		e.setAttribute("a", value);
		Assert.assertEquals(value, e.getAttribute("a"));
		Assert.assertEquals(1, e.getAttributes().size());
		
		e = env.getElement("test", "demo1");
		e.setAttribute("s", value);
		Assert.assertEquals(value, e.getAttribute("s"));
		Assert.assertEquals(1, e.getAttributes().size());
	}
	
	@Test
	public void testAttributeName3() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Do not support this attribute group. [b,c]");
		
		Element e = env.getElement("test", "demo1");
		e.setAttribute("b", "bbb");
		e.setAttribute("c", "ccc");
		
	}
	
	@Test
	public void testAttributeName4() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Reduplicate attribute. [b]");
		
		Element e = env.getElement("test", "demo1");
		e.setAttribute("b", "bbb");
		e.setAttribute("b", "ccc");
		
	}
	
	@Test
	public void testProcessMethod1() throws Throwable {
		Element e = env.getElement("test", "demo1");
		e.setEnvironment(env);
		e.setAttribute("x", "bbb");
		Assert.assertEquals(1, e.invoke());
		
		e = env.getElement("test", "demo1");
		e.setEnvironment(env);
		e.setAttribute("x", "11");
		Assert.assertEquals(2, e.invoke());
		
		
		e = env.getElement("test", "demo1");
		e.setEnvironment(env);
		e.setAttribute("z", "bbb");
		Assert.assertEquals(0, e.invoke());
		
	}
	
	@Test
	public void testProcessMethod2() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Can't fine the method to process.");
		
		Element e = env.getElement("test", "demo1");
		e.setEnvironment(env);
		e.setAttribute("y", "bbb");
		
		e.invoke();
	}
	
	
	@Test
	public void testPrimal() throws Throwable {
		
		Element e = env.getElement("test", "demo2");
		e.setEnvironment(env);
		e.setAttribute("one", "{1+1}");
		e.setAttribute("two", "{2+2}");
		
		e.invoke();
		
		Assert.assertEquals("{1+1}", ((junit.kenh.xscript.elements.Demo2)e).getS1());
		Assert.assertEquals("4", ((junit.kenh.xscript.elements.Demo2)e).getS2());
		
		e = env.getElement("test", "demo2");
		e.setEnvironment(env);
		e.setAttribute("s1", "{1+1}");
		e.setAttribute("s2", "{2+2}");
		
		e.invoke();
		
		Assert.assertEquals("{1+1}", ((junit.kenh.xscript.elements.Demo2)e).getS1());
		Assert.assertEquals("{2+2}", ((junit.kenh.xscript.elements.Demo2)e).getS2());
	}
	
	
	@Test
	public void testReparse() throws Throwable {
		
		Element e = env.getElement("test", "demo2");
		e.setEnvironment(env);
		e.setAttribute("a", "{1+1}+1");
		e.setAttribute("b", "{2+2}+2");
		
		e.invoke();
		
		Assert.assertEquals("3", ((junit.kenh.xscript.elements.Demo2)e).getS1());
		Assert.assertEquals("4+2", ((junit.kenh.xscript.elements.Demo2)e).getS2());
		
	}
	
	@Test
	public void testText() throws Throwable {
		
		String text = "  {1+1}  ";
		
		Element e = env.getElement("test", "Text1");
		try {
			e.setText(text);
			Assert.fail("Do not allow text(Without @Text).");
		} catch(UnsupportedScriptException ex) {
			Assert.assertEquals("Do not support text.", ex.getMessage());
		}
		
		e = env.getElement("test", "Text2");
		try {
			e.setText(text);
			Assert.fail("Do not allow text.");
		} catch(UnsupportedScriptException ex) {
			Assert.assertEquals("Do not support text.", ex.getMessage());
		}
		
		e = env.getElement("test", "Text3");
		e.setEnvironment(env);
		e.setText(text);
		Assert.assertEquals(text, e.getText());
		Assert.assertEquals("  2  ", ((junit.kenh.xscript.elements.Text3)e).getContext());
		
		e = env.getElement("test", "Text4");
		e.setEnvironment(env);
		e.setText(text);
		Assert.assertEquals(StringUtils.trim(text), e.getText());
		Assert.assertEquals("2", ((junit.kenh.xscript.elements.Text4)e).getContext());
	}
	
	@Test
	public void testChildren() throws Throwable {
		
		Element e = env.getElement("test", "Parent");
		
		Element c1 = env.getElement("test", "Child1");
		e.addChild(c1);
		
		c1 = env.getElement("test", "Child4");
		e.addChild(c1);
		
		try {
			c1 = env.getElement("test", "Child1");
			e.addChild(c1);
			Assert.fail("Only allow 2 children.");
		} catch(UnsupportedScriptException ex) {
			Assert.assertEquals("Do not support this child element. [junit.kenh.xscript.elements.Child1]", ex.getMessage());
		}
		
		for(int i=1; i<= 10; i++) {
			Element c2 = env.getElement("test", "Child2");
			e.addChild(c2);
		}
		
		try {
			Element c3 = env.getElement("test", "Child3");
			e.addChild(c3);
			Assert.fail("Do not allow this child.");
		} catch(UnsupportedScriptException ex) {
			Assert.assertEquals("Do not support this child element. [junit.kenh.xscript.elements.Child3]", ex.getMessage());
		}
		
	}
	
	@Test
	public void testIgnoreSuperClass() throws Throwable  {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Can't fine the method to process.");
		
		String content = 
				"<script xmlns:demo=\"junit.kenh.xscript.elements\" xmlns:func.demo=\"junit.kenh.xscript.functions\">" +
				"	<demo:set_zero var=\"result\" value=\"1\"/>" +
				"	<method name=\"main\">" +
				"		<set var=\"public result1\" value=\"{#demo:zero()}\"/>" +
				"	</method>" +
				"</script>";
			
			Document doc = Main.stringToDocument(content);
			Element e = ScriptUtils.getInstance(doc, null);
			
			e.invoke();
	}
}
