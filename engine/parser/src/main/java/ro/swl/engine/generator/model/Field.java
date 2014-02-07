package ro.swl.engine.generator.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.parser.ASTProperty;

import com.google.common.base.CaseFormat;


public abstract class Field<T extends Type> extends ResourceProperty {


	private String name;

	private Set<Annotation> annotations;

	private boolean hasGetter;

	private Set<Annotation> getterAnnotations;

	private boolean hasSetter;

	private Set<Annotation> setterAnnotations;

	private T type;

	private String pkg;

	protected ASTProperty modelProp;


	public Field(ASTProperty modelProp, String pkg) throws GenerateException {
		this.modelProp = modelProp;
		this.name = modelProp.getName();
		this.pkg = pkg;
		this.annotations = new LinkedHashSet<Annotation>();
		this.getterAnnotations = new LinkedHashSet<Annotation>();
		this.setterAnnotations = new LinkedHashSet<Annotation>();
		this.type = initFieldType(modelProp.getType(), pkg);
	}


	protected abstract T initFieldType(String type, String pkg) throws GenerateException;


	public String getName() {
		return name;
	}


	public void addAnotation(Annotation annotation) {
		this.annotations.add(annotation);
	}


	public void addGetterAnnotation(Annotation ann) {
		this.getterAnnotations.add(ann);
	}


	public void addSetterAnnotation(Annotation ann) {
		this.setterAnnotations.add(ann);
	}


	public void addAnotation(String fqAnnName) throws GenerateException {
		this.annotations.add(new Annotation(fqAnnName));
	}


	public void addGetterAnotation(String fqAnnName) throws GenerateException {
		this.getterAnnotations.add(new Annotation(fqAnnName));
	}


	public void addSetterAnnotation(String fqAnnName) throws GenerateException {
		this.setterAnnotations.add(new Annotation(fqAnnName));
	}


	public List<Annotation> getGetterAnnotations() {
		return new ArrayList<Annotation>(getterAnnotations);
	}


	public List<Annotation> getSetterAnnotations() {
		return new ArrayList<Annotation>(setterAnnotations);
	}


	public List<Annotation> getAnnotations() {
		return new ArrayList<Annotation>(annotations);
	}


	public T getType() {
		return type;
	}


	public void setType(T type) {
		this.type = type;
	}



	public List<String> getImports() {
		List<String> list = newArrayList();
		// add import for current field type
		list.add(type.getImport());

		for (Annotation ann : annotations) {
			list.addAll(ann.getImports());
		}


		return list;
	}


	@Override
	public String toString() {
		return getType() + " " + getName();
	}



	public boolean hasGetter() {
		return hasGetter;
	}



	public void setHasGetter(boolean hasGetter) {
		this.hasGetter = hasGetter;
	}



	public boolean hasSetter() {
		return hasSetter;
	}



	public void setHasSetter(boolean hasSetter) {
		this.hasSetter = hasSetter;
	}



	public String getPkg() {
		return pkg;
	}



	public void setPkg(String pkg) {
		this.pkg = pkg;
	}


	public String getUpperUnderscoreName() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, getName());
	}


	public String getUpperCamelName() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getName());
	}

}
