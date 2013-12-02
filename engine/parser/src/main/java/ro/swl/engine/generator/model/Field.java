package ro.swl.engine.generator.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.GenerateException;


public abstract class Field<T extends Type> extends ResourceProperty {


	private String name;

	private List<Annotation> annotations;

	private T type;


	public Field(String name, String type, String pkg) throws GenerateException {
		this.name = name;
		this.type = initFieldType(type, pkg);
		this.annotations = new ArrayList<Annotation>();
	}


	protected abstract T initFieldType(String type, String pkg) throws GenerateException;


	public String getName() {
		return name;
	}


	public void addAnotation(Annotation annotation) {
		this.annotations.add(annotation);
	}


	public void addAnotation(String fqAnnName) throws GenerateException {
		this.annotations.add(new Annotation(fqAnnName));
	}


	public List<Annotation> getAnnotations() {
		return annotations;
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

}
