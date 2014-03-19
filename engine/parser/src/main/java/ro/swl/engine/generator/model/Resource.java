package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.writer.template.DefaultResourceWriter;
import ro.swl.engine.writer.template.ResourceWriter;
import ro.swl.engine.writer.ui.WriteException;


/**
 * Model for a project 'Resource'.
 * 
 * It is the base class for the Resource tree structure. It will provide a
 * default writer (responsible for writing the resource to disk) along with the
 * default output file name.
 * 
 * Subclasses can override {@link #writeSelf(GenerationContext)} or
 * {@link #writeChildren(GenerationContext)} methods
 * 
 * 
 * 
 * 
 * @author VictorBucutea
 * 
 */
public abstract class Resource extends BaseResource {

	private ResourceWriter writer;
	private String outputFileName;
	private String parentModuleName;


	/**
	 * Constructor to use when there isn't any initial template file.
	 * Use when you want to create a {@link Resource} programmatically.
	 * 
	 * @param parent
	 * @param outputFileName
	 */
	public Resource(Resource parent, String outputFileName, boolean isDir) {
		this.parent = parent;
		this.writer = createWriter(null, isDir);
		this.outputFileName = outputFileName;
	}


	public Resource(Resource parent, File sourceTemplate) {
		this.parent = parent;
		this.writer = createWriter(sourceTemplate, sourceTemplate.isDirectory());
		this.outputFileName = sourceTemplate.getName();
	}


	protected ResourceWriter createWriter(File sourceTemplate, boolean isDir) {
		return new DefaultResourceWriter(sourceTemplate, this, isDir);
	}


	@Override
	protected void writeSelf() throws WriteException {
		writer.write();
	}


	public String getOutputFileName() {
		return this.outputFileName;
	}


	public String getOutputFilePath() {
		if (getParent() == null) {
			return getOutputFileName();
		}

		return getParent().getOutputFilePath() + File.separator + getOutputFileName();
	}


	@Override
	public void registerState(GenerationContext ctxt) {
		this.parentModuleName = ctxt.getCurrentModule();
	}


	public String getModuleName() {
		return parentModuleName;
	}


	@Override
	public String toString() {
		return outputFileName + " (" + getClass().getSimpleName() + ")";
	}



}
