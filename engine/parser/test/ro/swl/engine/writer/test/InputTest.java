package ro.swl.engine.writer.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTCheckbox;
import ro.swl.engine.parser.ASTHorizontalForm;
import ro.swl.engine.parser.ASTImg;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ASTInputArea;
import ro.swl.engine.parser.ASTInputFile;
import ro.swl.engine.parser.ASTRadio;
import ro.swl.engine.parser.ASTRadios;
import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.WriteException;

public class InputTest extends SwlTest {

	@Test
	public void inputlabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		input(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInput input = swl.Input();

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
	public void inputInHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {"+
						                    "label(\"Course Materials\",\"font-size:90%;\")"+
						                    "radio(materials,\"value1\",\"css1;\")"+
						                    "radio(materials,\"value2\",\"css2;\")"+
						                    "radio(materials,\"value3\",\"css3;\") "+ 
						                    "input(modelValue,\"css3;\") " +
						                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		System.out.println(writer);
	}

	@Test
	public void componentsAtMultipleLevelsInHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {"+
						                    "label(\"Course Materials\",\"font-size:90%;\")"+
						                    "radio(materials,\"value1\",\"css1;\")"+
						                    "radio(materials,\"value2\",\"css2;\")"+
						                    "radio(materials,\"value3\",\"css3;\") "+ 
						                    "vertical_layout () { label('lbl') , input(somevalue) } " +
						                    "input(modelValue,\"css3;\") " +
						                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		System.out.println(writer);
	}

	@Test
	public void inputInHorizontalFormGroup() throws ParseException, WriteException {

		fail();

	}

	@Test
	public void inputInHorizontalLayout() throws ParseException, WriteException {
		fail();
	}

	@Test
	public void inputInVerticalLayout() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout(\"vertical-css-class; some-css: someValue;\") {"+
				                    "label(\"Course Materials\",\"font-size:90%;\")"+
				                    "radio(materials,\"value1\",\"css1;\")"+
				                    "radio(materials,\"value2\",\"css2;\")"+
				                    "radio(materials,\"value3\",\"css3;\") "+ 
				                    "input(modelValue,\"css3;\") " +
				                    "}"));
		//@formatter:on

		ASTVerticalLayout verticalLayout = swl.VerticalLayout();

		verticalLayout.render(writer);

		parse(writer);

		System.out.println(writer);

		// 1st row
		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class");
		el.assertContainsAttribute("style", "some-css: someValue");

		el = getElement("span", 2);
		el.assertContainsText("input label TODO");

		// 2nd row
		el = getElement("div", 3);
		assertRadioProperlyRendered("css1", "value1", 4);

		//3rd row
		el = getElement("div", 5);
		assertRadioProperlyRendered("css2", "value2", 6);

		el = getElement("div", 7);
		assertRadioProperlyRendered("css3", "value3", 7);

	}

	@Test
	public void inputAreaLabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("input_area(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInputArea input = swl.InputArea();

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
	public void inputFileLabelRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
					"		input_file(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTInputFile input = swl.InputFile();

		input.render(writer);
		parse(writer.toString());

		Element element = getElement("label", 1);
		element.assertContainsText("input label TODO");
		element.assertIndentAfterClose(1);
		element.assertNewLineAfterClose();

		element = getElement("input", 2);
		element.assertContainsAttribute("type", "file");
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

		input.render(writer);

		parse(writer.toString());

		Element el = getElement("label", 1);
		el.assertContainsAttribute("class", "checkbox");
		el.assertContainsText("\n\t\t"); // indentation of next tag

		Element el1 = getElement("input", 2);
		el1.assertContainsAttribute("style", "height: 200px; width: 30px");
		el1.assertContainsAttribute("class", "cssclass1");
		el1.assertContainsAttribute("type", "checkbox");
		el1.assertTrailingText("\n\tinput label TODO\n\t");

		// label is parent for input
		el1.assertHasParent(el);
	}

	@Test
	public void radioRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radio(modelVar,valueProp,\"cssStyleClass1;\")"));
		ASTRadio radio = swl.Radio();
		//@formatter:on

		radio.render(writer);
		parse(writer.toString());

		assertRadioProperlyRendered("cssStyleClass1", "modelVar", 1);

	}

	private void assertRadioProperlyRendered(String cssclass, String group, int labelIdx) {
		Element el = getElement("label", labelIdx);
		el.assertContainsAttribute("class", "radio");
		el.assertContainsText("\n\t\t"); // indentation of next tag

		Element el1 = getElement("input", labelIdx + 1);
		el1.assertDoesNotContainAttribute("style");
		el1.assertContainsAttribute("class", cssclass);
		el1.assertContainsAttribute("type", "radio");
		el1.assertContainsAttribute("name", group); // same value as variable
		el1.assertTrailingText("\n\tinput label TODO\n\t");

		// label is parent for input
		el1.assertHasParent(el);
	}

	@Test
	public void radiosRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios (modelVar,collection, valueProp,\"css;\")"));
		ASTRadios radio = swl.Radios();
		//@formatter:on
	}

	@Test
	public void imgRenderDynamicResource() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img(cv.rawFile,\"width:300px; height:150px;\")"));
		//@formatter:on
		ASTImg img = swl.Img();
		img.render(writer);
		parse(writer.toString());
		System.out.println(writer); //  <img ng-src="GOPR1640.jpg" style="width:300px; height:150px;">

		Element el = getElement("img", 1);
		el.assertContainsAttribute("style", "width: 300px; height: 150px");
		el.assertContainsAttribute("ng-src", "{{cv.rawFile}}");

	}

	@Test
	public void imgRenderStaticResource() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img(\"some_url\",\"width:300px; height:150px;\")"));
		//@formatter:on
		ASTImg img = swl.Img();
		img.render(writer);

		parse(writer.toString());
		System.out.println(writer); //  <img src="GOPR1640.jpg" style="width:300px; height:150px;">

		Element el = getElement("img", 1);
		el.assertContainsAttribute("style", "width: 300px; height: 150px");
		el.assertContainsAttribute("src", "some_url");

	}

}
