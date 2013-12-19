package ro.swl.engine.generator.model;

import ro.swl.engine.generator.GenerateException;


public class Type {


	protected String swlDeclaredName;

	protected QualifiedClassName clsName;


	public Type(String name, String pkg) throws GenerateException {
		this.swlDeclaredName = name;
		this.clsName = new QualifiedClassName(name, pkg);
	}


	public String getName() {
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


	public String getImport() {
		return clsName.getImport();
	}


	public String getFqName() {
		return clsName.getFqName();
	}



	@Override
	public String toString() {
		return getImport();
	}

}