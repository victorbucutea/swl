package ro.swl.engine.generator.javaee.model;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.File;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.model.Resource;


public class EntityResource extends JavaResource<EntityField> {

	public EntityResource(Resource parent, File template, String pkg) throws CreateException {
		super(parent, template, pkg);
		Annotation entity = new Annotation("javax.persistence.Entity");
		addAnnotation(entity);
		String defaultPackage = getGlobalCtxt().getDefaultPackage();
		setSuperClass(defaultPackage + ".base.model.EntityBase");
	}


}
