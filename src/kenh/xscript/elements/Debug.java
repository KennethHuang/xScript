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

package kenh.xscript.elements;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;

import kenh.xscript.annotation.Attribute;
import kenh.xscript.annotation.Reparse;
import kenh.xscript.impl.NoChildElement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Debug interface.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public class Debug extends NoChildElement implements ListSelectionListener, ActionListener, KeyListener {
	
	private static final String ATTRIBUTE_CONDITION = "cond";
	
	private JTextArea result = null;
	private static final String LINE_SEP = System.getProperty("line.separator");
	
	public void process(@Reparse@Attribute(ATTRIBUTE_CONDITION) boolean cond) {
		if(cond) process();
	}
	
	/**
	 * a dialog use to debug.
	 */
	public void process() {	
		
		JDialog dialog = new JDialog((Frame)null, true);
		dialog.setTitle("Debugger");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(750, 200);
		initial(dialog.getContentPane());
		dialog.setVisible(true);
		
		this.result = null;
		
	}
	
	private void initial(Container c) {
		c.setLayout(new BorderLayout());
		
		// Add variable list
		
		DefaultListModel<String> model = new DefaultListModel();
		
		if(this.getEnvironment() != null) {
			java.util.Set<String> keys = this.getEnvironment().getVariables().keySet();
			for(String key: keys) {
				model.addElement(key);
			}
		} else {
			for(int i=1; i<10; i++) {
				model.addElement("Variable " + i);
			}
			model.addElement("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}
		
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listPane = new JScrollPane();
		listPane.setViewportView(list);
		listPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		c.add(listPane, BorderLayout.EAST);
		
		list.setPreferredSize(new Dimension(150, list.getPreferredSize().height));
		
		// 
		
		JTextField quote = new JTextField();
		quote.requestFocus();
		
		//JButton button = new JButton(">>");
		
		JPanel quotePanel = new JPanel();
		quotePanel.setLayout(new BorderLayout());
		quotePanel.add(quote, BorderLayout.CENTER);
		//quotePanel.add(button, BorderLayout.EAST);
		
		JTextArea result = new JTextArea();
		result.setEditable(false);
		
		JScrollPane resultPane = new JScrollPane();
		resultPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultPane.setViewportView(result);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(quotePanel, BorderLayout.NORTH);
		panel.add(resultPane, BorderLayout.CENTER);
		
		c.add(panel, BorderLayout.CENTER);
		
		list.addListSelectionListener(this);
		//button.addActionListener(this);
		quote.addKeyListener(this);
		
		this.result = result;
	}
	
	/*
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		new Debug().process();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) return;
		
		Object obj = e.getSource();
		if(obj instanceof JList) {
			list((JList)obj);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { 
		
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		 if(e.getKeyChar()==KeyEvent.VK_ENTER) {
			 Object obj = e.getSource();
			 if(obj instanceof JTextField) {
				 parse((JTextField)obj);
			 }
		 }
	}

	@Override
	public void keyReleased(KeyEvent e) { }
	
	private void parse(JTextField c) {
		if(result == null) return;
		
		if(StringUtils.isBlank(c.getText())) return;
		
		if(this.getEnvironment() != null) {
			String context = "";
			try {
				Object obj = this.getEnvironment().parse(c.getText());
				context = (obj != null ? obj.toString() + LINE_SEP + LINE_SEP + "-- Class: " + obj.getClass().getCanonicalName() : "<null>" );
				
			} catch(Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				context = sw.toString();
			}
			
			result.setText(context);
			
		} else {
			result.setText(c.getText());
		}
		
		result.setCaretPosition(0);
		c.requestFocus();
	}
	
	private void list(JList c) {
		if(result == null) return;
		
		if(StringUtils.isBlank(c.getSelectedValue().toString())) return;
		
		if(this.getEnvironment() != null) {
			
			String context = "";
			try {
				Object obj = this.getEnvironment().getVariable(c.getSelectedValue().toString());
				if(obj != null) {
					
					context = c.getSelectedValue().toString() + LINE_SEP + LINE_SEP;
					
					context += "-- Class: " + obj.getClass().getCanonicalName() + LINE_SEP;
					
					context += LINE_SEP;
					context += "-- Fields: " + LINE_SEP;
					Field[] fields = obj.getClass().getFields();
					
					for(Field field: fields) {
						int i = field.getModifiers();
						String retval = Modifier.toString(i);
						if(StringUtils.contains(retval, "public")) {
							context += "\t" + field.getName() + " - " + retval + LINE_SEP;
						}
					}
					
					context += LINE_SEP;
					context += "-- Method: " + LINE_SEP;
					java.lang.reflect.Method[] methods = obj.getClass().getMethods();
					
					for(java.lang.reflect.Method method: methods) {
						int i = method.getModifiers();
						String retval = Modifier.toString(i);
						if(StringUtils.contains(retval, "public")) {
							Class[] pcs = method.getParameterTypes();
							StringBuffer sb = new StringBuffer();
							
							for(Class c_: pcs) {
								String s = c_.getSimpleName();
								sb.append(s + ", ");
							}
							
							String p = StringUtils.trimToEmpty(StringUtils.substringBeforeLast(sb.toString(), ","));
							
							context += "\t" + method.getName() + "(" + p + ") - " + retval + LINE_SEP;
						}
					}
					
				} else {
					context = "<null>";
				}
			} catch(Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				context = sw.toString();
			}
			
			result.setText(context);
			
		} else {
			result.setText(c.getSelectedValue().toString());
		}
		
		result.setCaretPosition(0);
		c.requestFocus();
	}
	
}
