/*
 * xScript (XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.
 * 
 * xScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript;

import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.util.*;
import java.lang.reflect.Method;
import java.net.URL;

import kenh.xscript.elements.Script;

/**
 * Static method for initialing xScript instance.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class ScriptUtils {
	
	/**
	 * Get xScript instance.
	 * @param doc
	 * @param env
	 * @return
	 */
	public static final Element getInstance(Document doc, Environment env) throws UnsupportedScriptException {
		NodeList children = doc.getChildNodes();
		Node node = null;
		
		for(int i = 0; i< children.getLength(); i++) {
			Node n = children.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				node = n;
				break;
			}
		}
		
		return getInstance(node, env);
	}
	
	/**
	 * Get xScript instance.
	 * @param node
	 * @param env
	 * @return
	 */
	public static final Element getInstance(Node node, Environment env) throws UnsupportedScriptException {
		
		if(node.getNodeType() != Node.ELEMENT_NODE) return null;
		
		if(env == null) env = new Environment();
		
		Element element = getElement(node, env);
		
		if(!(element instanceof Script)) throw new UnsupportedScriptException(element, "The root element should be <script>. [" + element.getClass().getCanonicalName() + "]");
		
		return element;
	}
	
	/**
	 * Get xScript instance.
	 * @param file
	 * @param env
	 * @return
	 * @throws UnsupportedScriptException
	 */
	public static final Element getInstance(File file, Environment env) throws UnsupportedScriptException {
		
		if(file == null || !file.exists()) return null;
		
		if(env == null) env = new Environment();
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new FileInputStream(file));
			String home = file.getCanonicalFile().getParent();
			if(env != null) {
				if(!env.containsVariable(Constant.VARIABLE_HOME)) env.setPublicVariable(Constant.VARIABLE_HOME, home, true);
			}
			return getInstance(doc, env);
		} catch(UnsupportedScriptException e) {
			throw e;
		} catch(Exception e) {
			throw new UnsupportedScriptException(null, e);
		}
	}
	
	/**
	 * Get xScript instance.
	 * @param url
	 * @param env
	 * @return
	 * @throws UnsupportedScriptException
	 */
	public static final Element getInstance(URL url, Environment env) throws UnsupportedScriptException {
		
		if(url == null) return null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setIgnoringElementContentWhitespace(true);
			
			InputStream in = url.openStream();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			return getInstance(doc, env);
		} catch(UnsupportedScriptException e) {
			throw e;
		} catch(Exception e) {
			throw new UnsupportedScriptException(null, e);
		}
	}
	
	
	/**
	 * Use <code>Node</code> to initial an <code>Element</code>.
	 * @param node
	 * @param env
	 * @return
	 */
	private static final Element getElement(Node node, Environment env) throws UnsupportedScriptException {
		if(env == null) return null;
		
		String ns = node.getNamespaceURI(); // name space
		String name = node.getLocalName();  // name
		//String prefix = node.getPrefix(); // prefix
		
		Element element = env.getElement(ns, name);
		if(element == null) {
			throw new UnsupportedScriptException(null, "Could't find the element.[" + (StringUtils.isBlank(ns)? name : ns + ":" + name) + "]");
		}
		element.setEnvironment(env);
		
		NamedNodeMap attributes = node.getAttributes();
		if(attributes != null) {
			for(int i=0; i<attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				String attrName = attr.getNodeName();
				String attrValue = attr.getNodeValue();
				
				if(attrName.equals("xmlns") || attrName.startsWith("xmlns:")) {
					if(attrName.startsWith("xmlns:")) {
						// to add function package
						String abbr = StringUtils.substringAfter(attrName, "xmlns:");
						if(StringUtils.startsWithAny(abbr, "f.", "func.", "function.")) {
							abbr = StringUtils.substringAfter(abbr, ".");
							env.setFunctionPackage(abbr, attrValue);
						}
					}
				} else {
					element.setAttribute(attrName, attrValue);
				}
			}
		}
		
		if(includeTextNode(node)) {
			String text = node.getTextContent();
			element.setText(text);
		}
		
		NodeList nodes = node.getChildNodes();
		if(nodes != null) {
			for(int i=0; i< nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if(n.getNodeType() == Node.ELEMENT_NODE) {
					Element child = getElement(n, env);
					element.addChild(child);
				}
			}
		}
		
		return element;
	}
	
	/**
	 * Check if <code>Node</code> has text.
	 * @param node
	 * @return
	 */
	private static boolean includeTextNode(Node node) {
		short type = node.getNodeType();
		switch(type) {
		
		case Node.ELEMENT_NODE:
			NodeList nodes = node.getChildNodes();
			if(nodes == null) return false;
			
			for (int i = 0; i < nodes.getLength(); i++)  {
				Node child = nodes.item(i);
				if(child.getNodeType() == Node.TEXT_NODE) {
					String text = child.getNodeValue();
					if(StringUtils.isNotBlank(text)) return true;
				} else if(child.getNodeType() == Node.CDATA_SECTION_NODE) {
					return true;
				}
			}
			return false;
		case Node.TEXT_NODE:
			return true;
		default:
			return false;
		} 
	}
	
	
	/**
	 * Debug element, use standard system output.
	 * @param element
	 */
	public static final void debug(Element element) {
		debug_(element, 0, System.out);
	}
	
	/**
	 * Debug element, specify output stream.
	 * @param element
	 * @param stream
	 */
	public static final void debug(Element element, PrintStream stream) {
		debug_(element, 0, stream);
	}
	
	/**
	 * Debug method for <code>Element</code>
	 * @param element
	 * @param level
	 * @param stream
	 */
	private static final void debug_(Element element, int level, PrintStream stream) {
		String repeatStr = "    ";
		
		String prefix = StringUtils.repeat(repeatStr, level);
		stream.print(prefix + "<" + element.getClass().getCanonicalName());
		
		// Attribute output
		Map<String, String> attributes = element.getAttributes();
		if(attributes != null && attributes.size() > 0) {
			Set<String> keys = attributes.keySet();
			for(String key: keys) {
				stream.print(" " + key + "=\"" + attributes.get(key) + "\"");
			}
		}
		
		Vector<Element> children = element.getChildren();
		String text = element.getText();
		if((children == null || children.size() == 0) && StringUtils.isBlank(text) ) {
			stream.println("/>");
			return;
		} else {
			stream.println(">");
			
			// child elements
			if(children != null && children.size() > 0) {
				for(Element child: children) {
					debug_(child, level + 1, stream);
				}
			}
			
			// text/context
			if(StringUtils.isNotBlank(text)) {
				stream.println(prefix + repeatStr + "<![CDATA[" + text + "]]>");
			}
			
			
			stream.println(prefix + "</" + element.getClass().getCanonicalName() + ">" );
		}
	}
	
	
	// main method
	public static void main(String[] args) {
		String file = null;
		
		for(String arg: args) {
			if(StringUtils.startsWithAny(StringUtils.lowerCase(arg), "-f:", "-file:")) {
				file = StringUtils.substringAfter(arg, ":");
			}
		}
		
		Element e = null;
		try {
			if(StringUtils.isBlank(file)) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("xScript");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						} else if (f.isFile() && StringUtils.endsWithIgnoreCase(f.getName(), ".xml")) {
							return true;
						}
						return false;
					}
					public String getDescription() {
						return "xScript (*.xml)";
					}
				});
				
				int returnVal = chooser.showOpenDialog(null);
				chooser.requestFocus();
				
				if(returnVal == JFileChooser.CANCEL_OPTION) return;
				
				File f = chooser.getSelectedFile();
				
				e = getInstance(f, null);
			} else {
				e = getInstance(new File(file), null);
			}
			//debug(e);
			//System.out.println("----------------------");
			
			int result = e.invoke();
			if(result == Element.EXCEPTION) {
				Object obj = e.getEnvironment().getVariable(Constant.VARIABLE_EXCEPTION);
				if(obj != null && obj instanceof Throwable) {
					System.err.println();
					((Throwable)obj).printStackTrace();
					
				} else {
					System.err.println();
					System.err.println("Unknown EXCEPTION is thrown.");
				}
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			
			System.err.println();
			
			if(ex instanceof UnsupportedScriptException) {
				UnsupportedScriptException ex_ = (UnsupportedScriptException)ex;
				if(ex_.getElement() != null) {
					debug(ex_.getElement(), System.err);
				}
			}
		} finally {
			if(e != null) e.getEnvironment().callback();
		}
	}
	
}
