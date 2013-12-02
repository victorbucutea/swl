package ro.swl.engine.generator.javaee.model;

import java.util.HashMap;
import java.util.Map;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.QualifiedClassName;
import ro.swl.engine.generator.model.Type;


public class EntityType extends Type {

	private static final Map<String, QualifiedClassName> internalTypes = new HashMap<String, QualifiedClassName>();

	static {
		try {
			//@formatter:off
			internalTypes.put("Blob",    new QualifiedClassName("byte[]"));
			internalTypes.put("Date",    new QualifiedClassName("java.util.Date"));
//			internalTypes.put("Integer", new QualifiedClassName("java.lang.Integer"));
//			internalTypes.put("int",     new QualifiedClassName("java.lang.Integer"));
//			internalTypes.put("Double",  new QualifiedClassName("java.lang.Double"));
//			internalTypes.put("double",  new QualifiedClassName("java.lang.Double"));
//			internalTypes.put("Long",    new QualifiedClassName("java.lang.Long"));
//			internalTypes.put("long",    new QualifiedClassName("java.lang.Long"));
			//@formatter:on
		} catch (GenerateException e) {
			e.printStackTrace();
		}
	}


	public EntityType(String declaredName, String pkg) throws GenerateException {
		super(declaredName, pkg);

		clsName = internalTypes.get(declaredName);

		if (clsName == null) {
			if (declaredName.contains("Set") || declaredName.contains("List")) {
				clsName = new QualifiedClassName("java.util." + declaredName);
			} else {
				clsName = new QualifiedClassName(declaredName, pkg);
			}
		}
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


	@Override
	public boolean isInternalType() {
		return internalTypes.containsKey(swlDeclaredName);
	}

}
