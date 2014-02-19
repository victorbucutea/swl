package ro.swl.engine.generator.java.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.QualifiedClassName;


public class Type {

	public static Type VOID;

	protected static Map<String, String> internalTypes = new HashMap<String, String>();

	static {
		internalTypes.put("Blob", "byte[]");
		internalTypes.put("Date", "java.util.Date");
		internalTypes.put("int", "int");
		internalTypes.put("Integer", "java.lang.Integer");
		internalTypes.put("double", "double");
		internalTypes.put("Double", "java.lang.Double");
		internalTypes.put("long", "long");
		internalTypes.put("Long", "java.lang.Long");
		try {
			VOID = new Type("void");
		} catch (GenerateException e) {
			e.printStackTrace();
		}
	}

	protected String swlDeclaredName;

	protected QualifiedClassName clsName;


	public Type(String simpleName, String pkg) {

	}


	/**
	 * 
	 * @param name
	 * @throws GenerateException
	 */
	public Type(String fqName) throws GenerateException {
		this.clsName = new QualifiedClassName(fqName);
		this.swlDeclaredName = clsName.getSimpleName();
		String internalType = internalTypes.get(swlDeclaredName);
		if (internalType != null) {
			this.clsName = new QualifiedClassName(internalType);
		}
	}


	public String getSwlName() {
		return swlDeclaredName;
	}


	public String getSimpleClassName() {
		return clsName.getSimpleName();
	}


	public boolean isCollection() {
		return clsName.getSimpleName().contains("Set") || clsName.getSimpleName().contains("List");
	}


	public boolean isInternalType() {
		return false;
	}


	public String getParameter() {
		return clsName.getParameterType();
	}


	public String getParameterizedName() {
		return clsName.getParameterizedName();
	}


	public boolean isCollectionOf(String type) {
		if (!isCollection())
			return false;

		String paramType = clsName.getParameterType();

		if (paramType == null)
			return false;


		return type.equals(paramType);
	}


	public boolean isObject() {
		return clsName.isObject();
	}


	public Set<String> getImports() {
		return clsName.getImports();
	}


	public String getFqName() {
		return clsName.getFqName();
	}



	@Override
	public String toString() {
		return getParameterizedName();
	}

}
