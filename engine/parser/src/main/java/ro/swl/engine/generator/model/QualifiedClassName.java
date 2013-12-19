package ro.swl.engine.generator.model;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.javaee.exception.EmptyFqNameException;
import ro.swl.engine.generator.javaee.exception.InvalidPackageException;


/**
 * a Fully Qualified Class Name (e.g
 * javax.persistence.Temporal,ava.util.Set<SomeClass>). It provides utility
 * methods for retrieving the package class name and import statement
 * 
 * For compatibility with simple types it also accepts non fully qualified names
 * ( e.g. SomeClass )
 * 
 * @author VictorBucutea
 * 
 */
public class QualifiedClassName {

	private String pkg;

	private String name;

	private String parameterType;


	public QualifiedClassName(String fqName) throws GenerateException {

		if (isEmpty(fqName))
			throw new EmptyFqNameException();

		int firstCapIdx = indexOfCapLetter(fqName);

		if (!fqName.contains(".")) {
			// it's a simple name 
			this.name = fqName;
			return;
		}

		if (firstCapIdx == 0) {
			this.name = fqName;
			return;
		}


		String strUpToFirstCap = fqName.substring(0, firstCapIdx - 1);

		if (!strUpToFirstCap.contains(".")) {
			// this is no package declaration
			name = fqName;
			return;
		} else {
			pkg = strUpToFirstCap;
		}


		int firstAngBrkt = fqName.indexOf("<");
		if (firstAngBrkt == -1) {
			name = fqName.substring(firstCapIdx);
		} else {
			name = fqName.substring(firstCapIdx, firstAngBrkt);
			int lastAngBrkt = fqName.lastIndexOf(">");
			parameterType = fqName.substring(firstAngBrkt + 1, lastAngBrkt);

		}
	}


	public QualifiedClassName(String simpleName, String pkgName) throws GenerateException {
		if (isEmpty(simpleName))
			throw new EmptyFqNameException();

		if ((pkgName != null) && !pkgName.contains(".")) {
			throw new InvalidPackageException("Cannot have single level packages.");
		}


		this.name = simpleName;
		if (!isPrimitive()) {
			this.pkg = pkgName;
		}
	}


	private static int indexOfCapLetter(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				return i;
			}
		}
		return -1;
	}


	public String getFqName() {
		if (isEmpty(pkg))
			return name;

		return pkg + "." + name;
	}


	public String getImport() {
		if (isEmpty(pkg) || "java.lang".equals(pkg))
			return null;

		if (isInternalClass()) {
			int idx = getSimpleName().indexOf(".");
			return pkg + "." + getSimpleName().substring(0, idx);
		}

		return getFqName();
	}


	private boolean isInternalClass() {
		return getSimpleName().contains(".");
	}


	public String getSimpleName() {
		return name;
	}


	public String getParameterizedName() {
		if (parameterType == null)
			return name;

		return name + "<" + parameterType + ">";
	}


	public boolean isArray() {
		return name.contains("[]");
	}



	public boolean isObject() {
		return !isPrimitive() && !isArray();
	}



	public boolean isPrimitive() {
		return name.equals("int") || name.equals("long") || name.equals("byte") || name.equals("double")
				|| name.equals("char") || name.equals("Integer") || name.equals("Long") || name.equals("Character")
				|| name.equals("Double") || name.equals("Byte");
	}


	public String getParameterType() {
		return this.parameterType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((pkg == null) ? 0 : pkg.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QualifiedClassName other = (QualifiedClassName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pkg == null) {
			if (other.pkg != null)
				return false;
		} else if (!pkg.equals(other.pkg))
			return false;
		return true;
	}



}
