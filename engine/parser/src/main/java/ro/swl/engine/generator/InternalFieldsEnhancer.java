package ro.swl.engine.generator;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.parser.ASTSwdlApp;


/**
 * Types of Fields in JavaResources might not have been initialized properly.
 * Their package location is not finished until after generation lifecycle.
 * 
 * Therefore the best place to set the proper Fully Quallified Name for the type
 * of the field is in an enhancer.
 * 
 * 
 * @author VictorBucutea
 * 
 */
public class InternalFieldsEnhancer extends Enhancer<JavaResource<Field>> {

	@Override
	public void enhance(ASTSwdlApp appModel, JavaResource<Field> r) throws CreateException {

		for (Field f : r.getFields()) {
			String typeName = f.getType().getFqName();

			String fqName = GlobalContext.getGlobalCtxt().getFqNameForRegisteredType(typeName);

			if (isNotEmpty(fqName)) {
				f.setType(new Type(fqName));
			}

		}

	}
}
