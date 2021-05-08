package kenh.xscript.io;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Utils {
	
	/**
	 * Check all attribute names that are all in the list
	 * @param atts   the map
	 * @param allowZero   allow the map with empty element
	 * @param names  all allowed attribute names
	 * @return
	 */
	public static boolean checkAttributes(Map<String, Object> atts, boolean allowZero, String ... names) {
		
		if(atts == null || atts.size() == 0) {
			if(allowZero) return true;
			else return false;
		}
		
		int i=0;
		for(String name: names) {
			if(atts.containsKey(name)) i++;
		}
		
		if(atts.size() == i) return true;
		
		return false;
	}
	
	/**
	 * Check the value that are all in the list
	 * @param value
	 * @param values
	 * @return
	 */
	public static boolean match(String value, String ... values) {
		
		for(String s: values) {
			if(StringUtils.equals(value, s)) return true;
		}
		
		return false;
	}
	

}
