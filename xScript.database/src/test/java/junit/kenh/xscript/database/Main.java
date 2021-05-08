package junit.kenh.xscript.database;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.kenh.xscript.database.functions.GetConnection;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			GetConnection gConn = new GetConnection();
			java.sql.Connection conn = gConn.process();
			conn.close();
			
		} catch(Exception e) {
			System.out.println("Failure to initial local database environment.");
			System.out.println();
			e.printStackTrace();
			System.exit(-1);
		}
		
		Class[] classes = new Class[] { 
				BeanTest.class,
				ElementTest.class
		};
		
		Result result = org.junit.runner.JUnitCore.runClasses(classes);
		
		int runCount = result.getRunCount();
		int ignoreCount = result.getIgnoreCount();
		int failureCount = result.getFailureCount();
		System.out.println("Run: " + runCount + "/" + (runCount + ignoreCount));
		System.out.println("Failure: " + failureCount);
		System.out.println();
		
		if(failureCount > 0) {
			List<Failure> failures = result.getFailures();
			for(Failure failure: failures) {
				System.out.println(failure.toString());
			}
		}
	}
	
	public static Document stringToDocument(String content) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
			
			return doc;
		} catch(Exception e) {
			return null;
		}
		
	}
	

}
