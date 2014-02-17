package ro.swl.engine.writer.template;

import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.Type;



/**
 * Template provider for writing Java classes from a {@link JavaResource} model
 * 
 * 
 * 
 * @author VictorBucutea
 * 
 */
public class JavaTemplateWriter extends VelocityTemplateWriter {

	public static final String JAVA_TEMPLATE = "/ro/swl/engine/writer/template/java.vm";


	public <T extends Type, F extends AbstractField<T>> JavaTemplateWriter(JavaResource<T, F> javaResource) {
		super(JAVA_TEMPLATE, javaResource);
	}

}
