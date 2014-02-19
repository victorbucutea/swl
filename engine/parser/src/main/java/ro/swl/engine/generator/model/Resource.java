package ro.swl.engine.generator.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public abstract class Resource {

	private Resource parent;
	private List<Resource> children = new ArrayList<Resource>();
	private File templateFile;
	private String outputFileName;
	private ResourceWriter writer;


	/**
	 * Constructor to use when there isn't any initial template file.
	 * Use when you want to create a {@link Resource} programmatically.
	 * 
	 * @param parent
	 * @param outputFileName
	 */
	public Resource(Resource parent, String outputFileName) {
		this(parent, new File(parent.getTemplateFile(), outputFileName));
	}


	public Resource(Resource parent, File template) {
		this.parent = parent;
		this.templateFile = template;
		if (template != null)
			this.outputFileName = template.getName();
		this.writer = initWriter();
	}


	protected ResourceWriter initWriter() {
		return new DefaultResourceWriter(this);
	}


	public void write(GenerationContext ctxt) throws WriteException {
		writeSelf(ctxt);
		writeChildren(ctxt);
	}


	protected void writeSelf(GenerationContext ctxt) throws WriteException {
		getWriter().write(ctxt);
	}


	protected void writeChildren(GenerationContext ctxt) throws WriteException {
		for (Resource child : children) {
			child.write(ctxt);
		}
	}


	public void addChild(Resource resource) {
		children.add(resource);
	}


	public File getTemplateFile() {
		return templateFile;
	}


	public Resource getParent() {
		return parent;
	}


	public String getOutputFilePath() {
		if (parent == null) {
			return getOutputFileName();
		}

		return parent.getOutputFilePath() + File.separator + getOutputFileName();
	}


	/**
	 * The name can come from 2 sources:
	 * 1. The setter was called during the generation phase, so the
	 * {@link #templateFile} has no correlation with this name
	 * 
	 * 3. The {@link #Resource(Resource, File)} was called and it was deduced
	 * from the {@link #templateFile}
	 * 
	 * @return
	 */
	public String getOutputFileName() {
		return outputFileName;
	}


	public void setOutputFileName(String name) {
		this.outputFileName = name;
	}


	public Resource getChild(int idx) {
		return children.get(idx);
	}


	@SuppressWarnings("unchecked")
	public <T extends Resource> T getChildCast(int idx) {
		return (T) children.get(idx);
	}


	public List<Resource> getChildren() {
		return children;
	}


	public void addChildren(List<? extends Resource> children) {
		for (Resource res : children) {
			addChild(res);
		}
	}


	public void registerStateInContext(GenerationContext ctxt) {

	}


	public void unregisterStateInContext(GenerationContext ctxt) {

	}


	@Override
	public String toString() {
		return templateFile.getName();
	}



	public ResourceWriter getWriter() {
		return writer;
	}



	public void setWriter(ResourceWriter writer) {
		this.writer = writer;
	}



}
