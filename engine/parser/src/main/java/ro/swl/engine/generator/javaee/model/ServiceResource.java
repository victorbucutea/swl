package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.model.Resource;


public class ServiceResource extends JavaResource<Field> {

	public static final String ID = "__service__";



	public ServiceResource(Resource parent, File template, String pkg) {
		super(parent, template, pkg);
	}


}
