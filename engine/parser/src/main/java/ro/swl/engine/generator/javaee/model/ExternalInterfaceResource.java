package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTExternalInterface;


public class ExternalInterfaceResource extends JavaResource<Field> {

	private ASTExternalInterface model;


	public ExternalInterfaceResource(Resource parent, File template, String pkg, ASTExternalInterface model) {
		super(parent, template, pkg);
		setName(model.getName());
		this.model = model;
	}


	public String getType() {
		return model.getType();
	}


	public ASTExternalInterface getModel() {
		return model;
	}

}
