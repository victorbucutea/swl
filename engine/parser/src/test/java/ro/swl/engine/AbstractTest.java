package ro.swl.engine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public abstract class AbstractTest {

	protected InputStream createInputStream(String string) {
		InputStream stream = null;
		try {
			stream = new ByteArrayInputStream(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return stream;
	}

}
