package ro.swl.engine.generator.javaee.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.model.QualifiedClassName;


public class EntityType extends Type {

	public EntityType(String collectionType, String genericType, String pkg) throws CreateException {
		super(pkg + "." + collectionType + "<" + genericType + ">");
		clsName = new QualifiedClassName("java.util." + collectionType + "<" + genericType + ">");
	}


	public EntityType(String declaredName, String pkg) throws CreateException {
		super(internalTypes.containsKey(declaredName) ? declaredName : isNotEmpty(pkg) ? pkg + "." + declaredName
				: declaredName);
	}

}
