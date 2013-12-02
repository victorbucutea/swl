package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;


public class FolderResource extends Resource {

	public FolderResource(Resource parent, File f) {
		super(parent, f);
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {

	}


}
