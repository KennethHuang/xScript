package kenh.xscript.io.elements;

import org.apache.commons.lang3.StringUtils;

import kenh.expl.UnsupportedExpressionException;
import kenh.xscript.annotation.Text;
import kenh.xscript.impl.NoChildElement;
import kenh.xscript.*;
import kenh.xscript.io.*;

import java.util.*;

/**
 * Class for tag read
 * @author Kenneth
 *
 */
@Text(Text.Type.FULL)
public class Read extends NoChildElement {

	private static final String ATTRIBUTE_REF = "ref";
	private static final String ATTRIBUTE_VARIABLE = "var";
	
	private Map<String, String> attributes = new LinkedHashMap();
	
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
		
		// Reader
		
		Reader reader = null;
		
		String ref = StringUtils.trimToNull(this.getAttribute(ATTRIBUTE_REF));
		if(StringUtils.isNotBlank(ref)) {
			try {
				Object obj = this.getEnvironment().parse(ref);
				
				if(obj instanceof Reader) reader = (Reader)obj;
				else if(obj instanceof String) {
					obj = this.getEnvironment().getVariable((String)obj);
					if(obj instanceof Reader) reader = (Reader)obj;
				}
				
			} catch(UnsupportedExpressionException e) {
				throw new UnsupportedScriptException(this, e);
			}
		}
		
		if(reader == null) throw new UnsupportedScriptException(this, "Reference is empty.");
		
		
		// variable
		
		String var = getAttribute(ATTRIBUTE_VARIABLE);
		if(StringUtils.isNotBlank(var)) {
			try {
				var = StringUtils.trimToNull((String)this.getEnvironment().parse(var));
			} catch(Exception e) {
				throw new UnsupportedScriptException(this, e);
			}
		}
		if(StringUtils.isBlank(var)) throw new UnsupportedScriptException(this, "Variable is empty.");
		
		
		// Attributes
		
		Map<String, Object> src = new LinkedHashMap();
		Set<String> keys = attributes.keySet();
		for(String key: keys) {
			if(key.equals(ATTRIBUTE_REF) ||
			   key.equals(ATTRIBUTE_VARIABLE) ) {
				continue;
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
		
		
		// to reader
		try {
			Object obj = reader.read(src);
			this.saveVariable(var, obj, null);
			
		} catch(Exception e) {
			if(e instanceof UnsupportedScriptException) throw (UnsupportedScriptException)e;
			else throw new UnsupportedScriptException(this, e);
		}
		
		
		return NONE;
	}
}
