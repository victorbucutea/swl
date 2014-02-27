package ro.swl.engine.generator.java.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;


public class PackageResource extends Resource {

	private String namespace;


	/**
	 * Create PackageResource from a namespace ( e.g. 'ro.sft.somepackage')
	 * 
	 * It will have the file output name ro/sft/somepackage
	 * 
	 * @param parent
	 * @param namespace
	 */
	public PackageResource(Resource parent, String namespace) {
		super(parent, namespace.replace('.', File.separatorChar), true);
		this.namespace = namespace;
	}


	public PackageResource(Resource parent, File template, String namespace) {
		super(parent, template);
		this.namespace = namespace;
	}


	@Override
	public void registerState(GenerationContext ctxt) {
		super.registerState(ctxt);
		ctxt.setCurrentPackage(namespace);
	}


	@Override
	public void unregisterState(GenerationContext ctxt) {
		ctxt.setCurrentPackage("");
	}


	public String getNamespace() {
		return namespace;
	}


	@Override
	public String getOutputFileName() {
		return getNamespaceAsDir();
	}


	public String getNamespaceAsDir() {
		return namespace.replace('.', File.separatorChar);
	}


}