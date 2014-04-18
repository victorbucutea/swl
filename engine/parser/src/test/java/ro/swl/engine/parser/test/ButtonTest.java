package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTButton;
import ro.swl.engine.parser.ASTButtonAction;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class ButtonTest extends WriterTest {

	@Test
	public void simpleButtonWithAction() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("button(saveAction())"));
		//@formatter:on

		swl.Button();
	}


	@Test
	public void simpleButtonWithActionParameter() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("button(saveAction(var1.subvar2.subvar3))"));
		//@formatter:on

		swl.Button();
	}


	@Test
	public void button() throws Exception {

		SWL swl = new SWL(createInputStream("button(action1(var1.childAttr1.childAttr2), \"cssClass;\")"));

		ASTButton button = swl.Button();

		ASTButtonAction bAction = button.getAction();
		List<String> cssClassNames = button.getCssClassNames();

		List<String> cssInlineStyles = button.getCssInlineStyles();

		assertEquals("action1(var1.childAttr1.childAttr2)", bAction.getImage());
		assertEquals("cssClass", cssClassNames.get(1));
		assertTrue(cssInlineStyles.isEmpty());
	}


	@Test
	public void buttonActionWithMultipleParams() throws Exception {

		SWL swl = new SWL(
				createInputStream("button(action1(var1.childAttr1.childAttr2, var2.childAttr1), \"cssClass;\")"));

		ASTButton button = swl.Button();

		ASTButtonAction bAction = button.getAction();
		List<String> cssClassNames = button.getCssClassNames();

		List<String> cssInlineStyles = button.getCssInlineStyles();

		assertEquals("action1(var1.childAttr1.childAttr2, var2.childAttr1)", bAction.getImage());
		assertEquals("cssClass", cssClassNames.get(1));
		assertTrue(cssInlineStyles.isEmpty());

	}


}
