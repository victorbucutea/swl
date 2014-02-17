package ro.swl.engine.generator.java.model;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.parser.ASTProperty;


public class Field extends AbstractField<Type> {

	public Field(ASTProperty modelProp, String typePkg) throws GenerateException {
		super(modelProp, typePkg);
	}


	@Override
	protected Type initFieldType(String type, String pkg) throws GenerateException {
		return new Type(type, pkg);
	}

}
