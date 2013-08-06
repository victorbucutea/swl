package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;

public class ComponentTest extends SwlTest {

	@Test
	public void noComponents() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	@Test
	public void simpleModel() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			input( inputVar)" +
				"			input_file(inputFileVar)" +
				"			input_area(inputAreaVar)" +
				"			radio(model.radioVar)" +
				"			radios(modelVar,collection)" +
				"			checkbox(var2)" +
				"           selectbox(modelVar1,collection)" +
				"			label('literal')" +
				"			input()" +
				"			selectbox()" +
				"		}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	@Test
	public void styleClassDecl() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			input(\"inputVar\", \"styleClass1;styleClass2;style_Class-N;\")"+ 
				"		}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlFile();

	}

	@Test
	public void inlineStyleDecl() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream("input(var1.x.y.z, \" " +
					"			top: 23%; bottom:3px; "+
					"			color: #000 !important; " +
					" 			*width: 99.94680851063829%;" +
					" 			width: .94680851063829%;" +
					"			background-image: url(\"../img/glyphicons-halflings.png\");"+
					"			background-image: -moz-linear-gradient(top, #f5f5f5, #f9f9f9);" +
					"			background-image: -webkit-gradient(linear, 0 100%, 100% 0, color-stop(0.25, rgba(255, 255, 255, 0.15)), " +
					"				color-stop(0.25, transparent), color-stop(0.5, transparent), color-stop(0.5, rgba(255, 255, 255, 0.15))," +
					"				color-stop(0.75, rgba(255, 255, 255, 0.15)), color-stop(0.75, transparent), to(transparent)); " +
				"				\")"));
		
		//@formatter:on

		ASTInput input = swl.Input();

		List<String> cssClassNames = input.getCssClassNames();
		assertTrue(cssClassNames.isEmpty());

		List<String> cssStyles = input.getCssInlineStyles();

		assertEquals(8, cssStyles.size());
	}

	@Test
	public void combinedInlineAndClassDecl() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream("input(var1.x.y.z, \" " +
					"			top: 23%; bottom:3px; "+
					"			color: #000 !important; " +
					" 			*width: 99.94680851063829%;" +
					" 			 cssStyleClass1 ;cssStyleClass2;" +
					"			\")"));
		
		//@formatter:on

		swl.Input();
	}

	@Test
	public void spacesBetweenStyles() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" screen screen1 {" +
				"			input(var1.x.y.z, \"  cssStyleClass1     ; " +
					"			top: 	23%; 	bottom:3px	; "+
					"			color:	 #000 	!important; " +
					" 			*width: 	99.94680851063829%	  ;   " +
					" 			 cssStyleClass1 ;" +
					"             \ncssStyleClass2;" +
					"			\")"+ 
				"		}" +
				""));
		
		//@formatter:on

		swl.Screen();
	}

	@Test
	public void literalWithStyleLikeText() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" screen screen1 {" +
				"			input(\"top: 	23%  " +
					"			\")"+ 
				"		}" +
				""));
		
		//@formatter:on

		swl.Screen();
	}

	@Test
	public void radio() throws ParseException, UnsupportedEncodingException {
		//@formatter:off
			SWL swl = new SWL (createInputStream("radio()"));
		//@formatter:on
		swl.Radio();
	}

	@Test
	public void radioGroupName() throws ParseException, UnsupportedEncodingException {
		//@formatter:off
			SWL swl = new SWL (createInputStream("radio(modelVar,\"cssStyleClass1;\")"));
		//@formatter:on
		swl.Radio();
	}

	@Test
	public void radioWithVariableValue() throws ParseException, UnsupportedEncodingException {
		//@formatter:off
			SWL swl = new SWL (createInputStream("radio(modelVar,\"cssStyleClass1;\")"));
		//@formatter:on
		swl.Radio();
	}

	@Test
	public void radioNoCss() throws ParseException, UnsupportedEncodingException {
		//@formatter:off
			SWL swl = new SWL (createInputStream("radio(modelVar)"));
		//@formatter:on
		swl.Radio();
	}

	@Test
	public void radiosAllProps() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,\"background-url: 'http://www.somesite.com';\")"));
		//@formatter:on
		swl.Radios();
	}

	@Test
	public void radiosAllPropsSingleQuoteForCss() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,'background-url: \"http://www.somesite.com\";')"));
		//@formatter:on
		swl.Radios();
	}

	@Test
	public void radiosAllPropsWithSimpleCssClass() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,\"someCssClass ;\")"));
		//@formatter:on
		swl.Radios();
	}

	@Test
	public void radiosMandatoryAttrs() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection)"));
		//@formatter:on
		swl.Radios();
	}

	@Test
	public void simpleRadios() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios()"));
		//@formatter:on
		swl.Radios();
	}

	@Test
	public void simpleSelectBox() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox()"));
		//@formatter:on
		swl.Selectbox();
	}

	@Test
	public void selectBoxWithMandatoryAttrs() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox(modelValue,modelCollection)"));
		//@formatter:on
		swl.Selectbox();
	}

	@Test
	public void selectBoxWithAllAttrs() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox(modelValue,modelCollection,\"styleClass;\")"));
		//@formatter:on
		swl.Selectbox();

	}

	@Test
	public void selectBoxWithOptions() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox(modelValue,modelCollection) {" +
				" selectoption() selectoption()}"));
		//@formatter:on
		swl.Selectbox();
	}

	@Test
	public void selectBoxWithOptionGroups() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox(modelValue,modelCollection) {" +
				" selectoptiongroup() { selectoption() selectoption()} " +
				" selectoptiongroup() { selectoption() selectoption()}" +
				" selectoption() }"));
		//@formatter:on
		swl.Selectbox();
	}

	@Test
	public void selectBoxWithAdvOptions() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("selectbox(modelValue,modelCollection) {" +
				" selectoption(\"value\",\"label\") selectoption(\"value\",\"label\",'styleclass;')}"));
		//@formatter:on
		swl.Selectbox();
	}

	@Test
	public void imgSimple() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img(cv.rawFile,\"width:300px; height:150px;\")"));
		//@formatter:on
		swl.Img();
	}

	@Test
	public void imgLiteral() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img('cv.rawFile')"));
		//@formatter:on
		swl.Img();
	}

	@Test
	public void inputLiteral() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("input('some text')"));
		//@formatter:on
		swl.Input();
	}

	@Test
	public void labelLiteral() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("label(\"some text\")"));
		//@formatter:on
		swl.Label();
	}

	@Test
	public void imgEmpty() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img()"));
		//@formatter:on
		swl.Img();
	}

	@Test
	public void simpleVerticalLayout() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout() {}"
								));
		//@formatter:on

		swl.VerticalLayout();

	}

	@Test
	public void verticalLayoutWithContent() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout () {"+
													"img(cv.rawFile,\"width:300px; height:150px;\") "+
													"input_file(cv.rawFile) " +
													"img(\"/img/static.png\")" +
												"}"
								));
		//@formatter:on
		swl.VerticalLayout();
	}

	@Test
	public void verticalLayoutModelController() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout () {"+
													"model {} "+
													" controller {} " +
													"img(cv.rawFile,\"width:300px; height:150px;\") "+
													"input_file(cv.rawFile) " +
													"img(\"/img/static.png\")" +
												"}"
								));
		//@formatter:on
		swl.VerticalLayout();
	}

	@Test
	public void nestedVerticalLayoutWithContent() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout () {"+
														"//layout can be dynamically defined \n"+ 
														"img(cv.rawFile,\"width:300px; height:150px;\") "+
														"input_file(cv.rawFile) " +
														"img(\"/img/static.png\")" +
														"vertical_layout () {" +
														" input() input_file()" +
														"}" +
											"}"
								));
		//@formatter:on

		swl.VerticalLayout();

	}

	@Test
	public void nestedVerticalLayoutWithModelAndContent() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("vertical_layout (\"cssStyleClass2; top:20px;\") {"+
														"img(cv.rawFile,\"width:300px; height:150px;\") "+
														"input_file(cv.rawFile) " +
														"img(\"/img/static.png\")" +
														"vertical_layout () {" +
															" model { some content inside model, var x = asdadsa } " +
															" controller { some content inside controller for (int i = 0 ; i < 100 ; i ++ ) {} }" +
															" input() " +
															" input_file()" +
														"}" +
											"}"
								));
		//@formatter:on

		swl.VerticalLayout();
	}

	@Test
	public void simpleHorizontalLayout() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(4,1,1,1,1,1) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test
	public void simpleHorizontalLayout2() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout() {}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test
	public void simpleHorizontalLayoutWithCss() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout([\"container-css;\"]4[\"row-css;\"],1,1,1['background-color: blue;'],1,1) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test
	public void simpleHorizontalLayoutWithCssAllOver() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout([\"container-css;\"]4[\"row1-css;\"],1[\"row2-css;\"]," +
																			"1[\"row-css;\"],1['background-color: blue;'],1[\"row-css;\"],1[\"row-css;\"]) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+ 
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test(expected = ParseException.class)
	public void horizontalLayoutWithMoreThen12Cols() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(1,1,1,1,1,1,1,1,1,1,1,1,1) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test(expected = ParseException.class)
	public void horizontalLayoutWithMoreThen12Cols2() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(13) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+ 
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test(expected = ParseException.class)
	public void horizontalLayoutWithMoreThen12Cols3() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(4,1,1,2,2,3) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\")"+
                            "radio(materials,\"1\") "+ 
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test(expected = ParseException.class)
	public void horizontalLayoutWithMoreThen12Cols4() throws ParseException {
		//@formatter:off
				SWL swl = new SWL (createInputStream(" horizontal_layout(5,5,5) {"+
                            "label(\"Course Materials\",\"font-size:90%;\")"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "radio(materials)"+
                            "}"));
				//@formatter:on

		swl.HorizontalLayout();
	}

	@Test
	public void simpleButton() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("button()"));
		//@formatter:on

		swl.Button();
	}

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
	public void simpleButtonWithActionParameterAndCss() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("button(saveAction(var1.subvar2.subvar3)),'width :300px;')"));
		//@formatter:on

		swl.Button();
	}

	@Test
	public void simpleHorizontalForm() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("horizontal_form('width :300px;') {} "));
		//@formatter:on

		swl.HorizontalForm();
	}

	@Test
	public void simpleHorizontalFormWithContent() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("horizontal_form('width :300px;') {" +
				"							  model {} " +
				"							  controller{} " +
				"								input() input_file() " +
				"								horizontal_layout(){} } "));
		//@formatter:on

		swl.HorizontalForm();
	}

}
