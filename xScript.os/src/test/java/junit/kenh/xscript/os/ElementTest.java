package junit.kenh.xscript.os;

import java.util.Arrays;

import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;
import kenh.xscript.os.Utils;

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
	
	private static kenh.xscript.Environment env = null;
	private static String path = System.getProperties().getProperty("user.home");
	private static String sub = "KENH";
	
	@ClassRule
	public static ExternalResource resource= new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			env = new kenh.xscript.Environment(new kenh.expl.impl.ExpLParser());
			
			String content = 
					"<script xmlns:d=\"kenh.xscript.os.elements.dialog\" >" + 
					"	<method name=\"main\">" + 
					"		<d:message>This is a demo, please choose a folder for testing.</d:message>" + 
					"		<d:folder var=\"folder\" title=\"Choose a folder for testing\" />" + 
					"		<set var=\"selectFolder\" value=\"false\" />" + 
					"		<ifthen cond=\"{#not({#isNull({folder})})}\">" + 
					"			<set var=\"selectFolder\" value=\"true\" />" + 
					"		</ifthen>" + 
					"	</method>" + 
					"</script>";
			
			Document doc = Main.stringToDocument(content);
			Element e = ScriptUtils.getInstance(doc, env);
			
			e.invoke();
			
			Assert.assertTrue("Folder is required.", StringUtils.equals(e.getEnvironment().getVariable("selectFolder").toString(),"true"));
			
			path = ((kenh.xscript.os.beans.File)e.getEnvironment().getVariable("folder")).getPath();
			java.io.File f = new java.io.File(path);
			
			Assert.assertTrue("Folder do not exist.", f.exists());
			Assert.assertTrue("Folder is not a folder.", f.isDirectory());
			
			java.io.File demoFolder = null;
			
			boolean emptyFolder = false;
			while(!emptyFolder) {
				String s = "KENH" + (int)(Math.random() * 100000);
				demoFolder = new java.io.File(f, s);
				if(!demoFolder.exists()) {
					emptyFolder = true;
					sub = s;
				}
			}
			
			if(!demoFolder.exists()) {
				demoFolder.mkdir();
				
				java.io.File demoFile = new java.io.File(demoFolder, "abc.txt");
				java.io.PrintStream out = null;
				
				try {
					
					out = new java.io.PrintStream(demoFile);
					out.println("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
					
				} catch(Exception e_) {
					
				} finally {
					if(out != null) try {out.close();} catch(Exception e_) {}
				}
			}
			
			env.callback();
			
			System.out.println("Test Folder: " + demoFolder.getCanonicalPath());
			System.out.println();
	    };
	    
	    @Override
	    protected void after() {
	    	env.callback();
	    	
	    	java.io.File demoFolder = new java.io.File(path, sub);
	    	
	    	java.io.File demo1 = new java.io.File(demoFolder, "ONE");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "TWO", "xyz.txt");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "TWO");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "SIX", "ONE", "THREE", "abc.txt");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "SIX", "ONE", "THREE");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "SIX", "ONE", "abc.txt");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "SIX", "ONE");
	    	demo1.delete();
	    	
	    	demo1 = Utils.getFile(demoFolder, "SIX");
	    	demo1.delete();
	    	
	    	Assert.assertTrue("Failure to delete demo folder. [" + demoFolder.getPath() + "]", demoFolder.delete());
	    };
	};
	
	@After
	public void afterEachTest() {
		if(env != null) env.callback();
	}
	
	@Test
	// Cd, Md, Mkdir
	public void test01() throws Throwable {
    	java.io.File demoFolder = new java.io.File(path, sub);
    	
		String content = 
				"<script xmlns:o=\"kenh.xscript.os.elements\" >" + 
				"	<method name=\"main\">" + 
				"		<o:cd path=\"" + demoFolder.getCanonicalPath() + "\" />" + 
				"		<o:cd var=\"folder1\" />" + 
				"		<o:md parent=\"" + demoFolder.getCanonicalPath() + "\" child=\"ONE\" />" + 
				"		<o:md path=\"." + java.io.File.separator + "TWO\" />" + 
				"		<o:cd path=\"." + java.io.File.separator + "ONE\" var=\"folder2\" />" + 
				"		<o:cd parent=\"" + demoFolder.getCanonicalPath() + "\" child=\"TWO\" var=\"folder3\" />" + 
				"		<o:md parent=\".." + java.io.File.separator + "ONE\" child=\"THREE\" />" + 
				"		<o:cd parent=\".." + java.io.File.separator + "ONE\" child=\"THREE\" var=\"folder4\" />" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("No.1", demoFolder.getCanonicalPath(), e.getEnvironment().getVariable("folder1"));
		Assert.assertEquals("No.2", new java.io.File(demoFolder, "ONE").getCanonicalPath(), e.getEnvironment().getVariable("folder2"));
		Assert.assertEquals("No.3", new java.io.File(demoFolder, "TWO").getCanonicalPath(), e.getEnvironment().getVariable("folder3"));
		Assert.assertEquals("No.4", new java.io.File(new java.io.File(demoFolder, "ONE"), "THREE").getCanonicalPath(), e.getEnvironment().getVariable("folder4"));
	}
		
	@Test
	// Copy
	public void test02() throws Throwable {
    	java.io.File demoFolder = new java.io.File(path, sub);
    	
		String content = 
				"<script xmlns:o=\"kenh.xscript.os.elements\" >" + 
				"	<method name=\"main\">" + 
				"		<o:cd path=\"" + demoFolder.getCanonicalPath() + "\" />" + 
				"		<o:copy path=\"abc.txt\" dest=\"ONE" + java.io.File.separator + "abc.txt\" />" + 
				"		<o:copy path=\"abc.txt\" dest=\"TWO\" />" + 
				"		<o:copy path=\"abc.txt\" dest=\"ONE" + java.io.File.separator + "THREE\" cut=\"true\" />" + 
				"		<o:copy path=\"ONE\" dest=\"FIVE\" cut=\"true\" />" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertFalse("No.1", new java.io.File(demoFolder, "ONE").exists());
		Assert.assertTrue("No.2", new java.io.File(new java.io.File(demoFolder, "TWO"), "abc.txt").exists());
		Assert.assertTrue("No.3", new java.io.File(new java.io.File(demoFolder, "FIVE"), "ONE").exists());
		Assert.assertTrue("No.4", new java.io.File(new java.io.File(new java.io.File(demoFolder, "FIVE"), "ONE"), "THREE").exists());
		Assert.assertTrue("No.5", new java.io.File(new java.io.File(new java.io.File(demoFolder, "FIVE"), "ONE"), "abc.txt").exists());
		Assert.assertTrue("No.6", new java.io.File(new java.io.File(new java.io.File(new java.io.File(demoFolder, "FIVE"), "ONE"), "THREE"), "abc.txt").exists());
	}
	
	
	@Test
	// Ren, Rename
	public void test03() throws Throwable {
    	java.io.File demoFolder = new java.io.File(path, sub);
    	
		String content = 
				"<script xmlns:o=\"kenh.xscript.os.elements\" >" + 
				"	<method name=\"main\">" + 
				"		<o:cd path=\"" + demoFolder.getCanonicalPath() + "\" />" + 
				"		<o:ren path=\"FIVE\" dest=\"SIX\" />" + 
				"		<o:ren parent=\"TWO\" child=\"abc.txt\" dest=\"xyz.txt\" />" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertFalse("No.1", new java.io.File(demoFolder, "FIVE").exists());
		Assert.assertTrue("No.2", new java.io.File(new java.io.File(demoFolder, "TWO"), "xyz.txt").exists());
		Assert.assertTrue("No.3", new java.io.File(new java.io.File(demoFolder, "SIX"), "ONE").exists());
		Assert.assertTrue("No.4", new java.io.File(new java.io.File(new java.io.File(demoFolder, "SIX"), "ONE"), "THREE").exists());
		Assert.assertTrue("No.5", new java.io.File(new java.io.File(new java.io.File(demoFolder, "SIX"), "ONE"), "abc.txt").exists());
		Assert.assertTrue("No.6", new java.io.File(new java.io.File(new java.io.File(new java.io.File(demoFolder, "SIX"), "ONE"), "THREE"), "abc.txt").exists());
	}
	
	@Test
	// Dir, Ls
	public void test04() throws Throwable {
    	java.io.File demoFolder = new java.io.File(path, sub);
    	
		String content = 
				"<script xmlns:o=\"kenh.xscript.os.elements\" >" + 
				"	<method name=\"main\">" + 
				"		<o:cd path=\"" + demoFolder.getCanonicalPath() + "\"/>" + 
				"		<o:ls path=\".\" var=\"folder1s\"/>" + 
				"		<o:ls path=\".\" var=\"folder2s\" file-only=\"true\"/>" + 
				"		<o:ls path=\"TWO\" var=\"folder3s\" type=\"txt,xls\"/>" + 
				"		<o:ls path=\"SIX\" var=\"folder4s\" type=\"txt\"/>" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertThat("NO.1", Arrays.asList((String[])e.getEnvironment().getVariable("folder1s")), org.hamcrest.CoreMatchers.hasItems("SIX", "TWO"));
		Assert.assertArrayEquals("NO.2", (String[])e.getEnvironment().getVariable("folder2s"), new String[] {});
		Assert.assertArrayEquals("NO.3", (String[])e.getEnvironment().getVariable("folder3s"), new String[] {"xyz.txt"});
		Assert.assertArrayEquals("NO.4", (String[])e.getEnvironment().getVariable("folder4s"), new String[] {});
	}

	@Test
	// File, Cd, Md, Ls
	public void test05() throws Throwable {
    	java.io.File demoFolder = new java.io.File(path, sub);
    	
		String content = 
				"<script xmlns:o=\"kenh.xscript.os.elements\" xmlns:f.o=\"kenh.xscript.os.functions\" >" + 
				"	<method name=\"main\">" + 
				"		<o:file path=\"" + demoFolder.getCanonicalPath() + "\" var=\"file1\" />" + 
				"		<o:cd path=\"{file1}\" var=\"folder1\" />" + 
				"		<o:file parent=\".\" child=\"ONE\"  var=\"file2\" />" + 
				"		<o:md path=\"{file2}\" />" + 
				"		<o:ls path=\"{file1}\" var=\"folders\"/>" + 
				"		<set var=\"file3\" value=\"{#o:getFile(SIX,ONE,abc.txt)}\"/>" + 
				"	</method>" + 
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("No.1", demoFolder.getCanonicalPath(), e.getEnvironment().getVariable("folder1"));
		Assert.assertTrue("No.2", new java.io.File(demoFolder, "ONE").exists());
		Assert.assertEquals("No.3", demoFolder.getCanonicalPath(), ((kenh.xscript.os.beans.File)e.getEnvironment().getVariable("file1")).getPath());
		Assert.assertEquals("No.4", new java.io.File(demoFolder, "ONE").getCanonicalPath(), ((kenh.xscript.os.beans.File)e.getEnvironment().getVariable("file2")).getPath());
		Assert.assertThat("NO.5", Arrays.asList((String[])e.getEnvironment().getVariable("folders")), org.hamcrest.CoreMatchers.hasItems("ONE", "TWO", "SIX"));
		Assert.assertEquals("No.6", new java.io.File(new java.io.File(new java.io.File(demoFolder, "SIX"), "ONE"), "abc.txt").getCanonicalPath(), ((kenh.xscript.os.beans.File)e.getEnvironment().getVariable("file3")).getPath());
	}
	
}

