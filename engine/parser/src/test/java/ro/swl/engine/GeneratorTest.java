package ro.swl.engine;

import java.io.File;
import java.util.List;

import org.junit.Before;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.ProjectGenerator;
import ro.swl.engine.generator.Technology;


public abstract class GeneratorTest extends AbstractTest {

	protected ProjectGenerator generator;
	protected GenerationContext ctxt;
	protected File testTemplateDir;


	@Before
	public void setUp() throws Exception {
		ctxt = new GenerationContext();
		generator = new ProjectGenerator(ctxt, getTechsUnderTest());
		testTemplateDir = new File(getClass().getClassLoader().getResource("generate/").toURI());
	}


	public abstract List<Technology> getTechsUnderTest();

}
