package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTColumnDescription;
import ro.swl.engine.parser.ASTHorizontalForm;
import ro.swl.engine.parser.ASTHorizontalFormGroup;
import ro.swl.engine.parser.ASTHorizontalLayout;
import ro.swl.engine.parser.ASTSection;
import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;

public class LayoutTest extends SwlTest {

	@Test
	public void verticalLayoutDescriptionTest() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
                    "label(\"Course Materials\",\"font-size:90%;\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\") "+ 
                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		verticalLayout.dump("");
		List<String> cssClassNames = verticalLayout.getDescription().getCssClassNames();
		assertEquals(1, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));

		List<String> cssInlineStyles = verticalLayout.getDescription().getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));
	}

	@Test
	public void verticalLayoutContent() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
		                    "label(\"Course Materials\",\"font-size:90%;\")"+
		                    "radio(materials,\"1\")"+
		                    "radio(materials,\"1\")"+
		                    "radio(materials,\"1\") " +
		                    "vertical_layout() {} " +
		                    "vertical_layout('css-class;'){ horizontal_layout(1,2) { input() } }"+ 
		                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		ASTVerticalLayout childVertical1 = verticalLayout.getFirstChildNodeOfType(ASTVerticalLayout.class, true);

		assertTrue(childVertical1.getDescription().getCssClassNames().isEmpty());
		assertTrue(childVertical1.getDescription().getCssInlineStyles().isEmpty());

		List<ASTVerticalLayout> childLayouts = verticalLayout.getChildNodesOfType(ASTVerticalLayout.class, true);

		assertEquals(2, childLayouts.size());
		ASTVerticalLayout layout2 = childLayouts.get(1);

		assertEquals("css-class", layout2.getDescription().getCssClassNames().get(0));

	}

	@Test
	public void horizontalFormDescriptionTest() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {"+
                    "label(\"Course Materials\",\"font-size:90%;\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\") "+ 
                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		List<String> cssClassNames = layout.getDescription().getCssClassNames();
		assertEquals(1, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));

		List<String> cssInlineStyles = layout.getDescription().getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));
	}

	@Test
	public void horizontalFormGroupDescriptionTest() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form_group(\"vertical-css-class; some-css: someValue;\") {"+
                    "label(\"Course Materials\",\"font-size:90%;\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\")"+
                    "radio(materials,\"1\") "+ 
                    "}"));
		//@formatter:on

		ASTHorizontalFormGroup verticalLayout = swl.HorizontalFormGroup();

		List<String> cssClassNames = verticalLayout.getDescription().getCssClassNames();
		assertEquals(1, cssClassNames.size());
		assertEquals("vertical-css-class", cssClassNames.get(0));

		List<String> cssInlineStyles = verticalLayout.getDescription().getCssInlineStyles();
		assertEquals(1, cssInlineStyles.size());
		assertEquals("some-css: someValue", cssInlineStyles.get(0));
	}

	@Test
	public void sectionDescriptionTest() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" section(variable,\"vertical-css-class; some-css: someValue;\") {"+
                    "label(\"Course Materials\",\"font-size:90%;\")"+
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
	}

	@Test
	public void simpleHorizontalLayoutWithCss() throws ParseException {
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

		List<String> cssClassNames = horizontalLayout.getCssClassNames();
		assertEquals(3, cssClassNames.size());
		assertEquals("container-css", cssClassNames.get(0));
		assertEquals("container-css2", cssClassNames.get(1));
		assertEquals("row-fluid", cssClassNames.get(2)); //row fluid is mandatory

		List<String> cssinlines = horizontalLayout.getCssInlineStyles();
		assertEquals(1, cssinlines.size());
		assertEquals("top: 23px", cssinlines.get(0));

		List<String> firstColumnClasses = horizontalLayout.getDescription().getCssClassNames(0);
		assertEquals(1, firstColumnClasses.size());
		assertEquals("row-css", firstColumnClasses.get(0));

		List<String> thirdColumnClasses = horizontalLayout.getDescription().getCssClassNames(3);
		assertEquals(1, thirdColumnClasses.size());
		assertEquals("css-style-class", thirdColumnClasses.get(0));

		List<String> thirdColumnStyles = horizontalLayout.getDescription().getCssInlineStyles(3);
		assertEquals(1, thirdColumnStyles.size());
		assertEquals("background-color: blue", thirdColumnStyles.get(0));

		List<String> forthColumnStyles = horizontalLayout.getDescription().getCssInlineStyles(4);
		assertTrue(forthColumnStyles.isEmpty());
		List<String> forthColumnClss = horizontalLayout.getDescription().getCssClassNames(4);
		assertTrue(forthColumnClss.isEmpty());

		List<ASTColumnDescription> columns = horizontalLayout.getColumns();

		assertEquals(6, columns.size());

		assertEquals("4", columns.get(0).getImage());
		assertEquals("1", columns.get(1).getImage());
		assertEquals("1", columns.get(2).getImage());
		assertEquals("1", columns.get(3).getImage());
		assertEquals("1", columns.get(4).getImage());
		assertEquals("1", columns.get(5).getImage());

	}
}
