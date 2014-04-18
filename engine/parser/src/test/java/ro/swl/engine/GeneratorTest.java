package ro.swl.engine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.swl.engine.generator.GlobalContext;
import ro.swl.engine.generator.ProjectGenerator;
import ro.swl.engine.generator.Skeleton;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;


public abstract class GeneratorTest extends AbstractTest {

	protected ProjectGenerator generator;
	protected GlobalContext ctxt;
	protected File testTemplateDir;
	protected File generateDestDir;
	protected Skeleton skeleton;


	//TODO sync binaries (target/test-classes/skeletons-test) with what we have in src/test/resources/skeletons-test folder
	// if you delete from src/test/resources/skeletons-test it will not delete from (target/test-classes/skeletons-test) 
	@Before
	public void setUp() throws Exception {
		ctxt = GlobalContext.getGlobalCtxt();
		ctxt.setDefaultPackage("ro.sft.recruiter");
		ctxt.setProjectName("test-gen");
		testTemplateDir = new File(getClass().getClassLoader().getResource("skeletons-test/").toURI());
		skeleton = new Skeleton();
		skeleton.setSkeletonRootDir(testTemplateDir);
		generator = new ProjectGenerator(skeleton, getTechsUnderTest());
		generateDestDir = new File(getClass().getClassLoader().getResource("generated/").toURI());
		FileUtils.cleanDirectory(generateDestDir);
		ctxt.setDestinationDir(generateDestDir);
	}


	@After
	public void tearDown() throws Exception {
		//
	}


	public abstract List<Technology> getTechsUnderTest();



	protected void printTree(Resource root) throws IOException {
		StringWriter writer = new StringWriter();
		root.printTree(writer);
		System.out.println(writer.toString());
	}


	protected void parseXmlWithHandler(List<File> list, DefaultHandler handler) throws ParserConfigurationException,
			SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		try {
			saxParser.parse(FileUtils.openInputStream(list.get(2)), handler);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
