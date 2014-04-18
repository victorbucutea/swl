package ro.swl.engine.generator.model;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Set;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.javaee.exception.EmptyFqNameException;
import ro.swl.engine.generator.javaee.exception.InvalidFqNameException;
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

	private QualifiedClassName parameterType;


	public QualifiedClassName(String fqName) throws CreateException {

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

		if (firstCapIdx == -1) {
			throw new InvalidFqNameException(fqName);
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
			parameterType = new QualifiedClassName(fqName.substring(firstAngBrkt + 1, lastAngBrkt));

		}
	}


	public QualifiedClassName(String simpleName, String pkgName) throws CreateException {
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


	public Set<String> getImports() {
		Set<String> imports = newHashSet();

		if (isEmpty(pkg) || "java.lang".equals(pkg))
			return imports;

		if (isInternalClass()) {
			int idx = getSimpleName().indexOf(".");
			imports.add(pkg + "." + getSimpleName().substring(0, idx));
		} else {
			imports.add(getFqName());
		}

		if (parameterType != null) {
			imports.addAll(parameterType.getImports());
		}

		return imports;
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

		return name + "<" + parameterType.getSimpleName() + ">";
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
		if (this.parameterType == null) {
			return null;
		}
		return this.parameterType.getSimpleName();
	}


	public String getFqParameterType() {
		if (this.parameterType == null) {
			return null;
		}
		return this.parameterType.getFqName();
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
