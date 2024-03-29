package kenh.xscript.io.elements;

import kenh.xscript.annotation.Attribute;
import org.apache.commons.lang3.StringUtils;

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.annotation.Text;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;
import kenh.xscript.io.*;

import java.util.*;

/**
 * Class for tag write.
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Write extends NoChildElement {

	private static final String ATTRIBUTE_REF = "ref";
	private static final String ATTRIBUTE_VALUE = "value";

	private Map<String, String> attributes = new LinkedHashMap();
	
	protected boolean new_line = false;
	
	@Override
	public void setAttribute(String name, String value) throws UnsupportedScriptException {
		attributes.put(name, value);
	}
	
	@Override
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	@Override
	public int invoke() throws UnsupportedScriptException {
		
		this.getLogger().debug("[XSCRIPT] " + getInfo());
		
		// Writer
		
		Writer writer = null;
		
		Object ref = StringUtils.trimToNull(this.getAttribute(ATTRIBUTE_REF));
		if(ref == null) throw new UnsupportedScriptException(this, "Missing attribute. [" + ATTRIBUTE_REF + "]");
		
		
		if(ref instanceof java.lang.String) {
			String ref_ = (String)ref;
			if(StringUtils.isNotBlank(ref_)) {
				try {
					Object obj = this.getEnvironment().parse(ref_);
					
					if(obj instanceof Writer) writer = (Writer)obj;
					else if(obj instanceof String) {
						obj = this.getEnvironment().getVariable((String)obj);
						if(obj instanceof Writer) writer = (Writer)obj;
					}
					
				} catch(UnsupportedExpressionException e) {
					throw new UnsupportedScriptException(this, e);
				}
			}
		} else if(ref instanceof Writer) {
			writer = (Writer)ref;
		}
		
		if(writer == null) throw new UnsupportedScriptException(this, "Missing reference.");
		
		// Attributes
		Map<String, Object> src = new LinkedHashMap();
		Set<String> keys = attributes.keySet();
		Object value = null;
		for(String key: keys) {
			if(key.equals(ATTRIBUTE_REF)) {    // skip
				continue;
			} else if(key.equals(ATTRIBUTE_VALUE)) {
				try {
					value = this.getEnvironment().parse(this.getAttribute(ATTRIBUTE_VALUE));
				} catch(UnsupportedExpressionException e) {
					throw new UnsupportedScriptException(this, e);
				}
			} else {
				String str = this.getAttribute(key);
				try {
					Object obj = this.getEnvironment().parse(str);
					src.put(key, obj);
				} catch(UnsupportedExpressionException e) {
					throw new UnsupportedScriptException(this, e);
				}
			}
		}
		
		// to writer
		try {

			if(value == null) {
				if (getText() == null) writer.write("", src);
				else {
					value = this.getEnvironment().parse(getText());
					writer.write(value, src);
				}
			} else {
				writer.write(value, src);
			}
			
		} catch(Exception e) {
			if(e instanceof UnsupportedScriptException) throw (UnsupportedScriptException)e;
			else throw new UnsupportedScriptException(this, e);
		}
		
		return NONE;
	}
}
