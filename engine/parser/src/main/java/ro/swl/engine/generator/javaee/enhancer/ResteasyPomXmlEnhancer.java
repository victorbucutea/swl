package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.generator.javaee.model.PomXml.Dependencies;
import ro.swl.engine.generator.javaee.model.PomXml.Dependency;
import ro.swl.engine.parser.ASTSwdlApp;

import java.util.Arrays;

import static java.util.Arrays.asList;


public class ResteasyPomXmlEnhancer extends Enhancer<PomXml> {


    @Override
    public void enhance(ASTSwdlApp appModel, PomXml pom) throws CreateException {


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
		 *
		 *  </dependency>
        <dependency>
        <groupId>org.jboss.resteasy</groupId>
        <artifactId>resteasy-jackson-provider</artifactId>
        <scope>provided</scope>
        </dependency>
		 */
        PomXml.Dependency dep = new PomXml.Dependency("org.jboss.resteasy", "resteasy-jaxrs", "2.3.4.Final");
        dep.setScope("provided");
        dep.addExclusion("javassist", "javassist");

        PomXml.Dependency jacksonPrvder = new PomXml.Dependency("org.jboss.resteasy", "resteasy-jackson-provider", "2.3.4.Final");
        jacksonPrvder.setScope("provided");

        pom.addDependency("jaxRsProviderDependency", dep , jacksonPrvder);


        /**
         *  <dependencies>
                 ${model.get('jaxRsProviderDependencyMgmnt')}
                 <dependency>
                 <groupId>org.codehaus.jackson</groupId>
                 <artifactId>jackson-xc</artifactId>
                 <version>1.9.2</version>
                 </dependency>
                 <dependency>
                 <groupId>org.codehaus.jackson</groupId>
                 <artifactId>jackson-mapper-asl</artifactId>
                 <version>1.9.2</version>
                 </dependency>
                 <dependency>
                 <groupId>org.codehaus.jackson</groupId>
                 <artifactId>jackson-core-asl</artifactId>
                 <version>1.9.2</version>
                 </dependency>
                 <dependency>
                 <groupId>org.codehaus.jackson</groupId>
                 <artifactId>jackson-jaxrs</artifactId>
                 <version>1.9.2</version>
                 </dependency>
         </dependencies>
         */

        Dependencies depMngmt = new Dependencies();

        PomXml.Dependency jacksonXc = new Dependency("org.codehaus.jackson","jackson-xc","1.9.2");
        PomXml.Dependency jacksonMapperAsl = new Dependency("org.codehaus.jackson","jackson-mapper-asl","1.9.2");
        PomXml.Dependency jacksonCoreAsl = new Dependency("org.codehaus.jackson","jackson-jaxrs","1.9.2");

        depMngmt.addAll(asList(jacksonXc, jacksonMapperAsl, jacksonCoreAsl));

        pom.addDependencyManagement("jaxRsProviderDependencyMgmnt",depMngmt);

    }
}
