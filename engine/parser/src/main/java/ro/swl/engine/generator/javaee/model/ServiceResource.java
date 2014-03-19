package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.model.Resource;


public class ServiceResource extends JavaResource<Field> {

	public static final String ID = "__service__";

	private String originalFileName;


	public ServiceResource(Resource parent, File template, String pkg) {
		super(parent, template, pkg);
		this.originalFileName = template.getName();
	}



	@Override
	public String getOutputFileName() {
		return originalFileName.replaceAll(ID, getName());
	}



	public String getOriginalFileName() {
		return originalFileName;
	}



	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

}
