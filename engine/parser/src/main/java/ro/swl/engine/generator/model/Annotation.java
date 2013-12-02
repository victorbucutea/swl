package ro.swl.engine.generator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.swl.engine.generator.GenerateException;


public class Annotation {

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


	public void setProperty(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationProperty(value));
	}


	public void setPropertyLiteral(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationProperty(value, true));
	}


	public List<String> getImports() {
		List<String> imports = new ArrayList<String>();
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

}
