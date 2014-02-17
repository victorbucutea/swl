package ro.swl.engine.generator.java.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.writer.template.JavaTemplateWriter;
import ro.swl.engine.writer.template.ResourceWriter;

import com.google.common.base.CaseFormat;


/**
 * 
 * @author VictorBucutea
 * 
 * @param <T>
 *            A subclass of {@link Type}. It should provide additional
 *            functionality than the old {@link Type}.
 *            e.g. EntityType - which can be a 'Blob', a 'Set<Something', a
 *            'Date',etc.
 * 
 * @param <F>
 *            A sublclass of {@link AbstractField}. It provides additional
 *            functionality
 *            the the basic {@link AbstractField}.
 *            e.g {@link EntityField} which can have relation types
 *            {@link EntityField#isManyToOne()} , ...
 */
public class JavaResource<T extends Type, F extends AbstractField<T>> extends Resource {

	private String name;

	private String pkg;

	private List<F> props = new ArrayList<F>();

	private List<Method> methods;

	private Set<Annotation> annotations;


	public JavaResource(Resource parent, String name, String pkg) {
		super(parent, name + ".java");
		this.pkg = pkg;
		annotations = new LinkedHashSet<Annotation>();
		methods = new ArrayList<Method>();
	}


	public JavaResource(Resource parent, File template, String pkg) {
		super(parent, template);
		this.pkg = pkg;
		annotations = new LinkedHashSet<Annotation>();
		methods = new ArrayList<Method>();
	}


	public void addProperty(F field) throws GenerateException {
		this.props.add(field);
	}


	public List<F> getFields() {
		return this.props;
	}


	public F getField(int idx) {
		return props.get(idx);
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	@Override
	public String getOutputFileName() {
		return getName() + ".java";
	}


	@Override
	protected ResourceWriter initWriter() {
		return new JavaTemplateWriter(this);
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


	public List<String> getImports() {
		List<String> imports = new ArrayList<String>();

		for (Method m : getMethods()) {
			imports.addAll(m.getImports());
		}

		for (Annotation ann : getAnnotations()) {
			imports.addAll(ann.getImports());
		}

		for (F f : getFields()) {
			imports.addAll(f.getImports());
		}

		return imports;
	}
}
