package ro.swl.engine.generator.javaee.model;

import static ro.swl.engine.generator.GenerationContext.PACKAGE;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;


public class PackageResource extends Resource {

	private String namespace;


	public PackageResource(Resource parent, File template) {
		super(parent, template);
	}


	@Override
	public void registerStateInContext(GenerationContext ctxt) {
		namespace = ctxt.getGenerationProp(PACKAGE);
		ctxt.setCurrentPackage(ctxt.getGenerationProp(PACKAGE));
	}


	@Override
	public void unregisterStateInContext(GenerationContext ctxt) {
		ctxt.setCurrentPackage("");
	}



	@Override
	protected void writeSelf(GenerationContext ctxt) {

	}


	public String getNamespace() {
		return namespace;
	}

}
