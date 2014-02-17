package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.model.Resource;


public class EntityResource extends JavaResource<EntityType, EntityField> {

	public EntityResource(Resource parent, File template, String pkg) throws GenerateException {
		super(parent, template, pkg);
		Annotation entity = new Annotation("javax.persistence.Entity");
		addAnnotation(entity);
	}

}
