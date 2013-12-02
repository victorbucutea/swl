package ro.swl.engine.generator.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.TemplateProvider;
import ro.swl.engine.generator.VelocityTemplateProvider;


/**
 * Model for a project 'Resource'.
 * 
 * 
 * @author VictorBucutea
 * 
 */
public abstract class Resource {

	private Resource parent;
	private List<Resource> children = new ArrayList<Resource>();
	private TemplateProvider templateProvider = new VelocityTemplateProvider();
	private File templateFile;
	private File outputFile;


	public Resource(Resource parent, File template) {
		this.parent = parent;
		this.templateFile = template;
	}


	public void write(GenerationContext ctxt) {
		writeSelf(ctxt);
		writeChildren(ctxt);
	}


	protected abstract void writeSelf(GenerationContext ctxt);


	protected void writeChildren(GenerationContext ctxt) {
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


	protected File getOutputFile() {
		return outputFile;
	}


	public void setOutputFileName(String name) {
		this.outputFile = new File(getParent().getOutputFile(), name);
	}


	public Resource getChild(int idx) {
		return children.get(idx);
	}


	public List<Resource> getChildren() {
		return children;
	}


	public void addChildren(List<? extends Resource> children) {
		for (Resource res : children) {
			addChild(res);
		}
	}



	public TemplateProvider getTemplateProvider() {
		return templateProvider;
	}


	public void registerStateInContext(GenerationContext ctxt) {

	}


	public void unregisterStateInContext(GenerationContext ctxt) {

	}


	@Override
	public String toString() {
		return templateFile.getName();
	}



}
