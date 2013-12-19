package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;



public class PersistenceXml extends Resource {

	private String provider;


	public PersistenceXml(Resource parent, File templateFile) {
		super(parent, templateFile);
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {
		// TODO Auto-generated method stub

	}


	public void addPersistenceProvider(String string) {
		this.provider = string;
	}


	public void addPersistenceProperty(String string, String string2) {
		// TODO Auto-generated method stub

	}

}
