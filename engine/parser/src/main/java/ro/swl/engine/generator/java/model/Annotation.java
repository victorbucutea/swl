package ro.swl.engine.generator.java.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.QualifiedClassName;


public class Annotation implements Comparable<Annotation> {

	private QualifiedClassName fqName;
	private Map<String, AnnotationProperty> attributes;


	public Annotation(String string) throws GenerateException {
		this.fqName = new QualifiedClassName(string);
		attributes = new HashMap<String, AnnotationProperty>();
	}



	public String getFqName() {
		return fqName.getFqName();
	}


	public String getSimpleName() {
		return fqName.getSimpleName();
	}


	public void addProperty(String name, String value) throws GenerateException {
		if (attributes.containsKey(name)) {
			AnnotationProperty prop = attributes.get(name);
			prop.addValue(value);
		} else {
			attributes.put(name, new AnnotationProperty(value));
		}
	}


	public void removeProperty(String name) {
		attributes.remove(name);
	}


	public void setProperty(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationProperty(value));
	}


	public void setPropertyLiteral(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationProperty(value, true));
	}


	public Set<String> getImports() {
		Set<String> imports = new HashSet<String>();
		imports.add(fqName.getImport());

		for (AnnotationProperty prop : attributes.values()) {
			imports.addAll(prop.getImports());
		}

		return imports;
	}


	public AnnotationProperty getAttribute(String name) {
		return attributes.get(name);
	}


	public Collection<Annotation.AnnotationProperty> getAttributes() {
		return attributes.values();
	}


	@Override
	public String toString() {
		return "@" + fqName.getFqName() + attributes.keySet();
	}



	public static class AnnotationProperty {

		private List<QualifiedClassName> typeValues = new ArrayList<QualifiedClassName>();

		private List<String> literals = new ArrayList<String>();


		public AnnotationProperty(String value) throws GenerateException {
			addValue(value);
		}


		public AnnotationProperty(String value, boolean literal) {
			addValueLiteral(value);
		}


		public List<String> getImports() {
			List<String> imports = new ArrayList<String>();
			for (QualifiedClassName name : typeValues) {
				imports.add(name.getImport());
			}
			return imports;
		}


		public List<QualifiedClassName> getValues() {
			return typeValues;
		}


		public List<String> getValueLiterals() {
			return literals;
		}


		public void addValue(QualifiedClassName value) {
			typeValues.add(value);
		}


		public void addValueLiteral(String value) {
			literals.add(value);
		}


		public void addValue(String value) throws GenerateException {
			typeValues.add(new QualifiedClassName(value));
		}

	}


	@Override
	public int compareTo(Annotation o) {
		return getFqName().compareTo(o.getFqName());
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((fqName == null) ? 0 : fqName.hashCode());
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
		Annotation other = (Annotation) obj;
		if (fqName == null) {
			if (other.fqName != null)
				return false;
		} else if (!fqName.equals(other.fqName))
			return false;
		return true;
	}


}
