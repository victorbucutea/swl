package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class HibernatePomXmlEnhancer extends Enhancer<PomXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom) throws CreateException {
		/*
		 * <groupId>org.hibernate</groupId>
		 * <artifactId>hibernate-entitymanager</artifactId>
		 * <version>4.2.0.Final</version>
		 */
		PomXml.Dependency dep = new PomXml.Dependency("org.hibernate", "hibernate-entitymanager", "4.2.0.Final");
		pom.addDependency("hibernateDependency", dep);

	}

}
