package ro.swl.engine.writer.template.test;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;
import static ro.swl.engine.generator.GenerationContext.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;


public class WriteResourceTreeTest extends GeneratorTest {


	private void generateEnhanceAndWrite(SWL swl) throws ParseException, GenerateException, WriteException {
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-entity-and-static-res"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
		generator.enhance(appModel);
		generator.write(appModel);
	}


	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void entityWrite() throws Exception {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
						" module cv {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		Customer {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			field 	  Blob"+
									"		} " +
									"  }" +
									"}" +
						"module customer {" +
										"  ui     {} " +
										"  logic  {}" +
										"  domain {" +
										"		Client {"+
										"			startDate Date,"+
										"			endDate   Date"+
										"		}"+
										""+	
										"	    Order {"+
										"			startDate Date,"+
										"			endDate   Date,"+
										"			field 	  Blob"+
										"		} " +
										"  }"+
						"}"));
		//@formatter:on
		generateEnhanceAndWrite(swl);

		File generated = new File(generateDestDir, "module-entity-and-static-res");
		Collection<File> list = listFilesAndDirs(generated, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(list);
		System.out.println(files);

		assertEquals("my-app", files.get(1).getName());
		assertEquals(new File(generated, "my-app"), files.get(1).getAbsoluteFile());
		assertEquals("cv", files.get(2).getName());
		assertEquals("ro", files.get(3).getName());
		assertEquals("sft", files.get(4).getName());
		assertEquals("somepackage", files.get(5).getName());
		assertEquals("img.jpg", files.get(6).getName());
		assertEquals(347796, files.get(6).length());
		assertEquals("model", files.get(7).getName());
		assertEquals("Customer.java", files.get(8).getName());
		assertEquals("Experience.java", files.get(9).getName());
		assertEquals("somefile.txt", files.get(10).getName());
		assertEquals(14, files.size());
	}


	@Override
	public List<Technology> getTechsUnderTest() {
		return new ArrayList<Technology>();
	}



}
