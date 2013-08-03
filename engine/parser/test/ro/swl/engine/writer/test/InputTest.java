package ro.swl.engine.writer.test;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTCheckbox;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ASTInputArea;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public class InputTest extends SwlTest {

	@Test
	public void inputlabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		input(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInput input = swl.Input();

		TagWriter writer = new TagWriter();
		input.render(writer);

		parse(writer.toString());

		Element element = getElement("label", 1);
		element.assertContainsText("input label TODO");
		element.assertIndentAfterClose(1);
		element.assertNewLineAfterClose();

		element = getElement("input", 2);
		element.assertContainsAttribute("class", "cssclass1");
		element.assertContainsAttribute("type", "text");
		element.assertContainsAttribute("style", "height: 200px; width: 30px");

	}

	@Test
	public void inputAreaLabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
					"		input_area(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInputArea input = swl.InputArea();

		TagWriter writer = new TagWriter();
		input.render(writer);
		parse(writer.toString());

		Element element = getElement("label", 1);
		element.assertContainsText("input label TODO");
		element.assertIndentAfterClose(1);
		element.assertNewLineAfterClose();

		element = getElement("input", 2);
		element.assertContainsAttribute("type", "area");
		element.assertContainsAttribute("class", "cssclass1");
		element.assertContainsAttribute("style", "height: 200px; width: 30px");

	}

	@Test
	public void checkboxlabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		checkbox(modelVar,' cssclass1; height: 200px; width:30px;')"));
		//@formatter:on

		ASTCheckbox input = swl.Checkbox();

		TagWriter writer = new TagWriter();
		input.render(writer);

		parse(writer.toString());

		Element el = getElement("label", 1);
		el.assertContainsAttribute("class", "checkbox");
		el.assertContainsText("\n\t\t"); // indentation of next tag

		Element el1 = getElement("input", 2);
		el1.assertContainsAttribute("style", "height: 200px; width: 30px");
		el1.assertContainsAttribute("class", "cssclass1");
		el1.assertTrailingText("\n\tinput label TODO\n\t");

		// label is parent for input
		el1.assertHasParent(el);
	}

}
