package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.model.Resource;



public class WebXml extends Resource {

	private String version = "3.0";

	private String versionSchema = "http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd";


	public WebXml(Resource parent, File f) {
		super(parent, f);
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}



	public String getVersionSchema() {
		return versionSchema;
	}



	public void setVersionSchema(String versionSchema) {
		this.versionSchema = versionSchema;
	}

}
