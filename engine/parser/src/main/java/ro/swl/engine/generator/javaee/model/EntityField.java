package ro.swl.engine.generator.javaee.model;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.Field;

import com.google.common.base.CaseFormat;


public class EntityField extends Field<EntityType> {


	public boolean owning;


	public EntityField(String name, String type, String pkg) throws GenerateException {
		super(name, type, pkg);
	}


	@Override
	protected EntityType initFieldType(String type, String pkg) throws GenerateException {
		return new EntityType(type, pkg);
	}



	public String getUpperUnderscoreName() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, getName());
	}



	public boolean isOwning() {
		return owning;
	}



	public void setOwning(boolean owning) {
		this.owning = owning;
	}


}
