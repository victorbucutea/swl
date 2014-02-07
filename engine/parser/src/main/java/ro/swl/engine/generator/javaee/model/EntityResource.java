package ro.swl.engine.generator.javaee.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Annotation;
import ro.swl.engine.generator.model.Method;
import ro.swl.engine.generator.model.Resource;

import com.google.common.base.CaseFormat;


public class EntityResource extends Resource {

	private String name;

	private String pkg;

	private List<EntityField> props = new ArrayList<EntityField>();

	private List<Method> methods;

	private Set<Annotation> annotations;


	public EntityResource(Resource parent, File template, String pkg) {
		super(parent, template);
		this.pkg = pkg;
		annotations = new LinkedHashSet<Annotation>();
		methods = new ArrayList<Method>();
	}


	public void addEntityProperty(EntityField field) throws GenerateException {
		this.props.add(field);
	}


	public List<EntityField> getFields() {
		return this.props;
	}


	public EntityField getField(int idx) {
		return props.get(idx);
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {
	}


	@Override
	public String toString() {
		return name;
	}



	public String getPackage() {
		return pkg;
	}


	public void addMethod(Method m) {
		this.methods.add(m);
	}


	public String getLowerCamelName() {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getName());
	}



	public Set<Annotation> getAnnotations() {
		return annotations;
	}


	public void addAnnotation(String ann) throws GenerateException {
		this.annotations.add(new Annotation(ann));
	}


	public void addAnnotation(Annotation ann) {
		this.annotations.add(ann);
	}


	public List<Method> getMethods() {
		return methods;
	}


	public Method getMethod(String name) {
		for (Method m : methods) {
			if (m.getName().equals(name))
				return m;
		}

		return null;
	}
}
