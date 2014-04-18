package ro.swl.engine.generator.java.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.parser.ASTProperty;


public class Field extends AbstractField {

	public Field(String name, String type, String pkg) throws CreateException {
		super(name, type, pkg);
	}


	public Field(ASTProperty modelProp, String typePkg) throws CreateException {
		super(modelProp, typePkg);
	}


	@Override
	protected Type initFieldType(String type, String pkg) throws CreateException {
		return new Type(isNotEmpty(pkg) ? pkg + "." + type : type);
	}


}
