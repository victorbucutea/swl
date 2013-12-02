package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;


public class FileResource extends Resource {

	public FileResource(Resource parent, File f) {
		super(parent, f);
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {
	}
}
