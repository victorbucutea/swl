package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTCssClassName;
import ro.swl.engine.parser.ASTCssInlineStyle;
import ro.swl.engine.parser.ASTHorizontalLayout;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ASTLayoutColumn;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.parser.model.Component;


public class SWLNodeTest extends WriterTest {

	@Test
	public void inputCssChildren() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		input(modelVar,' cssclass1; height: 200px;')"));
		//@formatter:on

		ASTInput input = swl.Input();

		List<ASTCssInlineStyle> styles = input.getChildNodesOfType(ASTCssInlineStyle.class, true);
		List<ASTCssClassName> clsNames = input.getChildNodesOfType(ASTCssClassName.class, true);

		assertEquals("cssclass1", clsNames.get(0).getImage());
		assertEquals("height: 200px", styles.get(0).getImage());

	}


	@Test
	public void hasChildren() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream("" +
						"		input(modelVar,' cssclass1; height: 200px;')"));
		//@formatter:on

		assertFalse(swl.Input().hasChildComponents());
	}


	@Test
	public void componentChildren() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream("" +
						"		horizontal_layout() {" +
							"		column(1) { input(modelVar,' cssclass1; height: 200px;')}" +
							"		column(2) {button(action1(param1))}" +
						"		}"));
		//@formatter:on

		ASTHorizontalLayout horizontalLayout = swl.HorizontalLayout();
		List<Component> components = horizontalLayout.getChildComponents();

		assertEquals(2, components.size());

		assertTrue(components.get(0) instanceof ASTLayoutColumn);
		assertTrue(components.get(1) instanceof ASTLayoutColumn);

		List<Component> childInput = components.get(0).getChildComponents();

		assertEquals(1, childInput.size());
		assertTrue(childInput.get(0) instanceof ASTInput);

	}
}
