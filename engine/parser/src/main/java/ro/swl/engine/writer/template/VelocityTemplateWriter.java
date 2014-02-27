package ro.swl.engine.writer.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import ro.swl.engine.generator.model.Resource;



public class VelocityTemplateWriter extends DefaultResourceWriter {

	private Template velocityTemplate;

	private Context context;


	public VelocityTemplateWriter(String template, Resource model) {
		super(null, model, false);
		initVelocity();
		this.velocityTemplate = Velocity.getTemplate(template);
		this.context = new VelocityContext();
		context.put("model", model);
	}


	protected void initVelocity() {
		Properties props = new Properties();
		props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init(props);
	}


	@Override
	protected void internalWrite(File destinationFile) throws FileNotFoundException, IOException {
		Writer writer = new FileWriter(destinationFile);
		velocityTemplate.merge(context, writer);
		writer.close();
	}


	/**
	 * Provides the relation between {@link VelocityContext} and our
	 * {@link Resource} model.
	 * 
	 * In other words it provides a way for Velocity templates to refer
	 * properties
	 * of the model
	 * without prepending 'model.'
	 * 
	 * e.g Prop className from model $className vs $model.className
	 * 
	 * 
	 * @author VictorBucutea
	 * 
	 */
	public static class TemplateContext extends VelocityContext {

		public TemplateContext(Resource resource) {
			put("model", resource);
		}


		@Override
		public Object get(String key) {
			return super.get("model." + key);
		}


		@Override
		public boolean containsKey(Object key) {
			return super.containsKey("model." + key);
		}



	}

}
