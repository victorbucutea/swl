package ro.swl.engine.writer.template;

import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.generator.java.model.JavaResource;



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


	public <F extends AbstractField> JavaTemplateWriter(JavaResource<F> javaResource) {
		super(JAVA_TEMPLATE, javaResource);
	}

}
