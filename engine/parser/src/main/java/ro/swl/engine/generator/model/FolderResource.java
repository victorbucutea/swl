package ro.swl.engine.generator.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;


public class FolderResource extends Resource {

	public FolderResource(Resource parent, File f) {
		super(parent, f);
	}


	@Override
	public void registerStateInContext(GenerationContext ctxt) {

		if (isNotEmpty(ctxt.getCurrentPackage())) {
			// this folder is a package 
			String currPkg = ctxt.getCurrentPackage();
			ctxt.setCurrentPackage(currPkg + "." + getOutputFileName());
		}
	}


	@Override
	public void unregisterStateInContext(GenerationContext ctxt) {
		if (isNotEmpty(ctxt.getCurrentPackage())) {
			String currPkg = ctxt.getCurrentPackage().replace("." + getOutputFileName(), "");
			ctxt.setCurrentPackage(currPkg);
		}
	}

}
