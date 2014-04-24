package ro.swl.engine.generator.java.model;

import static com.google.common.collect.Sets.newHashSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.writer.JavaTemplateWriter;
import ro.swl.engine.writer.ResourceWriter;

import com.google.common.base.CaseFormat;


/**
 * 
 * @author VictorBucutea
 * 
 * 
 * @param <F>
 *            A sublclass of {@link AbstractField}. It provides additional
 *            functionality than the basic {@link AbstractField}.
 *            e.g {@link EntityField} which can have relation types
 *            {@link EntityField#isManyToOne()} , ...
 */
public class JavaResource<F extends AbstractField> extends Resource {

	private String name;

	private String pkg;

	private Type superClass;

	private List<F> props = new ArrayList<F>();

	private List<F> staticFinalProps = new ArrayList<F>();

	private List<Method> methods = new ArrayList<Method>();

	private List<Annotation> annotations = new ArrayList<Annotation>();


	public JavaResource(Resource parent, String name, String pkg) {
		super(parent, name, false);
		this.name = name;
		this.pkg = pkg;
	}


	public JavaResource(Resource parent, File template, String pkg) {
		super(parent, template);
		this.pkg = pkg;
	}


	public void addField(F field) throws CreateException {
		this.props.add(field);
	}


	public void addStaticFinalProperty(F field) throws CreateException {
		field.setHasGetter(false);
		field.setHasSetter(false);
		this.staticFinalProps.add(field);
	}


	public void setSuperClass(String fqSuperClassName) throws CreateException {
		this.superClass = new Type(fqSuperClassName);
	}


	public List<F> getStaticFinalProperties() {
		return this.staticFinalProps;
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
	protected ResourceWriter createWriter(File sourceTemplate, boolean isDir) {
		return new JavaTemplateWriter(this);
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


	public String getLowerCaseName() {
		return getName().toLowerCase();
	}


	public List<Annotation> getAnnotations() {
		return annotations;
	}


	public void addAnnotation(String ann) throws CreateException {
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


	public Set<String> getImports() {
		Set<String> imports = newHashSet();

		for (Method m : getMethods()) {
			imports.addAll(m.getImports());
		}

		for (Annotation ann : getAnnotations()) {
			imports.addAll(ann.getImports());
		}

		for (F f : getFields()) {
			imports.addAll(f.getImports());
		}

		if (superClass != null)
			imports.addAll(superClass.getImports());

		return imports;
	}


	@Override
	public String getOutputFileName() {
		return getName() + ".java";
	}


	public boolean isSubClass() {
		return superClass != null;
	}


	public Type getSuperClass() {
		return superClass;
	}
}
