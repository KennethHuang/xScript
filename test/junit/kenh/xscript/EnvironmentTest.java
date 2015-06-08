package junit.kenh.xscript;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;

public class EnvironmentTest {
	
	private static kenh.xscript.Environment env = null;
	
	@ClassRule
	public static ExternalResource resource= new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			env = new kenh.xscript.Environment();
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
	
	@Test
	public void testContants() {
		String key = "KENH_CONSTANT";
		
		env.setVariable(key, "abcdef", true);
		
		List<String> constants = env.getContants();
		//Assert.assertThat(constants, hasItem(key));
		Assert.assertTrue(env.isConstant(key));
		Assert.assertFalse(env.isPublicVariable(key));
		
		try {
			env.removeVariable(key);
			Assert.fail("Constant can't be removed.");
		} catch(UnsupportedOperationException e) { }
		
		try {
			env.setVariable(key, "lmnopq");
			Assert.fail("Constant can't be updated.");
		} catch(UnsupportedOperationException e) { }
		
		constants.remove(key);
		env.removeVariable(key);
	}
	
	@Test
	public void testPublics() {
		String key = "KENH_PUBLIC";
		
		env.setPublicVariable(key, "abcdef", false);
		
		List<String> publics = env.getPublics();
		//Assert.assertThat(publics, hasItem(key));
		Assert.assertFalse(env.isConstant(key));
		Assert.assertTrue(env.isPublicVariable(key));
		
		env.removeVariable(key);
	}
	
	
	@Test
	public void testPublicsContants() {
		String key = "KENH_PUBLIC_CONSTANT";
		
		env.setPublicVariable(key, "abcdef", true);
		
		List<String> constants = env.getContants();
		//Assert.assertThat(constants, hasItem(key));
		Assert.assertTrue(env.isConstant(key));
		
		List<String> publics = env.getPublics();
		//Assert.assertThat(publics, hasItem(key));
		Assert.assertTrue(env.isPublicVariable(key));
		
		try {
			env.removeVariable(key);
			Assert.fail("Constant can't be removed.");
		} catch(UnsupportedOperationException e) { }
		
		try {
			env.setVariable(key, "lmnopq");
			Assert.fail("Constant can't be updated.");
		} catch(UnsupportedOperationException e) { }
		
		constants.remove(key);
		Assert.assertTrue(env.isPublicVariable(key));
		env.removeVariable(key);
		
		Assert.assertFalse(env.isConstant(key));
		Assert.assertFalse(env.isPublicVariable(key));
	}
	
	
	@Test
	public void testLoadElementPackages() {
		int i = 1;
		
		System.setProperty("kenh.xscript.element.packages.test", "junit.kenh.xscript.elements");
		kenh.xscript.Environment env = new kenh.xscript.Environment();
		System.clearProperty("kenh.xscript.element.packages.test");
		
		Assert.assertThat("kenh.xscript.elements.If", equalTo(env.getElement("if").getClass().getCanonicalName()));
		Assert.assertThat("junit.kenh.xscript.elements.Demo1", equalTo(env.getElement("demo1").getClass().getCanonicalName()));
		Assert.assertThat("junit.kenh.xscript.elements.If", equalTo(env.getElement("test", "if").getClass().getCanonicalName()));
		Assert.assertNull(env.getElement("test", "if_"));
		
	}
	
}
