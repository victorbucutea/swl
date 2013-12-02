package ro.swl.engine.parser.layout.test;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTHorizontalLayout;
import ro.swl.engine.parser.ASTLayoutColumn;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;


public class HorizontalLayoutTests extends WriterTest {

	@Test
	public void simpleHorizontalLayout() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout() {}"));
				//@formatter:on	
		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
	}


	@Test
	public void simpleHorizontalLayout1() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout() {"+
											                            "column(1) {}" +
											                            "}"));
		//@formatter:on	
		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
	}


	@Test(expected = ParseException.class)
	public void simpleHorizontalLayoutTooManyColumnsDeclared() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout() {"+
                            "column(10) { text(\"Course Materials\",\"font-size:90%;\")"+
                            		     "radio(materials,\"1\")"+
                            		     "radio(materials,\"1\")"+
                            		     "radio(materials,\"1\") "+ 
                            		   "}" +
                            "column(3) {text(\"second col\")} " +
                            		   "}"));
		//@formatter:on
		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
		// expect max number of rows exceeded
	}


	@Test
	public void simpleHorizontalLayoutWithCss() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(\"block-css;\") {"+
                            "column(1,\"column1-css;\") {text(\"Course Materials\",\"font-size:90%;\")"+
                            						  "radio(materials,\"1\")" +
                            						  "}"+
                            "column(2,\"column2-css;\") { radio(materials,\"1\") }"+
                            "column(2) 				   { radio(materials,\"1\") }" +
                            "column(3) 				   { input(materials1) input(materials2)}"+ 
                            "}"));
		//@formatter:on

		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
		assertEquals("block-css", horizontalLayout.getCssClassNames().get(1));
		List<ASTLayoutColumn> childComponents = horizontalLayout.getColumns();
		assertEquals(4, childComponents.size());
		ASTLayoutColumn col1 = childComponents.get(0);
		assertEquals(1, col1.getColSpan());
		ASTLayoutColumn col2 = childComponents.get(1);
		assertEquals(2, col2.getColSpan());
		ASTLayoutColumn col3 = childComponents.get(2);
		assertEquals(2, col3.getColSpan());
		ASTLayoutColumn col4 = childComponents.get(3);
		assertEquals(3, col4.getColSpan());
	}
}
