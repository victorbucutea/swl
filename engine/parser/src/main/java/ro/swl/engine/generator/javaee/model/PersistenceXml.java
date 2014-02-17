package ro.swl.engine.generator.javaee.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ro.swl.engine.generator.model.Resource;



public class PersistenceXml extends Resource {

	private String provider;

	private Map<String, String> persistenceProperties = new HashMap<String, String>();


	public PersistenceXml(Resource parent, File templateFile) {
		super(parent, templateFile);
	}


	public void addPersistenceProperty(String key, String value) {
		persistenceProperties.put(key, value);
	}



	public String getProvider() {
		return provider;
	}



	public void setPersistenceProvider(String provider) {
		this.provider = provider;
	}

}
