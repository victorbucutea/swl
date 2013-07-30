package ro.swl.engine.parser.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import ro.swl.engine.SwlTest;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;

public class ScreenTest extends SwlTest {

	@Test
	public void multipleScreens() throws UnsupportedEncodingException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module'"
				+ " \n\t\n module CV{ " +
				"	ui {" +
				"		screen screen1 {" +
				"			model { }" +
				"			controller { }" +
				"			input()" +
				"			}	" +
					   "screen screen2 {" +
					"			model {   }" +
					"			input()" +
					"			}	" +
				"		}" +
				"}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	@Test
	public void simpleModel() throws UnsupportedEncodingException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"			model { } " +
				"			controller { }" +
				"			}" +
				"}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	// 

	@Test
	public void modelWithJsTextInside1() throws UnsupportedEncodingException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"				model { {  sdfsdfsd { sdfsdf } sdf } }" +
				"			controller { }}}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	@Test
	public void modelWithJsFirstLineComment() throws UnsupportedEncodingException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {\n" +
								"/// comment \n" +
				"				model { " +
				"					//comment" +
				"					    {  sdfsdfsd { sdfsdf } sdf } " +
				"					  }" +
				"			controller { }}}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

	@Test
	public void modelAndCtrlrWithJsTextInside() throws UnsupportedEncodingException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' module CV{ " +
				"	ui { screen screen1 {" +
				"				model {  {@#$%^&*} "+
				"						var x = something;"+
				"						asd ***-*-*++{ asd asd }"+
				"						for ( var i = 0 ; i <= 100 ; i ++ ) { "+
				"						// a comment " +
				"						/* a multiline comment*/ "+  
				"						doSomething(i);"+
				"						i*5; i%5;}" +
				"						}" +
				"			controller {  {@#$%^&*} "+
				"						var x = something;"+
				"						asd ***-*-*++{ asd asd }"+
				"						for ( var i = 0 ; i <= 100 ; i ++ ) { "+
				"						// a comment " +
				"						/* a multiline comment*/ "+  
				"						doSomething(i);"+
				"						i*5; i%5;}" +
				"						}}}}"));
		
		//@formatter:on

		swl.SwdlFile();
	}

}
