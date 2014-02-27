package ro.swl.engine.generator;

import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class InternalTypesEnhancer extends Enhancer<JavaResource> {

	@Override
	public void enhance(ASTSwdlApp appModel, JavaResource r) throws GenerateException {
		getGlobalCtxt().registerGeneratedType(res.getName(), res.getPackage() + "." + res.getName());
		// TODO iterate through all modules, entities and all fields and register their package
		// best done in an internal enhancer ??

		// also register all classes present in current model.
	}

}
