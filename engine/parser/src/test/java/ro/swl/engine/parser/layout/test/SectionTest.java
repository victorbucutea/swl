package ro.swl.engine.parser.layout.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTSection;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;

public class SectionTest extends WriterTest {

	@Test
	public void sectionDescriptionTest() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" section(variable,\"vertical-css-class; some-css: someValue;\") {"+
                    "text(\"Course Materials\",\"font-size:90%;\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\") "+ 
                    "}"));
		//@formatter:on

		ASTSection sectinLayout = swl.Section();

		List<String> cssClassNames = sectinLayout.getDescription().getCssClassNames();
		assertEquals(1, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));

		List<String> cssInlineStyles = sectinLayout.getDescription().getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));

		String modelVar = sectinLayout.getModelValueBinding().getImage();
		assertEquals("variable", modelVar);
	}
}
