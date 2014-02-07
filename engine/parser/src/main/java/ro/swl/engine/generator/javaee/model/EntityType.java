package ro.swl.engine.generator.javaee.model;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.QualifiedClassName;
import ro.swl.engine.generator.model.Type;


public class EntityType extends Type {

	public EntityType(String collectionType, String genericType, String pkg) throws GenerateException {
		super(collectionType + "<" + genericType + ">", pkg);
		clsName = new QualifiedClassName("java.util." + collectionType + "<" + genericType + ">");
	}


	public EntityType(String declaredName, String pkg) throws GenerateException {
		super(declaredName, pkg);
	}


	public boolean isDate() {
		return getName().equals("Date");
	}


	public boolean isBlob() {
		return getName().equals("Blob");
	}


	public boolean isInteger() {
		return getName().equals("Integer") || getName().equals("int");
	}


	public boolean isDouble() {
		return getName().equals("Double") || getName().equals("double");
	}


	public boolean isLong() {
		return getName().equals("Long") || getName().equals("long");
	}

}
