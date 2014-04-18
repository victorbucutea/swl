package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.writer.ResourceWriter;
import ro.swl.engine.writer.VelocityTemplateWriter;


public class FileResource extends Resource {

	public FileResource(Resource parent, String outputFileName, boolean isDir) {
		super(parent, outputFileName, isDir);
	}


	public FileResource(Resource parent, File f) {
		super(parent, f);
	}


	@Override
	protected ResourceWriter createWriter(File sourceTemplate, boolean isDir) {
		return new VelocityTemplateWriter(sourceTemplate, this);
	}

}
