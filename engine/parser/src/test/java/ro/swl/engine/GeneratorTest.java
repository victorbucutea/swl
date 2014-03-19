package ro.swl.engine;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.GlobalContext;
import ro.swl.engine.generator.ProjectGenerator;
import ro.swl.engine.generator.Technology;


public abstract class GeneratorTest extends AbstractTest {

	protected ProjectGenerator generator;
	protected GlobalContext ctxt;
	protected GenerationContext genCtxt;
	protected File testTemplateDir;

	protected File generateDestDir;


	@Before
	public void setUp() throws Exception {
		ctxt = GlobalContext.getGlobalCtxt();
		genCtxt = new GenerationContext();
		generator = new ProjectGenerator(genCtxt, getTechsUnderTest());
		testTemplateDir = new File(getClass().getClassLoader().getResource("templates/").toURI());
		generateDestDir = new File(getClass().getClassLoader().getResource("generated/").toURI());
		FileUtils.cleanDirectory(generateDestDir);
		ctxt.setDestinationDir(generateDestDir);
	}


	@After
	public void tearDown() throws Exception {
		//
	}


	public abstract List<Technology> getTechsUnderTest();


}
