package ro.swl.engine;

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;


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


	protected void assertEqualsIgnoreWhitespace(String expected, String actual) {
		if (actual != null) {
			actual = StringUtils.deleteWhitespace(actual);
		}
		assertEquals(expected, actual);
	}
}
