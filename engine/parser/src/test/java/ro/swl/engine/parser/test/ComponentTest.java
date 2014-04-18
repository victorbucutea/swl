package ro.swl.engine.parser.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.WriterTest;
import ro.swl.engine.parser.ASTInput;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class ComponentTest extends WriterTest {

	@Test
	public void noComponents() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlApp();
	}


	@Test
	public void simpleModel() throws ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			input( inputVar)" +
				"			input_file(inputFileVar)" +
				"			input_area(inputAreaVar)" +
				"			radio(modelVar,valueProp,'cssStyleClass1;')" +
				"			radios(modelVar,collection,someVar,'cssStyleClass1;')" +
				"			checkbox(var2)" +
				"           selectbox(modelVar1,collection)" +
				"			text('literal')" +
				"			input()" +
				"			selectbox()" +
				"		}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlApp();
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

		swl.SwdlApp();

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
			SWL swl = new SWL (createInputStream("radio(modelVar,groupName,\"cssStyleClass1;\")"));
		//@formatter:on
		swl.Radio();
	}


	@Test
	public void radioNoCss() throws ParseException, UnsupportedEncodingException {
		//@formatter:off
			SWL swl = new SWL (createInputStream("radio(modelVar,groupName)"));
		//@formatter:on
		swl.Radio();
	}


	@Test
	public void radiosAllProps() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,valueProp,\"background-url: 'http://www.somesite.com';\")"));
		//@formatter:on
		swl.Radios();
	}


	@Test
	public void radiosAllPropsSingleQuoteForCss() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,valueProp,'background-url: \"http://www.somesite.com\";')"));
		//@formatter:on
		swl.Radios();
	}


	@Test
	public void radiosAllPropsWithNoCssClass() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("radios(modelVar,collection,valueProp)"));
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
				" selectoption(\"value\",\"label\")" +
				" selectoption(\"value\",\"label\",'styleclass;')}"));
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
		SWL swl = new SWL (createInputStream("text(\"some text\")"));
		//@formatter:on
		swl.Text();
	}


	@Test
	public void imgEmpty() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("img()"));
		//@formatter:on
		swl.Img();
	}


	@Test
	public void simpleHorizontalForm() throws ParseException {
		//@formatter:off
		SWL swl = new SWL (createInputStream("horizontal_form('width :300px;') {} "));
		//@formatter:on

		swl.HorizontalForm();
	}

}
