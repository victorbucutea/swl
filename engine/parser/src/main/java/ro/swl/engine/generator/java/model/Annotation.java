package ro.swl.engine.generator.java.model;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.QualifiedClassName;


public class Annotation implements Comparable<Annotation> {

	private static final String COMMA = " , ";
	private QualifiedClassName fqName;
	private Map<String, AnnotationPropertyValue> attributes;


	public Annotation(String string) throws GenerateException {
		this.fqName = new QualifiedClassName(string);
		attributes = new HashMap<String, AnnotationPropertyValue>();
	}



	public String getFqName() {
		return fqName.getFqName();
	}


	public String getSimpleName() {
		return fqName.getSimpleName();
	}


	public void addProperty(String name, String value) throws GenerateException {
		if (attributes.containsKey(name)) {
			AnnotationPropertyValue prop = attributes.get(name);
			prop.addTypeValue(value);
		} else {
			attributes.put(name, new AnnotationPropertyValue(value));
		}
	}


	public void removeProperty(String name) {
		attributes.remove(name);
	}


	public void setProperty(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationPropertyValue(value));
	}


	public void addPropertyLiteral(String name, String value) throws GenerateException {
		if (attributes.containsKey(name)) {
			AnnotationPropertyValue prop = attributes.get(name);
			prop.addValueLiteral(value);
		} else {
			attributes.put(name, new AnnotationPropertyValue(value, true));
		}
	}


	public void setPropertyLiteral(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationPropertyValue(value, true));
	}


	public void addPropertyAnnotation(String name, String value) throws GenerateException {
		if (attributes.containsKey(name)) {
			AnnotationPropertyValue prop = attributes.get(name);
			prop.addAnnotationValue(value);
		} else {
			attributes.put(name, new AnnotationPropertyValue(value, true, true));
		}
	}


	public void addPropertyAnnotation(String name, Annotation value) throws GenerateException {
		if (attributes.containsKey(name)) {
			AnnotationPropertyValue prop = attributes.get(name);
			prop.addAnnotationValue(value);
		} else {
			attributes.put(name, new AnnotationPropertyValue(value));
		}
	}


	public void setPropertyAnnotation(String name, String value) throws GenerateException {
		attributes.put(name, new AnnotationPropertyValue(value, true, true));
	}


	public Set<String> getImports() {
		Set<String> imports = new HashSet<String>();
		imports.addAll(fqName.getImports());

		for (AnnotationPropertyValue prop : attributes.values()) {
			imports.addAll(prop.getImports());
		}

		return imports;
	}


	public AnnotationPropertyValue getAttribute(String name) {
		return attributes.get(name);
	}


	public Collection<Annotation.AnnotationPropertyValue> getAttributes() {
		return attributes.values();
	}



	public String toJavaRepresentation() {
		String name = "@" + getSimpleName();

		if (attributes.isEmpty()) {
			return name;
		} else {

			if (attributes.size() == 1) {
				String first = attributes.keySet().iterator().next();
				String attrRepr = attributes.get(first).toJavaRepresentation();

				if (first.equals("value")) {
					//we have something like @SomeAnn(SomeVal.PROP)
					return name + "(" + attrRepr + ")";
				} else {
					return name + "(" + first + " = " + attrRepr + ")";
				}

			} else {
				// we have something like @SomeAnn(someProp = {SomeVal.PROP, SomeVale.PROP2 })

				name += "(";
				for (Entry<String, AnnotationPropertyValue> prop : attributes.entrySet()) {
					name += prop.getKey() + " = " + prop.getValue().toJavaRepresentation() + COMMA;
				}
				name = removeEnd(name, COMMA);
				name += ")";
				return name;
			}
		}
	}


	@Override
	public String toString() {
		return "@" + fqName.getFqName() + attributes.keySet();
	}



	public static class AnnotationPropertyValue {

		private List<QualifiedClassName> typeValues = new ArrayList<QualifiedClassName>();

		private List<Annotation> annotationValues = new ArrayList<Annotation>();

		private List<String> literals = new ArrayList<String>();


		/**
		 * used to construct a Type annotation value
		 * 
		 * @param value
		 *            (e.g. CascadeType.ALL )
		 * @throws GenerateException
		 */
		public AnnotationPropertyValue(String value) throws GenerateException {
			addTypeValue(value);
		}



		/**
		 * used to construct a literal annotation value
		 * 
		 * @param value
		 *            (e.g. true, "Select j from CV" )
		 * @throws GenerateException
		 */
		public AnnotationPropertyValue(String value, boolean literal) {
			addValueLiteral(value);
		}


		/**
		 * Use to create an annotation annotationValue
		 * 
		 * @param value
		 *            e.g. @NamedQuery(...)
		 * @param annotation
		 * @param ann
		 * @throws GenerateException
		 */
		public AnnotationPropertyValue(String value, boolean annotation, boolean ann) throws GenerateException {
			addAnnotationValue(value);
		}


		/**
		 * Use to create an annotation annotationValue
		 * 
		 * @param value
		 *            e.g. @NamedQuery(...)
		 * @param annotation
		 * @param ann
		 * @throws GenerateException
		 */
		public AnnotationPropertyValue(Annotation value) {
			addAnnotationValue(value);
		}


		public void addAnnotationValue(String value) throws GenerateException {
			annotationValues.add(new Annotation(value));
		}


		public void addAnnotationValue(Annotation value) {
			annotationValues.add(value);
		}


		public Set<String> getImports() {
			Set<String> imports = newHashSet();
			for (QualifiedClassName name : typeValues) {
				imports.addAll(name.getImports());
			}

			for (Annotation ann : annotationValues) {
				imports.addAll(ann.getImports());
			}
			return imports;
		}


		public List<QualifiedClassName> getTypeValues() {
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


		public void addTypeValue(String value) throws GenerateException {
			typeValues.add(new QualifiedClassName(value));
		}


		/**
		 * 
		 * @return the code representation for this AnnotationProperty ( e.g.
		 *         {SomeVal.VAL, SomeVal.VAL2}
		 */
		public String toJavaRepresentation() {
			if (!typeValues.isEmpty()) {
				// we have an array of type properties
				if (typeValues.size() == 1) {
					return typeValues.get(0).getSimpleName();
				} else {
					String repr = "{";

					for (QualifiedClassName cname : typeValues) {
						repr += cname.getSimpleName() + COMMA;
					}
					repr = removeEnd(repr, COMMA);
					repr += "}";
					return repr;
				}
			} else if (!annotationValues.isEmpty()) {
				if (annotationValues.size() == 1) {
					return annotationValues.get(0).toJavaRepresentation();
				} else {
					String repr = "{";
					for (Annotation cname : annotationValues) {
						repr += cname.toJavaRepresentation() + COMMA;
					}
					repr = removeEnd(repr, COMMA);
					repr += "}";
					return repr;
				}
			} else if (!literals.isEmpty()) {
				if (literals.size() == 1) {
					return getLiteralString(literals.get(0));
				} else {
					String repr = "{";
					for (String literal : literals) {
						repr += getLiteralString(literal) + COMMA;
					}
					repr = removeEnd(repr, COMMA);
					repr += "}";
					return repr;
				}
			} else {
				return "";
			}
		}


		private String getLiteralString(String string) {
			try {
				return Integer.valueOf(string).toString();
			} catch (NumberFormatException nfe) {
				if ("true".equalsIgnoreCase(string)) {
					return "true";
				} else if ("false".equals(string)) {
					return "false";
				} else {
					return "\"" + string + "\"";
				}
			}
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
