package junit.kenh.xscript.database;

import junit.kenh.xscript.database.functions.GetConnection;
import kenh.xscript.Element;
import kenh.xscript.ScriptUtils;
import kenh.xscript.database.beans.SQLBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.w3c.dom.Document;

public class ElementTest {
	
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
	public void testDatasource_Connection() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<set var=\"conn\" value=\"{#t:getConnection(5)}\"/>" +
				"	<method name=\"main\">" +
				"		<set var=\"public count2\" value=\"{#d:getInt(conn,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("5", e.getEnvironment().getVariable("count2"));
	}
	
	@Test
	public void testExecute_Commit() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<set var=\"conn1\" value=\"{#t:getConnection(5)}\"/>" +
				"	<method name=\"main\">" +
				"		<set value=\"{conn1.setAutoCommit(false)}\"/>" + 
				"		<set var=\"result1\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"		<d:execute var=\"result\" ref=\"conn1\">" +
				" 			<![CDATA[ INSERT INTO " + GetConnection.TABLE_NAME + "(str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL) ]]>" +
				"		</d:execute>" +
				"		<d:commit ref=\"conn1\"/>" + 
				"		<set var=\"result2\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("5", e.getEnvironment().getVariable("result1"));
		Assert.assertEquals("6", e.getEnvironment().getVariable("result2"));
	}

	@Test
	public void testExecute_Rollback() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<set var=\"conn1\" value=\"{#t:getConnection(5)}\"/>" +
				"	<method name=\"main\">" +
				"		<set value=\"{conn1.setAutoCommit(false)}\"/>" + 
				"		<set var=\"result1\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"		<d:execute var=\"result\" ref=\"conn1\">" +
				" 			<![CDATA[ INSERT INTO " + GetConnection.TABLE_NAME + "(str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL) ]]>" +
				"		</d:execute>" +
				"		<d:savepoint ref=\"conn1\" var=\"sp\"/>" + 
				"		<set var=\"result2\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"		<d:execute var=\"result\" ref=\"conn1\">" +
				" 			<![CDATA[ INSERT INTO " + GetConnection.TABLE_NAME + "(str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL) ]]>" +
				"		</d:execute>" +
				"		<set var=\"result3\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"		<d:execute var=\"result\" ref=\"conn1\">" +
				" 			<![CDATA[ INSERT INTO " + GetConnection.TABLE_NAME + "(str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL, NULL) ]]>" +
				"		</d:execute>" +
				"		<catch>" + 
				"			<d:rollback ref=\"conn1\" save-point=\"sp\"/>" + 
				"		</catch>" + 
				"		<d:commit ref=\"conn1\"/>" + 
				"		<set var=\"result4\" value=\"{#d:getInt(conn1,select count(*) from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("5", e.getEnvironment().getVariable("result1"));
		Assert.assertEquals("6", e.getEnvironment().getVariable("result2"));
		Assert.assertEquals("7", e.getEnvironment().getVariable("result3"));
		Assert.assertEquals("6", e.getEnvironment().getVariable("result4"));
		
	}
	
	@Test
	public void testSql() throws Throwable {
		String content = 
				"<script xmlns:d=\"kenh.xscript.database.elements\" " + 
				"        xmlns:f.d=\"kenh.xscript.database.functions\" " +
				"        xmlns:f.t=\"junit.kenh.xscript.database.functions\">" + 
				"	<d:sql var=\"updateTable\"><![CDATA[ update " + GetConnection.TABLE_NAME + " set STR_COL = ?, NUM_COL = ?  ]]>" + 
				"		<param name=\"STR\" value=\"KENH\"/>" +
				"		<param name=\"NUM\" value=\"123\"/>" +
				"	</d:sql>" +
				"	<set var=\"conn1\" value=\"{#t:getConnection(5)}\"/>" +
				"	<method name=\"main\">" +
				"		<set value=\"{conn1.setAutoCommit(true)}\"/>" + 
				"		<d:execute sql=\"updateTable\" ref=\"conn1\">" +
				"			<param name=\"STR\" link=\"NUM\"/>" +
				"		</d:execute>" +
				"		<set var=\"rs\" value=\"{#d:execute(conn1,select distinct str_col from " + GetConnection.TABLE_NAME + ")}\"/>" +
				"		<set var=\"str_col\" value=\"{rs.rows[1].str_col}\"/>" +
				"	</method>" +
				"</script>";
		
		Document doc = Main.stringToDocument(content);
		Element e = ScriptUtils.getInstance(doc, env);
		
		e.invoke();
		
		Assert.assertEquals("123", e.getEnvironment().getVariable("str_col"));
	}
}
