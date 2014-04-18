package ro.swl.engine.writer;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import ro.swl.engine.generator.model.Resource;



public class VelocityTemplateWriter extends DefaultResourceWriter {

	private Template velocityTemplate;

	private Context context;

	private VelocityEngine velocityEngine;


	public VelocityTemplateWriter(String templateInClassPath, Resource model) {
		super(null, model, false);
		this.velocityEngine = new VelocityEngine();
		initClasspathResourceLoader();
		initializeVelocityContext(model);
		this.velocityTemplate = velocityEngine.getTemplate(templateInClassPath);
	}


	public VelocityTemplateWriter(File templateFile, Resource model) {
		super(templateFile, model, false);
		this.velocityEngine = new VelocityEngine();
		initFileResourceLoader();
		initializeVelocityContext(model);
		try {

			if (templateFile != null)//template file can be null if resource is programmatically created
				this.velocityTemplate = velocityEngine.getTemplate(templateFile.getAbsolutePath());

		} catch (VelocityException ve) {
			System.err.println(templateFile.getAbsolutePath() + " cannot be parsed by velocity:" + ve.getMessage());
		}
	}


	private void initializeVelocityContext(Resource model) {
		this.context = new VelocityContext();
		context.put("model", model);
		addGlobalProperties();
	}


	private void addGlobalProperties() {
		Map<String, String> props = getGlobalCtxt().getProperties();
		for (String prop : props.keySet()) {
			context.put(prop, props.get(prop));
		}
		context.put("modules", getGlobalCtxt().getModules());
	}



	private void initFileResourceLoader() {
		Properties props = new Properties();
		props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		props.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
		props.setProperty("file.resource.loader.path", "");
		props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
		velocityEngine.init(props);
	}


	protected void initClasspathResourceLoader() {
		Properties props = new Properties();
		props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init(props);
	}


	@Override
	protected void preWrite(File destinationFile) {
		context.put("module", resource.getModuleName());
	}


	@Override
	protected void internalWrite(File destinationFile) throws FileNotFoundException, IOException {

		// some exception might have occurred or resource was created programatically
		if (velocityTemplate == null) {
			if (sourceFile != null)
				super.internalWrite(destinationFile);
			return;
		}

		Writer writer = new FileWriter(destinationFile);
		velocityTemplate.merge(context, writer);
		writer.close();
	}

}