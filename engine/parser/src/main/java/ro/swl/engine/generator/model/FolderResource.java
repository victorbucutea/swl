package ro.swl.engine.generator.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;


public class FolderResource extends FileResource {


	/**
	 * use for creating a {@link FolderResource} programmatically ( e.g. From
	 * enhancers)
	 * 
	 * @param parent
	 * @param outputFileName
	 */
	public FolderResource(Resource parent, String outputFileName) {
		super(parent, outputFileName, true);
	}



	public FolderResource(Resource parent, File f) {
		super(parent, f);
	}


	@Override
	public void registerState(GenerationContext ctxt) {
		super.registerState(ctxt);
		if (isNotEmpty(ctxt.getCurrentPackage())) {
			// this folder is part of a package 
			String currPkg = ctxt.getCurrentPackage();
			ctxt.setCurrentPackage(currPkg + "." + getOutputFileName());
		}
	}


	@Override
	public void unregisterState(GenerationContext ctxt) {
		if (isNotEmpty(ctxt.getCurrentPackage())) {
			String currPkg = ctxt.getCurrentPackage().replace("." + getOutputFileName(), "");
			ctxt.setCurrentPackage(currPkg);
		}
	}

}
