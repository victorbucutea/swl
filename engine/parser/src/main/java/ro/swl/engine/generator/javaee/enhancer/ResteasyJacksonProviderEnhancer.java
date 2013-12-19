package ro.swl.engine.generator.javaee.enhancer;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class ResteasyJacksonProviderEnhancer extends Enhancer<PomXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom, GenerationContext ctxt) throws GenerateException {

		/*
		 * <groupId>org.jboss.resteasy</groupId>
		 * <artifactId>resteasy-jackson-provider</artifactId>
		 * <version>2.3.4.Final</version>
		 * <scope>provided</scope>
		 */

		PomXml.Dependency dep = new PomXml.Dependency("org.jboss.resteasy", "resteasy-jackson-provider", "2.3.4.Final");
		dep.setScope("provided");


		List<PomXml.Dependency> depMngmt = new ArrayList<PomXml.Dependency>();

		/*
		 * <dependency>
		 * <groupId>org.codehaus.jackson</groupId>
		 * <artifactId>jackson-xc</artifactId>
		 * <version>1.9.2</version>
		 * </dependency>
		 * <dependency>
		 * <groupId>org.codehaus.jackson</groupId>
		 * <artifactId>jackson-mapper-asl</artifactId>
		 * <version>1.9.2</version>
		 * </dependency>
		 * <dependency>
		 * <groupId>org.codehaus.jackson</groupId>
		 * <artifactId>jackson-core-asl</artifactId>
		 * <version>1.9.2</version>
		 * </dependency>
		 * <dependency>
		 * <groupId>org.codehaus.jackson</groupId>
		 * <artifactId>jackson-jaxrs</artifactId>
		 * <version>1.9.2</version>
		 * </dependency>
		 */
		pom.addDependencyManagement(depMngmt);
	}
}
