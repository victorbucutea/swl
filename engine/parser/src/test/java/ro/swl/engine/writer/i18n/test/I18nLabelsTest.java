package ro.swl.engine.writer.i18n.test;

import org.junit.Test;

import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;
import ro.swl.engine.writer.ui.test.IdGeneratorTest;


public class I18nLabelsTest extends IdGeneratorTest {

	@Test
	public void labelsInVerticalLayout() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
				                    "row() { text(\"Course Materials\",\"font-size:90%;\")}"+
				                    "row() { radio(materials,\"value1\",\"css1;\")}"+
				                    "row() { radio(materials,\"value2\",\"css2;\")}"+
				                    "row() { checkbox(materials,\"css3;\") } "+ 
				                    "row() { input(modelValue,\"css3;\") }" +
				                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		verticalLayout.render(writer);


		// expect writer has 10 keys generated 

	}
}
