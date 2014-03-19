package ro.swl.engine.generator.java.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.parser.ASTProperty;


public class Field extends AbstractField {

	public Field(String name, String type, String pkg) throws GenerateException {
		super(name, type, pkg);
	}


	public Field(ASTProperty modelProp, String typePkg) throws GenerateException {
		super(modelProp, typePkg);
	}


	@Override
	protected Type initFieldType(String type, String pkg) throws GenerateException {
		return new Type(isNotEmpty(pkg) ? pkg + "." + type : type);
	}


}
