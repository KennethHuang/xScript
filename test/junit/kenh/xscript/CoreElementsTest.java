package junit.kenh.xscript;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.List;

import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;
import kenh.xscript.UnsupportedScriptException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;

public class CoreElementsTest {
	
	private static kenh.xscript.Environment env = null;
	
	@ClassRule
	public static ExternalResource resource= new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			env = new kenh.xscript.Environment(new kenh.expl.impl.ExpLParser());
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
	
	
	// ------------------------ xmlns for element/function
	
	@Test
	public void testXmlns_logic1() throws Throwable {
		String content = 
			"<script xmlns:demo=\"junit.kenh.xscript.elements\" xmlns:func.demo=\"junit.kenh.xscript.functions\">" +
			"	<demo:set_zero var=\"result\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"public result1\" value=\"{#demo:zero()}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("0", e.getEnvironment().getVariable("result"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("result1"));
	}
	
	
	
	// ------------------------ Script
	
	@Test
	public void testScript_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Could't find the method to invoke. [main]");
		
		String content = 
			"<script>" +
			"	<set var=\"author\" value=\"Kenneth Huang\"/>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}

	@Test
	public void testScript_error2() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Do not support this child element. [kenh.xscript.elements.Println]");
		
		String content = 
			"<script>" +
			"	<println>This is a test</println>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}
	
	@Test
	public void testScript_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"result\" value=\"call_main\"/>" +
			"	</method>" +
			"	<method name=\"main2\">" +
			"		<set var=\"result\" value=\"call_main2\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("call_main", e.getEnvironment().getVariable("result"));
	}
	
	@Test
	public void testScript_logic2() throws Throwable {
		String content = 
			"<script main-method=\"main2\" name=\"demo\">" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"result\" value=\"call_main\"/>" +
			"	</method>" +
			"	<method name=\"main2\">" +
			"		<set var=\"result\" value=\"call_main2\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("call_main2", e.getEnvironment().getVariable("result"));
		Assert.assertEquals("demo", e.getEnvironment().getVariable("@xScript"));
		Assert.assertEquals(System.getProperty("user.dir"), e.getEnvironment().getVariable("@home"));
		
	}
	
	
	
	// ------------------------ Set/Condition
	
	/**
	 * Test Condition element
	 * @throws Throwable
	 */
	@Test
	public void testSet_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"result\" value=\"1\">" +
			"			<condition cond=\"{result}==0\" value=\"2\"/>" +
			"			<condition cond=\"{result}==1\" value=\"3\"/>" +
			"			<condition cond=\"{result}==2\" value=\"4\"/>" +
			"		</set>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("2", e.getEnvironment().getVariable("result"));
	}
	
	/**
	 * Test public modifier
	 * @throws Throwable
	 */
	@Test
	public void testSet_logic2() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"global\" value=\"\" default=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"temp_but_not_removed\" value=\"\" default=\"2\"/>" +
			"		<call name=\"temp\"/>" +
			"	</method>" +
			"	<method name=\"temp\">" +
			"		<set var=\"public global2\" value=\"\" default=\"3\"/>" +
			"		<set var=\"temp_will_removed\" value=\"\" default=\"4\"/>" +
			"		<call name=\"temp2\"/>" +
			"	</method>" +
			"	<method name=\"temp2\">" +
			"		<set var=\"global2\" value=\"\" default=\"5\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("1", e.getEnvironment().getVariable("global"));
		Assert.assertEquals("2", e.getEnvironment().getVariable("temp_but_not_removed"));
		Assert.assertEquals("5", e.getEnvironment().getVariable("global2"));
		Assert.assertNull(e.getEnvironment().getVariable("temp_will_removed"));
	}
	
	/**
	 * Test final modifier
	 * @throws Throwable
	 */
	@Test
	public void testSet_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("[global] is a constant.");
		
		String content = 
			"<script>" +
			"	<set var=\"final global\" value=\"\" default=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"global\" value=\"{temp_in_main}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}

	/**
	 * Test final modifier
	 * @throws Throwable
	 */
	@Test
	public void testSet_logic3() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"global\" value=\"\" default=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"final temp_in_main\" value=\"\" default=\"2\"/>" +
			"		<call name=\"temp\"/>" +
			"	</method>" +
			"	<method name=\"temp\">" +
			"		<set var=\"temp_in_main\" value=\"\" default=\"4\"/>" +
			"		<set var=\"global\" value=\"{temp_in_main}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("4", e.getEnvironment().getVariable("global"));
		Assert.assertEquals("2", e.getEnvironment().getVariable("temp_in_main"));
	}
	
	
	
	// ------------------------ If/Then/Else/Ifthen
	
	/**
	 * Test If/Then/Else/Ifthen elements
	 * @throws Throwable
	 */
	@Test
	public void testIf_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"global\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<if cond=\"{global}==0\">" +
			"			<then>" +
			"				<set var=\"global\" value=\"1\"/>" +
			"			</then>" +
			"			<else>" +
			"				<set var=\"global\" value=\"2\"/>" +
			"			</else>" +
			"		</if>" +
			"		<ifthen cond=\"{global}==1\">" +
			"			<set var=\"public global2\" value=\"3\"/>" +
			"		</ifthen>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("1", e.getEnvironment().getVariable("global"));
		Assert.assertEquals("3", e.getEnvironment().getVariable("global2"));
	}	
	
	/**
	 * Test missing Then element error.
	 * @throws Throwable
	 */
	@Test
	public void testIf_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("Missing <then> element.");
		
		String content = 
			"<script>" +
			"	<set var=\"global\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<if cond=\"{global}==0\">" +
			"			<else>" +
			"				<set var=\"global\" value=\"1\"/>" +
			"			</else>" +
			"		</if>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}	
	
	
	
	// ------------------------ For/Break/Continue
	
	/**
	 * Test For element from/to
	 * @throws Throwable
	 */
	@Test
	public void testFor_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"global1\" value=\"0\"/>" +
			"	<set var=\"global2\" value=\"0\"/>" +
			"	<set var=\"global3\" value=\"0\"/>" +
			"	<set var=\"global4\" value=\"0\"/>" +
			"	<set var=\"global5\" value=\"0\"/>" +
			"	<set var=\"global1_index\" value=\"0\"/>" +
			"	<set var=\"global2_index\" value=\"0\"/>" +
			"	<set var=\"global3_index\" value=\"0\"/>" +
			"	<set var=\"global4_index\" value=\"0\"/>" +
			"	<set var=\"global5_index\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<for from=\"2\" to=\"5\">" +
			"			<set var=\"global1\" value=\"{{global1} + 1}\"/>" +
			"			<set var=\"global1_index\" value=\"{{global1_index} + {@index}}\"/>" +
			"		</for>" +
			"		<for from=\"10\" to=\"2\">" +
			"			<set var=\"global2\" value=\"{{global2} + 1}\"/>" +
			"			<set var=\"global2_index\" value=\"{{global2_index} + {@index}}\"/>" +
			"		</for>" +
			"		<for from=\"2\" to=\"7\" step=\"2\">" +
			"			<set var=\"global3\" value=\"{{global3} + 1}\"/>" +
			"			<set var=\"global3_index\" value=\"{{global3_index} + {@index}}\"/>" +
			"		</for>" +
			"		<for from=\"1\" to=\"10\">" +
			"			<ifthen cond=\"{@index} mod 2 == 1\">" +
			"				<continue/>" +
			"			</ifthen>" +
			"			<set var=\"global4\" value=\"{{global4} + 1}\"/>" +
			"			<set var=\"global4_index\" value=\"{{global4_index} + {@index}}\"/>" +
			"		</for>" +
			"		<for from=\"1\" to=\"10\">" +
			"			<ifthen cond=\"{global5_index} &gt; 10\">" +
			"				<break/>" +
			"			</ifthen>" +
			"			<set var=\"global5\" value=\"{{global5} + 1}\"/>" +
			"			<set var=\"global5_index\" value=\"{{global5_index} + {@index}}\"/>" +
			"		</for>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("4", e.getEnvironment().getVariable("global1"));
		Assert.assertEquals("9", e.getEnvironment().getVariable("global2"));
		Assert.assertEquals("3", e.getEnvironment().getVariable("global3"));
		Assert.assertEquals("5", e.getEnvironment().getVariable("global4"));
		Assert.assertEquals("5", e.getEnvironment().getVariable("global5"));
		Assert.assertEquals("14", e.getEnvironment().getVariable("global1_index"));
		Assert.assertEquals("54", e.getEnvironment().getVariable("global2_index"));
		Assert.assertEquals("12", e.getEnvironment().getVariable("global3_index"));
		Assert.assertEquals("30", e.getEnvironment().getVariable("global4_index"));
		Assert.assertEquals("15", e.getEnvironment().getVariable("global5_index"));
	}
	
	/**
	 * Test For element ref/sub-ref
	 * @throws Throwable
	 */
	@Test
	public void testFor_logic2() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"array1\" value=\"{#array(2,3,4,5)}\"/>" +
			"	<set var=\"global1\" value=\"0\"/>" +
			"	<set var=\"global2\" value=\"0\"/>" +
			"	<set var=\"global1_index\" value=\"0\"/>" +
			"	<set var=\"global2_index\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<for ref=\"array1\" sub-ref=\"no\">" +
			"			<set var=\"global1\" value=\"{{global1} + {no}}\"/>" +
			"			<set var=\"global1_index\" value=\"{{global1_index} + {@index}}\"/>" +
			"		</for>" +
			"		<for ref=\"array1\" sub-ref=\"no\" skip=\"2\">" +
			"			<set var=\"global2\" value=\"{{global2} + {no}}\"/>" +
			"			<set var=\"global2_index\" value=\"{{global2_index} + {@index}}\"/>" +
			"		</for>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("14", e.getEnvironment().getVariable("global1"));
		Assert.assertEquals("9", e.getEnvironment().getVariable("global2"));
		Assert.assertEquals("10", e.getEnvironment().getVariable("global1_index"));
		Assert.assertEquals("3", e.getEnvironment().getVariable("global2_index"));
	}	
	
	
	
	// ------------------------ While/Break/Continue
	
	/**
	 * Test While element
	 * @throws Throwable
	 */
	@Test
	public void testWhile_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"array1\" value=\"{#array(1,5,8,7,4)}\"/>" +
			"	<set var=\"global1\" value=\"0\"/>" +
			"	<set var=\"global2\" value=\"0\"/>" +
			"	<set var=\"global3\" value=\"0\"/>" +
			"	<set var=\"global4\" value=\"0\"/>" +
			"	<set var=\"global5\" value=\"0\"/>" +
			"	<set var=\"global1_index\" value=\"0\"/>" +
			"	<set var=\"global4_index\" value=\"0\"/>" +
			"	<set var=\"global5_index\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<while cond=\"{global1} &lt; 10\">" +
			"			<set var=\"global1\" value=\"{{global1} + 1}\"/>" +
			"			<set var=\"global1_index\" value=\"{{global1_index} + {@index}}\"/>" +
			"		</while>" +
			"		<while cond=\"false\">" +
			"			<set var=\"global2\" value=\"{{global2} + 1}\"/>" +
			"		</while>" +
			"		<while cond=\"false\" after=\"true\">" +
			"			<set var=\"global3\" value=\"{{global3} + 1}\"/>" +
			"		</while>" +
			"		<set var=\"ii\" value=\"0\"/>" +
			"		<while cond=\"{ii} &lt; {#length({array1})}\">" +
			"			<set var=\"ii\" value=\"{{ii} + 1}\"/>" +
			"			<ifthen cond=\"{array1[{{ii}-1}]} &gt; 5\">" +
			"				<continue/>" +
			"			</ifthen>" +
			"			<set var=\"global4\" value=\"{{global4} + {array1[{{ii}-1}]}}\"/>" +
			"			<set var=\"global4_index\" value=\"{{global4_index} + {@index}}\"/>" +
			"		</while>" +
			"		<set var=\"ii\" value=\"0\"/>" +
			"		<while cond=\"{ii} &lt; {#length({array1})}\">" +
			"			<set var=\"ii\" value=\"{{ii} + 1}\"/>" +
			"			<ifthen cond=\"{array1[{{ii}-1}]} &gt; 5\">" +
			"				<break/>" +
			"			</ifthen>" +
			"			<set var=\"global5\" value=\"{{global5} + {array1[{{ii}-1}]}}\"/>" +
			"			<set var=\"global5_index\" value=\"{{global5_index} + {@index}}\"/>" +
			"		</while>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("10", e.getEnvironment().getVariable("global1"));
		Assert.assertEquals("55", e.getEnvironment().getVariable("global1_index"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("global2"));
		Assert.assertEquals("1", e.getEnvironment().getVariable("global3"));
		Assert.assertEquals("10", e.getEnvironment().getVariable("global4"));
		Assert.assertEquals("8", e.getEnvironment().getVariable("global4_index"));
		Assert.assertEquals("6", e.getEnvironment().getVariable("global5"));
		Assert.assertEquals("3", e.getEnvironment().getVariable("global5_index"));
	}	
	
	
	
	// ------------------------ Accum
	@Test
	public void testAccum_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<accum var=\"a1\"/>" +
			"	<accum var=\"a2\" step=\"2\"/>" +
			"	<set var=\"a3\" value=\"3\"/>" +
			"	<method name=\"main\">" +
			"		<accum var=\"a1\"/>" +
			"		<accum var=\"a2\" step=\"2\"/>" +
			"		<accum var=\"public a3\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals(2, e.getEnvironment().getVariable("a1"));
		Assert.assertEquals(4, e.getEnvironment().getVariable("a2"));
		Assert.assertEquals(4, e.getEnvironment().getVariable("a3"));
	}
	
	
	
	// ------------------------ Clear
	
	@Test
	public void testClear_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"global\" value=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"public global1\" value=\"{global}\"/>" +
			"		<clear var=\"global\"/>" +
			"		<set var=\"public global2\" value=\"{global}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("1", e.getEnvironment().getVariable("global1"));
		Assert.assertEquals("", e.getEnvironment().getVariable("global2"));
	}
	
	
	
	// ------------------------ Include
	
	@Test
	public void testInclude_logic1() throws Throwable {
		String sep = System.getProperty("file.separator");
		File f = new File(ScriptUtilsTest.class.getResource("demo" + sep + "demo1.xml").toURI());
		String url = f.getCanonicalPath();
		
		String content = 
			"<script>" +
			"	<include file=\"" + url + "\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"public global\" value=\"{name}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("Kenneth Huang", e.getEnvironment().getVariable("global"));
	}

	@Test
	public void testInclude_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("The root element should be <script>.");
		
		String sep = System.getProperty("file.separator");
		File f = new File(ScriptUtilsTest.class.getResource("demo" + sep + "demo2.xml").toURI());
		String url = f.getCanonicalPath();
		
		String content = 
			"<script>" +
			"	<include file=\"" + url + "\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"public global\" value=\"{name}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}
	
	
	
	// ------------------------ Method/Call/Return
	
	/**
	 * Test method's parameter
	 * @throws Throwable
	 */
	@Test
	public void testMethod_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" param1=\"xml\" param2=\"Script\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"required param1, param2\">" +
			"		<set var=\"result\" value=\"{param1}{param2}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("xmlScript", e.getEnvironment().getVariable("result"));
	}
	
	/**
	 * Test method's parameter, required keyword
	 * @throws Throwable
	 */
	@Test
	public void testMethod_required_error1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"required param1, required final param2, required final default(0) param3, param4\">" +
			"		<set var=\"result\" value=\"{param1}{param2}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		try {
			e.invoke();
		} catch(UnsupportedScriptException e_) {
			Assert.assertEquals("Missing parameter. [param1]", e_.getMessage());
		}
		
		content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" param1=\"\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"required param1, required final param2, required final default(0) param3, param4\">" +
			"		<set var=\"result\" value=\"{param1}{param2}\"/>" +
			"	</method>" +
			"</script>";
			
		doc = Main.stringToDocument(content);
		e = ScriptUtils.getInstance(doc, env);
		
		try {
			e.invoke();
		} catch(UnsupportedScriptException e_) {
			Assert.assertEquals("Missing parameter. [param2]", e_.getMessage());
		}
		
		content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" param1=\"\" param2=\"\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"required param1, required final param2, required final default(0) param3, param4\">" +
			"		<set var=\"result\" value=\"{param1}{param2}\"/>" +
			"	</method>" +
			"</script>";
			
		doc = Main.stringToDocument(content);
		e = ScriptUtils.getInstance(doc, env);
		
		try {
			e.invoke();
		} catch(UnsupportedScriptException e_) {
			Assert.assertEquals("Missing parameter. [param3]", e_.getMessage());
		}
	
	}
	
	/**
	 * Test method's parameter, final keyword
	 * @throws Throwable
	 */
	@Test
	public void testMethod_final_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("[param1] is a constant.");
		
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" param1=\"\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"required final param1\">" +
			"		<set var=\"param1\" value=\"0\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
	}
	
	/**
	 * Test method's parameter, default keyword
	 * @throws Throwable
	 */
	@Test
	public void testMethod_default_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"0\"/>" +
			"	<set var=\"global1\" value=\"0\"/>" +
			"	<set var=\"global2\" value=\"0\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\"/>" +
			"		<set var=\"global1\" value=\"{result}\"/>" +
			"		<call name=\"method1\"/>" +
			"		<set var=\"global2\" value=\"{result}\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"default({result}) param1\">" +
			"		<set var=\"result\" value=\"{{param1} + 1}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("1", e.getEnvironment().getVariable("global1"));
		Assert.assertEquals("2", e.getEnvironment().getVariable("global2"));
	}
	
	/**
	 * Test method's return value
	 * @throws Throwable
	 */
	@Test
	public void testMethod_return_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" var=\"result\"/>" +
			"		<call name=\"method1\" var=\"result1\"/>" +
			"	</method>" +
			"	<method name=\"method1\">" +
			"		<return value=\"0\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("0", e.getEnvironment().getVariable("result"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("result1"));
	}
	
	/**
	 * Test method's return value
	 * @throws Throwable
	 */
	@Test
	public void testMethod_return_error1() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("The method does not have return value. [method1]");
		
		String content = 
			"<script>" +
			"	<set var=\"result\" value=\"1\"/>" +
			"	<method name=\"main\">" +
			"		<call name=\"method1\" var=\"result\"/>" +
			"	</method>" +
			"	<method name=\"method1\">" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
	}
	
	/**
	 * Test variable scope for method
	 * @throws Throwable
	 */
	@Test
	public void testMethod_var_scope_logic1() throws Throwable {
		String content = 
			"<script>" +
			"	<set var=\"var9\" value=\"9\"/>" +
			"	<method name=\"main\">" +
			"		<set var=\"var1\" value=\"0\"/>" +
			"		<set var=\"public var2\" value=\"0\"/>" +
			"		<set var=\"public var5\" value=\"0\"/>" +
			"		<set var=\"public final var6\" value=\"0\"/>" +
			"		<set var=\"public var7\" value=\"0\"/>" +
			"		<set var=\"public final var8\" value=\"0\"/>" +
			"		<call name=\"method1\" var5=\"2\" var6=\"6\" var7=\"7\" var8=\"8\" var=\"public var3\"/>" +
			"	</method>" +
			"	<method name=\"method1\" param=\"var5,var6,final var7,final var8,var9\">" +
			"		<set var=\"var1\" value=\"1\"/>" +
			"		<set var=\"var2\" value=\"1\"/>" +
			"		<set var=\"var4\" value=\"1\"/>" +
			"		<set var=\"public var9_in_method\" value=\"{var9}\"/>" +
			"		<return value=\"{var1}{var5}\"/>" +
			"	</method>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("0", e.getEnvironment().getVariable("var1"));
		Assert.assertEquals("1", e.getEnvironment().getVariable("var2"));
		Assert.assertEquals("12", e.getEnvironment().getVariable("var3"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var3"));
		Assert.assertEquals(false, e.getEnvironment().containsVariable("var4"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("var5"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var5"));
		Assert.assertFalse(e.getEnvironment().getContants().contains("var5"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("var6"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var6"));
		Assert.assertTrue(e.getEnvironment().getContants().contains("var6"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("var7"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var7"));
		Assert.assertFalse(e.getEnvironment().getContants().contains("var7"));
		Assert.assertEquals("0", e.getEnvironment().getVariable("var8"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var8"));
		Assert.assertTrue(e.getEnvironment().getContants().contains("var8"));
		Assert.assertEquals("", e.getEnvironment().getVariable("var9_in_method"));
		Assert.assertTrue(e.getEnvironment().getPublics().contains("var9_in_method"));
	}
	
	
	// ------------------------ Catch
	
	@Test
	public void testCatch_logic1() throws Throwable {
		String content = 
				"<script xmlns:demo=\"junit.kenh.xscript.elements\" xmlns:func.demo=\"junit.kenh.xscript.functions\">" +
				"	<method name=\"main\">" +
				"		<catch>" +
				"			<set var=\"public var1\" value=\"1\"/>" +
				"		</catch>" +
				"		<demo:exception/>" +
				"		<set var=\"public var2\" value=\"2\"/>" +
				"		<catch>" +
				"			<set var=\"public var3\" value=\"3\"/>" +
				"		</catch>" +
				"		<set var=\"public var4\" value=\"4\"/>" +
				"		<catch>" +
				"			<set var=\"public var5\" value=\"5\"/>" +
				"		</catch>" +
				"		<demo:exception/>" +
				"		<catch>" +
				"			<set var=\"public var6\" value=\"6\"/>" +
				"		</catch>" +
				"		<set var=\"public var7\" value=\"7\"/>" +
				"		<if cond=\"true\">" +
				"			<then>" +
				"				<demo:exception/>" +
				"			</then>" +
				"		</if>" +
				"		<catch>" +
				"			<set var=\"public var8\" value=\"{@exception.getMessage()}\"/>" +
				"		</catch>" +
				"		<for from=\"9\" to=\"11\">" +
				"			<set var=\"public var{@index}\" value=\"{@index}\"/>" +
				"			<demo:exception/>" +
				"		</for>" +
				"		<catch>" +
				"		</catch>" +
				"		<for from=\"12\" to=\"13\">" +
				"			<set var=\"public var{@index}\" value=\"{@index}\"/>" +
				"			<demo:exception/>" +
				"			<catch>" +
				"			</catch>" +
				"		</for>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertNull(e.getEnvironment().getVariable("var1"));
		Assert.assertNull(e.getEnvironment().getVariable("var2"));
		Assert.assertEquals("3", e.getEnvironment().getVariable("var3"));
		Assert.assertEquals("4", e.getEnvironment().getVariable("var4"));
		Assert.assertNull(e.getEnvironment().getVariable("var5"));
		Assert.assertEquals("6", e.getEnvironment().getVariable("var6"));
		Assert.assertEquals("7", e.getEnvironment().getVariable("var7"));
		Assert.assertEquals("Oh~! Exception!!!!!!", e.getEnvironment().getVariable("var8"));
		Assert.assertEquals("9", e.getEnvironment().getVariable("var9"));
		Assert.assertNull(e.getEnvironment().getVariable("var10"));
		Assert.assertNull(e.getEnvironment().getVariable("var11"));
		Assert.assertEquals("12", e.getEnvironment().getVariable("var12"));
		Assert.assertEquals("13", e.getEnvironment().getVariable("var13"));
	}
	
}
