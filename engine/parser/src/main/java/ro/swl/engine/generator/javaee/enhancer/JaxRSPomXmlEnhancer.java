package ro.swl.engine.generator.javaee.enhancer;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRSPomXmlEnhancer extends Enhancer<PomXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom) throws CreateException {
		getGlobalCtxt().setProperty("jaxrs-dependencies", "true");
	}
}
