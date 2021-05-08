package junit.kenh.xscript.database;

import junit.kenh.xscript.database.functions.GetConnection;
import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;
import kenh.xscript.database.beans.ResultSetBean;
import kenh.xscript.database.beans.SQLBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.w3c.dom.Document;

public class BeanTest {
	
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
	
	@Test
	public void testResultSetBean() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<set var=\"conn\" value=\"{#t:getConnection(5)}\"/>" +
				"	<method name=\"main\">" +
				"		<set var=\"rs\" value=\"{#d:execute(conn,select * from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("0", e.getEnvironment().parse("{rs.rows[1].id}"));
		Assert.assertEquals("1", e.getEnvironment().parse("{rs.rows[2].1}"));
		Assert.assertEquals("java.lang.String", e.getEnvironment().parse("{rs.rows[1].str_col.getClass().getCanonicalName()}"));
		Assert.assertEquals("java.lang.Double", e.getEnvironment().parse("{rs.rows[1].num_col.getClass().getCanonicalName()}"));
		Assert.assertEquals("java.lang.Boolean", e.getEnvironment().parse("{rs.rows[1].bo_col.getClass().getCanonicalName()}"));
		Assert.assertEquals("java.sql.Timestamp", e.getEnvironment().parse("{rs.rows[1].date_col.getClass().getCanonicalName()}"));
		
		Assert.assertEquals("", e.getEnvironment().parse("{rs.rows[2].str_col}"));
		Assert.assertEquals("", e.getEnvironment().parse("{rs.rows[3].num_col}"));
		Assert.assertEquals("", e.getEnvironment().parse("{rs.rows[4].bo_col}"));
		Assert.assertEquals("", e.getEnvironment().parse("{rs.rows[5].date_col}"));
		
	}
	
	
	@Test
	public void testSQLBean() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<d:sql var=\"getTables\"><![CDATA[ select * from TABLE p where p.TABLE_ID = ? and p.TABLE_NAME = ? ]]>" + 
				"		<param name=\"TABLE ID\" value=\"123456\"/>" +
				"		<param name=\"TABLE NAME\" value=\"KENH\"/>" +
				"	</d:sql>" +
				"	<method name=\"main\">" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		SQLBean bean = (SQLBean)e.getEnvironment().getVariable("getTables");
		
		Assert.assertEquals("select * from TABLE p where p.TABLE_ID = ? and p.TABLE_NAME = ?", bean.getSql());
		Assert.assertEquals(2, bean.getParameters().size());
		Assert.assertEquals("TABLE ID", bean.getParameters().get(0).getName());
		Assert.assertEquals("TABLE NAME", bean.getParameters().get(1).getName());
		Assert.assertEquals("123456", bean.getParameters().get(0).getValue());
		Assert.assertEquals("KENH", bean.getParameters().get(1).getValue());
	}

}
