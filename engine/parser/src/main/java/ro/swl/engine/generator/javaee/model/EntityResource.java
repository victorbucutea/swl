package ro.swl.engine.generator.javaee.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;


public class EntityResource extends Resource {

	private String name;

	private String pkg;

	private List<EntityField> props = new ArrayList<EntityField>();


	public EntityResource(Resource parent, File template, String pkg) {
		super(parent, template);
		this.pkg = pkg;
	}


	public void addEntityProperty(String name, String type, boolean owning) throws GenerateException {
		EntityField field = new EntityField(name, type, pkg);
		field.setOwning(owning);
		this.props.add(field);
	}


	public List<EntityField> getFields() {
		return this.props;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	@Override
	protected void writeSelf(GenerationContext ctxt) {
	}


	@Override
	public String toString() {
		return name;
	}



	public String getPackage() {
		return pkg;
	}


}
