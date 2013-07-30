package ro.swl.engine.writer.test;

import static junit.framework.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTCheckbox;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;
import ro.swl.engine.writer.Writer;

public class InputTest extends SwlTest {

	@Test
	public void inputlabelRender() throws UnsupportedEncodingException, ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		input(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInput input = swl.Input();

		Writer writer = new Writer();
		input.renderComponent(writer);

		assertEquals("<label >input TODO</label><input type=\"text\""
				+ " class=\"cssclass1\"  style=\"height: 200px; width: 30px\" ></input>", writer.toString());
	}

	@Test
	public void checkboxlabelRender() throws UnsupportedEncodingException, ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		checkbox(modelVar,' cssclass1; height: 200px; width:30px;')"));
		//@formatter:on

		ASTCheckbox input = swl.Checkbox();

		Writer writer = new Writer();
		input.renderComponent(writer);

		assertEquals("<label  class=\"checkbox\" ><input type=\"checkbox\"  "
				+ "class=\"cssclass1\"  style=\"height: 200px; width: 30px\" ></input>checkbox label TODO</label>",
				writer.toString());
	}

}
