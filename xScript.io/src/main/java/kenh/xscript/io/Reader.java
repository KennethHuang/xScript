package kenh.xscript.io;

import java.util.Map;

/**
 * Implement this interface to read the object.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public interface Reader {
	
	Object read(Map<String, Object> attributes) throws Exception;
	
}
