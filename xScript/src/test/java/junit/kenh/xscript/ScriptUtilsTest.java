package junit.kenh.xscript;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import kenh.xscript.Environment;
import kenh.xscript.ScriptUtils;
import kenh.xscript.Element;
import kenh.xscript.Constant;
import kenh.xscript.UnsupportedScriptException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.endsWith;

public class ScriptUtilsTest {
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	//kenh.xscript.ScriptUtils.getInstance(Node node, Environment env) throws UnsupportedScriptException
	public void testGetInstance_Node_Exception() throws Throwable {
		thrown.expect(UnsupportedScriptException.class);
		thrown.expectMessage("The root element should be <script>. [kenh.xscript.elements.Method]");
		
		String content = 
			"<method>" +
			"	<set var=\"author\" value=\"Kenneth Huang\"/>" +
			"</method>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, null);
		
	}
	
	
	@Test
	//kenh.xscript.ScriptUtils.getInstance(Node node, Environment env) throws UnsupportedScriptException
	public void testGetInstance_Node() throws Throwable {
		
		String content = 
			"<script>" +
			"	<set var=\"author\" value=\"Kenneth Huang\"/>" +
			"</script>";
		
		Document doc = Main.stringToDocument(content);
		
		NodeList children = doc.getChildNodes();
		Node node = null;
		for(int i = 0; i< children.getLength(); i++) {
			Node n = children.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				node = n;
				break;
			}
		}
		
		Element e = ScriptUtils.getInstance(node, null);
		//ScriptUtils.debug(e);
		
		Assert.assertTrue(e instanceof kenh.xscript.elements.Script);
		Assert.assertTrue(e.getChildren().size() == 1);
		
		Element e_ = e.getChildren().get(0);
		Assert.assertTrue(e_ instanceof kenh.xscript.elements.Set);
		
		Assert.assertTrue(e_.getAttributes().size() == 2);
		Assert.assertEquals(e_.getAttribute("value"), "Kenneth Huang");
		Assert.assertEquals(e_.getAttribute("var"), "author");
	}
	
	@Test
	//kenh.xscript.ScriptUtils.getInstance(File file, Environment env) throws UnsupportedScriptException
	public void testGetInstance_File() throws Throwable {
		String sep = System.getProperty("file.separator");
		
		File f = new File(ScriptUtilsTest.class.getResource("demo" + sep + "demo1.xml").toURI());
		
		Element e = ScriptUtils.getInstance(f, null);
		e.getEnvironment().getVariable(Constant.VARIABLE_HOME);
		
		String dir = System.getProperty("user.dir"); 
		String package_ = StringUtils.replace(ScriptUtilsTest.class.getPackage().getName(), ".", sep);
		Object obj = e.getEnvironment().getVariable(Constant.VARIABLE_HOME);
		
		Assert.assertThat((String)obj, startsWith(dir));
		Assert.assertThat((String)obj, endsWith(package_ + sep + "demo"));
	}

}
