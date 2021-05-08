package junit.kenh.xscript.io;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Main {
	
	public static void main(String[] args) {
		
		Class[] classes = new Class[] { 
				ElementTest.class,
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
