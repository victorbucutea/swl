package ro.swl.engine;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.ProjectGenerator;
import ro.swl.engine.generator.Technology;


public abstract class GeneratorTest extends AbstractTest {

	protected ProjectGenerator generator;
	protected GenerationContext ctxt;
	protected File testTemplateDir;

	protected File generateDestDir;


	@Before
	public void setUp() throws Exception {
		ctxt = new GenerationContext();
		generator = new ProjectGenerator(ctxt, getTechsUnderTest());
		testTemplateDir = new File(getClass().getClassLoader().getResource("templates/").toURI());
		generateDestDir = new File(getClass().getClassLoader().getResource("generated/").toURI());
		ctxt.setDestinationDir(generateDestDir);
	}


	@After
	public void tearDown() throws Exception {
		//FileUtils.cleanDirectory(generateDestDir);
	}


	public abstract List<Technology> getTechsUnderTest();

}
