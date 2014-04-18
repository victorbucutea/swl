package ro.swl.engine.generator;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class InternalTypesEnhancer extends Enhancer<JavaResource<Field>> {

	@Override
	public void enhance(ASTSwdlApp appModel, JavaResource<Field> res) throws CreateException {
		getGlobalCtxt().registerGeneratedType(res.getName(), res.getPackage() + "." + res.getName());
	}

}
