package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTButton;
import ro.swl.engine.parser.ASTButtonAction;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;

public class ButtonTest extends SwlTest {

	@Test
	public void button() throws UnsupportedEncodingException, ParseException, WriteException {

		SWL swl = new SWL(createInputStream("button(action1(var1), \"cssClass;\")"));

		ASTButton button = swl.Button();
		button.dump("");
		ASTButtonAction bAction = button.getChildNodesOfType(ASTButtonAction.class, true).get(0);
		List<String> cssClassNames = button.getCssClassNames();

		List<String> cssInlineStyles = button.getCssInlineStyles();

		assertEquals("action1(vaar1)", bAction.getImage());
		assertEquals("cssClass", cssClassNames.get(0));
		assertTrue(cssInlineStyles.isEmpty());
	}
}
