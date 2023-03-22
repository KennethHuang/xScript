package kenh.xscript.io;

import java.util.Map;

/**
 * implement this interface to write object.
 * 
 * @author Kenneth
 * @since 1.0
 *
 */
public interface Writer {
	
	void write(Object obj, Map<String, Object> attributes) throws Exception;
	
}
