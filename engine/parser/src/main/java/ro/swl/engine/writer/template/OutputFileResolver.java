package ro.swl.engine.writer.template;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;



public interface OutputFileResolver {

	public String getOutputFileName();


	public String getOutputFilePath();


	public File getOutputFile(GenerationContext ctxt);

}