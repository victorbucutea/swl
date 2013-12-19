package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAPomXmlEnhancer extends Enhancer<PomXml> {



	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom, GenerationContext ctxt) throws GenerateException {
		/*
		 * <groupId>org.hibernate.javax.persistence</groupId>
		 * <artifactId>hibernate-jpa-2.0-api</artifactId>
		 * <packaging>jar</packaging>
		 * <version>1.0.1.Final</version>
		 */
		PomXml.Dependency dep = new PomXml.Dependency("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api",
				"1.0.1.Final");
		dep.setScope("provided");
		pom.addDependency("jpaAPIDependency", dep);

		/*
		 * 
		 * <repository>
		 * <id>repository.jboss.org</id>
		 * <name>JBoss Repository</name>
		 * <url>http://repository.jboss.org/nexus/content/groups/public-jboss/</url
		 * >
		 * </repository>
		 */
		PomXml.Repository rep = new PomXml.Repository("repository.jboss.org", "JBoss Repository",
				"http://repository.jboss.org/nexus/content/groups/public-jboss/");

		pom.addRepository("jpaRepository", rep);
	}
}