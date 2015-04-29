package junit.kenh.xscript.functions;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import kenh.expl.UnsupportedExpressionException;
import kenh.expl.impl.BaseFunction;

public class Zero extends BaseFunction {
	
	@Override
	public String invoke(Object... params) throws UnsupportedExpressionException {
		return "0";
	}
}
