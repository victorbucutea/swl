package ro.swl.engine.parser.test;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;

public class ModuleTests extends SwlTest {

	@Test
	public void simple() throws UnsupportedEncodingException, ParseException {

		InputStream stream = createInputStream(" name 'x' \n  module CV { }");

		SWL swl = new SWL(stream);

		swl.SwdlFile();

	}

	@Test
	public void uiDomain() throws UnsupportedEncodingException, ParseException {

		InputStream stream = createInputStream(" name 'x' \n  module CV { ui{} domain{} }");
		SWL swl = new SWL(stream);
		swl.SwdlFile();
	}

	@Test
	public void logic() throws UnsupportedEncodingException, ParseException {

		SWL swl = new SWL(createInputStream(" name 'x' \n  module CV { logic{} }"));

		swl.SwdlFile();

	}

	@Test
	public void multiple() throws UnsupportedEncodingException, ParseException {

		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		InputStream stream = createInputStream(string);

		SWL swl = new SWL(stream);

		swl.SwdlFile();
	}

	@Test(expected = ParseException.class)
	public void noModule() throws UnsupportedEncodingException, ParseException {
		SWL swl = new SWL(createInputStream(" name \"x\" \n\t\n "));
		swl.SwdlFile();
	}

	@Test(expected = ParseException.class)
	public void noName() throws UnsupportedEncodingException, ParseException {
		SWL swl = new SWL(createInputStream(" name  \n\t\n module CV{ }"));
		swl.SwdlFile();
	}

	@Test
	public void keywordName() throws UnsupportedEncodingException, ParseException {
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n module CV{ }"));
		swl.SwdlFile();
	}

	@Test
	public void comments() throws UnsupportedEncodingException, ParseException {
		SWL swl = new SWL(createInputStream(" name /* name of module */ 'module'"
				+ " \n // one line comment \t\n module CV{ }"));
		swl.SwdlFile();
	}

}
