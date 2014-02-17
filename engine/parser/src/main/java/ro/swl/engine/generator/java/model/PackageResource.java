package ro.swl.engine.generator.java.model;

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


	public String getNamespace() {
		return namespace;
	}


	public String getNamespaceAsDir() {
		return namespace.replace('.', File.separatorChar);
	}


	@Override
	public String getOutputFilePath() {

		if (getParent() == null) {
			getNamespaceAsDir();
		}

		return getParent().getOutputFilePath() + File.separator + getNamespaceAsDir();
	}

}