package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.model.Resource;


public class ServiceResource extends JavaResource<Type, Field> {

	public static final String ID = "__service__";

	private String serviceName;
	private String originalFileName;


	public ServiceResource(Resource parent, File template, String pkg) {
		super(parent, template, pkg);
		this.originalFileName = template.getName();
	}


	public void setServiceName(String image) {
		this.serviceName = image;
	}


	public String getServiceName() {
		return serviceName;
	}



	@Override
	public String getOutputFileName() {
		return originalFileName.replaceAll(ID, serviceName);
	}



	public String getOriginalFileName() {
		return originalFileName;
	}



	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

}
