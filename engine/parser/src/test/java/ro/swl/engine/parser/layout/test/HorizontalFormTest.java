package ro.swl.engine.parser.layout.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTHorizontalForm;
import ro.swl.engine.parser.ASTLayoutRow;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.WriteException;


public class HorizontalFormTest extends WriterTest {

	@Test
	public void horizontalFormDescriptionTest() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {"+
                    "row () { text(\"Course Materials\",\"font-size:90%;\")"+
                    		 "radio(materials,\"1\") }"+
                    "row ('some-css-style;') {radio(materials,\"1\")"+
                    		"radio(materials,\"1\")}"+ 
                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		List<String> cssClassNames = layout.getCssClassNames();
		assertEquals(2, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));
		assertEquals("form-horizontal", cssClassNames.get(1));

		List<String> cssInlineStyles = layout.getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));

		List<ASTLayoutRow> rows = layout.getRows();

		assertEquals(2, rows.size());
		ASTLayoutRow row1 = rows.get(1);
		assertEquals("some-css-style", row1.getCssClassNames().get(1));

		List<Component> childComponents = row1.getChildComponents();

		assertEquals("materials", childComponents.get(0).getModelValueBinding().getImage());
		assertEquals("materials", childComponents.get(1).getModelValueBinding().getImage());
	}
}
