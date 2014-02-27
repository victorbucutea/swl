package ro.swl.engine.generator.javaee.enhancer;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRSPomXmlEnhancer extends Enhancer<PomXml> {

	@Override
	public void enhance(ASTSwdlApp appModel, PomXml pom) throws GenerateException {
		getGlobalCtxt().setProperty("jaxrs-dependencies", "true");
	}
}
