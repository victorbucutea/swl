package ro.swl.engine.parser.layout.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTLayoutRow;
import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class VerticalLayoutTest extends WriterTest {

	@Test
	public void simpleVerticalLayout() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout() {}"));
		//@formatter:on

		swl.VerticalLayout();
	}


	@Test
	public void verticalLayoutModelController() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout () {"+
													"model {} "+
													"controller {} " +
													"row() {	" +
													"		img(cv.rawFile,\"width:300px; height:150px;\") "+
													"		input_file(cv.rawFile) " +
													"		img(\"/img/static.png\")" +
													"}" +
												"}"
								));
		//@formatter:on
		swl.VerticalLayout();
	}


	@Test
	public void verticalLayoutDescriptionTest() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
                    "row('first-row-css; style:30px;') {text(\"Course Materials\",\"font-size:90%;\")}"+
                    "row() {radio(materials,\"1\")}"+
                    "row() {radio(materials,\"1\")}"+
                    "row() {radio(materials,\"1\")} "+ 
                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		List<String> cssClassNames = verticalLayout.getCssClassNames();
		assertEquals(1, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));

		List<String> cssInlineStyles = verticalLayout.getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));

		List<ASTLayoutRow> rows = verticalLayout.getRows();

		assertEquals(4, rows.size());

		ASTLayoutRow row1 = rows.get(0);
		assertEquals("first-row-css", row1.getCssClassNames().get(0));
		assertEquals("style: 30px", row1.getCssInlineStyles().get(0));

		assertTrue(rows.get(1).getCssClassNames().isEmpty());
		assertTrue(rows.get(2).getCssClassNames().isEmpty());
		assertTrue(rows.get(3).getCssClassNames().isEmpty());


	}


	@Test
	public void verticalLayoutContent() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
		                    "row() {text(\"Course Materials\",\"font-size:90%;\")"+
		                    	   "radio(materials,\"1\") " +
		                    	   "}"+
		                    "row() { radio(materials,\"1\")"+
			                    "radio(materials,\"1\") " +
			                    "vertical_layout() {} " +
			                    "vertical_layout('css-class;'){ " +
			                    								"row () { horizontal_layout(\"some-css;\") { " +
			                    																"column (3) { input() } " +
			                    																"}  " +
			                    										"} " +
			                    							    "}"+ 
			                    "}" +
		                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		ASTVerticalLayout childVertical1 = verticalLayout.getFirstChildNodeOfType(ASTVerticalLayout.class, true);

		assertTrue(childVertical1.getCssClassNames().isEmpty());
		assertTrue(childVertical1.getCssInlineStyles().isEmpty());

		List<ASTVerticalLayout> childLayouts = verticalLayout.getChildNodesOfType(ASTVerticalLayout.class, true);

		assertEquals(2, childLayouts.size());
		ASTVerticalLayout layout2 = childLayouts.get(1);

		assertEquals("css-class", layout2.getCssClassNames().get(0));

	}

}
