package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.generator.CreationContext;
import ro.swl.engine.writer.DefaultResourceWriter;
import ro.swl.engine.writer.ResourceWriter;
import ro.swl.engine.writer.ui.WriteException;


/**
 * Model for a project 'Resource'.
 * 
 * It is the base class for the Resource tree structure. It will provide a
 * default writer (responsible for writing the resource to disk) along with the
 * default output file name.
 * 
 * Subclasses can override {@link #write()} method to
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
	public void write() throws WriteException {
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
	public void registerState(CreationContext ctxt) {
		this.parentModuleName = ctxt.getCurrentModule();
	}


	public String getModuleName() {
		return parentModuleName;
	}


	@Override
	public String toString() {
		return getOutputFilePath() + " (" + getClass().getSimpleName() + ")";
	}



}
