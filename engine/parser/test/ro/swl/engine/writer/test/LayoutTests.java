package ro.swl.engine.writer.test;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTHorizontalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;
import ro.swl.engine.writer.Writer;

public class LayoutTests extends SwlTest {

	@Test(expected = WriteException.class)
	public void simpleHorizontalLayoutTooManyColumnsDeclared() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(" +
						"[\"container-css;container-css2;top:23px;\"]4[\"row-css;\"],1,1," +
						"1['background-color: blue;css-style-class;'],1,1) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\") "+ 
                            "}"));
		//@formatter:on

		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
		Writer writer = new Writer();
		horizontalLayout.renderComponent(writer);
	}

	@Test
	public void simpleHorizontalLayoutWithCss() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(" +
						"[\"container-css;container-css2;top:23px;\"]4[\"row-css;\"],1,1," +
						"1['background-color: blue;css-style-class;'],1,1) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\") " +
                            "input(materials1)"+ 
                            "input(materials2)"+ 
                            "}"));
		//@formatter:on

		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
		Writer writer = new Writer();
		horizontalLayout.renderComponent(writer);
		System.err.println(writer);
		assertEquals("", writer.toString());
	}
}
