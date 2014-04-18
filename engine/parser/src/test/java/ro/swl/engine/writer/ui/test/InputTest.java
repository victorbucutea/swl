package ro.swl.engine.writer.ui.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTCheckbox;
import ro.swl.engine.parser.ASTHorizontalForm;
import ro.swl.engine.parser.ASTImg;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ASTInputArea;
import ro.swl.engine.parser.ASTInputFile;
import ro.swl.engine.parser.ASTRadio;
import ro.swl.engine.parser.ASTRadios;
import ro.swl.engine.parser.ASTText;
import ro.swl.engine.parser.ASTVerticalLayout;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;


public class InputTest extends WriterTest {

	@Test
	public void textRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("" +
				"		text(modelVar,' cssclass1; height:200px; width:30px;')"));
		//@formatter:on

		ASTText text = swl.Text();

		text.render(writer);

		parse(writer.toString());

		Element element = getElement("span", 1);
		element.assertContainsText("label TODO");
		element.assertIndentAfterClose(0);
		element.assertNoNewLineAfterClose();

	}


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
	public void simpleHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form() {}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "form-horizontal");
		el.assertDoesNotContainAttribute("style");

		try {
			getElement("div", 2);
			fail("No child element should exist");
		} catch (Exception e) {
		}

	}


	@Test
	public void simpleHorizontalForm1() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form() {row() { text('sometext') }}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);


		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "form-horizontal");
		el.assertDoesNotContainAttribute("style");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");

		el = getElement("span", 5);

	}


	@Test
	public void horizontalFormLabelForCheckboxAndRadio() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {" +
									"row() {text(\"Course Materials\",\"font-size:90%;\") }"+
						            "row() {radio(materials,\"value1\",\"css1;\")}"+
						            "row() {radio(materials,\"value2\",\"css2;\")}" +
						            "row() {radio(materials,\"value3\",\"css3;\")}" +
						            "row() {checkbox('x') input('y') }"+ 
						            "row() {input(modelValue,\"css3;\")}" +
						                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class form-horizontal");
		el.assertContainsAttribute("style", "some-css: someValue");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");
		// label is inlined
		el.assertIndentAfterOpenTag(0);

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("span", 5);
		el.assertContainsAttribute("style", "font-size: 90%");

		el = getElement("input", 10);
		el.assertContainsAttribute("class", "css1");

		assertRadioProperlyRendered("css2", "materials", 14);

		assertRadioProperlyRendered("css3", "materials", 19);

		el = getElement("label", 29);
		el.assertContainsText("input label TODO");

		el = getElement("input", 32);
		el.assertContainsAttribute("type", "text");

	}


	@Test
	public void horizontalFormLabelForLayout() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class;\") {" +
									            "row() { horizontal_layout () {" +
									            "								column(2) { radio(materials,\"value1\") input(someValue) }" +
									            "							  }" +
									            "}"+
							            "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class form-horizontal");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");

		el = getElement("div", 5);
		el.assertContainsAttribute("class", "row-fluid");

		el = getElement("div", 6);
		el.assertContainsAttribute("class", "span2");

		el = getElement("input", 8);
		el.assertContainsAttribute("type", "radio");
		el.assertContainsAttribute("name", "materials");

		el = getElement("label", 9);
		el = getElement("input", 10);
	}


	@Test
	public void horizontalLayoutInHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {" +
									            "row() { horizontal_layout () {" +
									            "								column(2) { radio(materials,\"value1\") }" +
									            "								column(2) { #input(someValue) } " +
									            "								column(2) { #input(someValue3) }" +
									            "							  }" +
									            "}"+
							            "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		List<ASTInput> childInputs = layout.getChildNodesOfType(ASTInput.class, true);

		assertFalse(childInputs.get(0).isRenderLabel());
		assertFalse(childInputs.get(1).isRenderLabel());

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class form-horizontal");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");

		el = getElement("div", 5);
		el.assertContainsAttribute("class", "row-fluid");

		el = getElement("div", 6);
		el.assertContainsAttribute("class", "span2");

		el = getElement("input", 8);
		el.assertContainsAttribute("type", "radio");
		el.assertContainsAttribute("name", "materials");

		el = getElement("input", 10);
		el.assertContainsAttribute("type", "text");

		el = getElement("input", 12);
		el.assertContainsAttribute("type", "text");
	}


	@Test
	public void sectionInVerticalLayout_horizontalFormInVerticalLayout() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" vertical_layout() {" +
					"							  row() { horizontal_form() {"+
												                    "row() { checkbox(\"Course Materials\",\"font-size:90%;\")}"+
												                    "}" +
												        "}" +
										                    
							                      "row () {section('some_section','css1;') {" +
							                      "		horizontal_form() {"+
										                    " row () { vertical_layout () { row('form-inline;') { text('lbl')  input(somevalue) }} " +
										                    			"input(modelValue,\"css3;\") " +
										                    		"}" +
									                    "}}" +
									              "}" +
								              "}"
						                      ));
		//@formatter:on

		ASTVerticalLayout layout = swl.VerticalLayout();

		layout.render(writer);

		parse(writer);



		Element el = getElement("div", 1);
		el.assertDoesNotContainAttribute("class");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 2);
		el.assertDoesNotContainAttribute("class");


		el = getElement("div", 3);
		el.assertContainsAttribute("class", "form-horizontal");

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "control-group");

		el = getElement("input", 8);
		el.assertContainsAttribute("type", "checkbox");
		el.assertContainsAttribute("style", "font-size: 90%");

		el = getElement("div", 9);
		el.assertDoesNotContainAttribute("class");

		el = getElement("fieldset", 10);
		el.assertContainsAttribute("class", "css1");

		el = getElement("legend", 11);
		el.assertDoesNotContainAttribute("class");

		el = getElement("div", 13);
		el.assertContainsAttribute("class", "control-group");

		el = getElement("label", 14);
		el.assertContainsAttribute("class", "control-label");

		el = getElement("div", 16);

		el = getElement("div", 17);
		el.assertContainsAttribute("class", "form-inline");

		el = getElement("span", 18);

		el = getElement("input", 20);
		el.assertContainsAttribute("type", "text");

		el = getElement("input", 22);
		el.assertContainsAttribute("type", "text");
		el.assertContainsAttribute("class", "css3");
	}


	@Test
	public void verticalLayoutInHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {"+
						                    "row() { text(\"Course Materials\",\"font-size:90%;\")} "+
						                    "row() {input(materials,\"css1;\") }"+
						                    "row() {radio(materials,\"value2\",\"css2;\") }"+
						                    "row() {radio(materials,\"value3\",\"css3;\")  }"+ 
						                    "row() {vertical_layout () { row() { text('lbl') } row() {  input(somevalue) }} }" +
						                    "row() {input(modelValue,\"css3;\") }" +
						                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);


		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class form-horizontal");
		el.assertContainsAttribute("style", "some-css: someValue");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");
		// label is inlined
		el.assertIndentAfterOpenTag(0);
		el.assertNoNewLineAfterOpenTag();

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("span", 5);
		el.assertContainsAttribute("style", "font-size: 90%");

		el = getElement("input", 10);
		el.assertContainsAttribute("class", "css1");

		assertRadioProperlyRendered("css2", "materials", 14);

		assertRadioProperlyRendered("css3", "materials", 19);

		el = getElement("div", 25);
		el = getElement("span", 26);

		el = getElement("label", 28);
		el.assertContainsText("input label TODO");

		el = getElement("input", 29);
		el.assertContainsAttribute("type", "text");

	}


	@Test
	public void inputInHorizontalForm() throws ParseException, WriteException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_form() {"+
															"row() {  #input(materials)  " +
															"		  #input(modelValue) " +
															"		  }" +
															"row() {  input(materials)  " +
															"		  input(modelValue) " +
															"		  }" +
															"}" +
								                    "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "form-horizontal");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();


		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");
		el.assertIndentAfterOpenTag(3);
		el.assertNewLineAfterOpenTag();

		el = getElement("input", 5);
		el = getElement("input", 6);


		el = getElement("div", 7);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();


		el = getElement("label", 8);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");

	}


	@Test
	public void multipleInputRadioInHorizForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form() {"+
													"row() {  radio(modelValue,'radio value 1')  " +
													"		  radio(modelValue,'radio value 2') " +
													"		}" +
													"row() {  radio(modelValue2, 'radio value 3')  " +
													"		  radio(modelValue2, 'radio value 4') " +
													"		}" +
													"}" +
						                    "}"));
		//@formatter:on
		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);


		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "form-horizontal");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();


		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");

		assertRadioProperlyRendered(null, "modelValue", 5);

		assertRadioProperlyRendered(null, "modelValue", 7);

		el = getElement("div", 9);

		assertRadioProperlyRendered(null, "modelValue2", 12);

		assertRadioProperlyRendered(null, "modelValue2", 14);

	}



	@Test
	public void multipleInputCheckboxInHorizForm() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form() {"+
													"row() {  checkbox(modelValue)  " +
													"		  checkbox(modelValue) " +
													"		}" +
													"row() {  checkbox(modelValue2)  " +
													"		  checkbox(modelValue2) " +
													"		}" +
													"}" +
						                    "}"));
		//@formatter:on
		ASTHorizontalForm layout = swl.HorizontalForm();

		layout.render(writer);

		parse(writer);



		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "form-horizontal");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();


		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");

		assertCheckboxProperlyRendered(null, "modelValue", 5);

		assertCheckboxProperlyRendered(null, "modelValue", 7);

		el = getElement("div", 9);

		assertCheckboxProperlyRendered(null, "modelValue2", 12);

		assertCheckboxProperlyRendered(null, "modelValue2", 14);
	}


	@Test
	public void inputInVerticalLayout() throws ParseException, WriteException {
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

		parse(writer);

		// 1st row
		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class");
		el.assertContainsAttribute("style", "some-css: someValue");

		el = getElement("span", 3);
		el.assertContainsText("label TODO");

		// 2nd row
		el = getElement("div", 4);
		assertRadioProperlyRendered("css1", "materials", 5);

		//3rd row
		el = getElement("div", 7);
		assertRadioProperlyRendered("css2", "materials", 8);

		el = getElement("div", 10);
		assertCheckboxProperlyRendered("css3", "value3", 11);

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
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		Element el1 = getElement("input", 2);
		el1.assertContainsAttribute("style", "height: 200px; width: 30px");
		el1.assertContainsAttribute("class", "cssclass1");
		el1.assertContainsAttribute("type", "checkbox");
		el1.assertTrailingText("input label TODO");

		// label is parent for input
		el1.assertHasParent(el);
	}


	@Test
	public void radioRender() throws ParseException, WriteException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radio(modelVar,valueProp,'cssStyleClass1;')"));
		ASTRadio radio = swl.Radio();
		//@formatter:on

		radio.render(writer);
		parse(writer.toString());

		assertRadioProperlyRendered("cssStyleClass1", "modelVar", 1);

	}


	@Test
	public void radioLiteralLabel() throws Exception {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radio(modelVar,'value label')"));
		ASTRadio radio = swl.Radio();
		//@formatter:on

		radio.render(writer);
		parse(writer.toString());

		assertRadioProperlyRendered("cssStyleClass1", "modelVar", 1);

		Element el = getElement("input", 2);
		el.assertTrailingText("value label");
	}


	private void assertRadioProperlyRendered(String cssclass, String group, int labelIdx) {
		Element el = getElement("label", labelIdx);
		el.assertContainsAttribute("class", "radio");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		Element el1 = getElement("input", labelIdx + 1);
		el1.assertDoesNotContainAttribute("style");
		el1.assertContainsAttribute("type", "radio");
		el1.assertContainsAttribute("name", group); // same value as variable

		String id = el1.getAttribute("id");
		el.assertContainsAttribute("for", id);

		// label is parent for input
		el1.assertHasParent(el);
	}


	private void assertCheckboxProperlyRendered(String cssclass, String group, int labelIdx) {
		Element el = getElement("label", labelIdx);
		el.assertContainsAttribute("class", "checkbox");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		Element el1 = getElement("input", labelIdx + 1);
		el1.assertDoesNotContainAttribute("style");
		if (cssclass != null)
			el1.assertContainsAttribute("class", cssclass);
		el1.assertContainsAttribute("type", "checkbox");

		String id = el1.getAttribute("id");
		el.assertContainsAttribute("for", id);

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

		Element el = getElement("img", 1);
		el.assertContainsAttribute("style", "width: 300px; height: 150px");
		el.assertContainsAttribute("src", "some_url");

	}


	@Test
	public void buttonInHorizontalForm() throws Exception {
		//@formatter:off
		SWL swl = new SWL (createInputStream(" horizontal_form(\"vertical-css-class; some-css: someValue;\") {" +
									"row() {text(\"Course Materials\",\"font-size:90%;\") }"+
						            "row() {radio(materials,\"value1\",\"css1;\")}"+
						            "row() {radio(materials,\"value2\",\"css2;\")}" +
						            "row() {checkbox('x') input('y') }"+ 
						            "#row() {button(controllerAction())}" +
						            "}"));
		//@formatter:on

		ASTHorizontalForm layout = swl.HorizontalForm();

		assertFalse(layout.getRows().get(4).isRenderLabel());

		layout.render(writer);

		parse(writer);

		Element el = getElement("div", 1);
		el.assertContainsAttribute("class", "vertical-css-class form-horizontal");
		el.assertContainsAttribute("style", "some-css: someValue");
		el.assertIndentAfterOpenTag(1);
		el.assertNewLineAfterOpenTag();

		el = getElement("div", 2);
		el.assertContainsAttribute("class", "control-group");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("label", 3);
		el.assertContainsAttribute("class", "control-label");
		el.assertContainsText("input label TODO");
		// label is inlined
		el.assertIndentAfterOpenTag(0);
		el.assertNoNewLineAfterOpenTag();

		el = getElement("div", 4);
		el.assertContainsAttribute("class", "controls");
		el.assertIndentAfterOpenTag(2);
		el.assertNewLineAfterOpenTag();

		el = getElement("span", 5);
		el.assertContainsAttribute("style", "font-size: 90%");

		el = getElement("input", 10);
		el.assertContainsAttribute("class", "css1");

		assertRadioProperlyRendered("css2", "materials", 14);

		// label should have been at index 24, but we skip it
		el = getElement("div", 24);

		el = getElement("button", 25);
		el.assertContainsAttribute("class", "btn");
	}

}
