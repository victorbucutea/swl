package ro.swl.engine.generator.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.model.ApplicationXml;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.generator.javaee.model.WebXml;


public class ProjectRoot extends Resource {

	private ApplicationXml applicationXml;
	private PersistenceXml persistenceXml;
	private WebXml webXml;


	public ProjectRoot(File templateFile) {
		super(null, templateFile);
	}


	public WebXml getWebXml() {
		return this.webXml;
	}


	public PersistenceXml getPersistenceXml() {
		return this.persistenceXml;
	}


	public ApplicationXml getApplicationXml() {
		return this.applicationXml;
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {
		// TODO generate project root folder
	}
}
