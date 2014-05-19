package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;

/**
 * Created by VictorBucutea on 27.04.2014.
 */
public class EJBPomXmlEnhancer extends Enhancer<PomXml> {

    /**
     * @param appModel
     * @param r
     * @throws CreateException
     */
    @Override
    public void enhance(ASTSwdlApp appModel, PomXml r) throws CreateException {
        /**
         *<dependency>
         <groupId>javax</groupId>
         <artifactId>javaee-api</artifactId>
         <version>6.0</version>
         </dependency>
         */
        PomXml.Dependency dep = new PomXml.Dependency("javax", "javaee-api", "6.0");
        dep.setScope("provided");
        r.addDependency("javaeeApi");

    }
}
