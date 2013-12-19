package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRSPomXmlEnhancer extends Enhancer<PomXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom, GenerationContext ctxt) throws GenerateException {

		/*
		 * <dependency>
		 * <groupId>org.jboss.spec.javax.ws.rs</groupId>
		 * <artifactId>jboss-jaxrs-api_1.1_spec</artifactId>
		 * <version>1.0.1.Final</version>
		 * <scope>provided</scope>
		 * </dependency>
		 */
		PomXml.Dependency dep = new PomXml.Dependency("org.jboss.spec.javax.ws.rs", "jboss-jaxrs-api_1.1_spec",
				"1.0.1.Final");
		dep.setScope("provided");

		pom.addDependency("jxrsApiDependency", dep);

	}
}
