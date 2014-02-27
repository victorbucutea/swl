package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class ResteasyPomXmlEnhancer extends Enhancer<PomXml> {



	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom) throws GenerateException {

		/*
		 * <groupId>org.jboss.resteasy</groupId>
		 * <artifactId>resteasy-jaxrs</artifactId>
		 * <version>2.3.4.Final</version>
		 * <scope>provided</scope>
		 * <exclusions>
		 * <exclusion>
		 * <artifactId>javassist</artifactId>
		 * <groupId>javassist</groupId>
		 * </exclusion>
		 * </exclusions>
		 */
		PomXml.Dependency dep = new PomXml.Dependency("org.jboss.resteasy", "resteasy-jaxrs", "2.3.4.Final");
		dep.setScope("provided");
		dep.addExclusion("javassist", "javassist");
		pom.addDependency("resteasyDependency", dep);


	}
}
