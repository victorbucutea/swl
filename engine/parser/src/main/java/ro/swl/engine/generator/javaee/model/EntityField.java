package ro.swl.engine.generator.javaee.model;

import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.Field;
import ro.swl.engine.parser.ASTProperty;

import com.google.common.base.CaseFormat;


public class EntityField extends Field<EntityType> {


	private boolean manyToOne;
	private boolean oneToOne;
	private boolean oneToMany;


	public EntityField(ASTProperty prop, String package1) throws GenerateException {
		super(prop.getName(), prop.getType(), package1);
		setOneToOne(prop.isMarkedAsOneToOne());
		setManyToOne(prop.isMarkedAsManyToOne());
		setOneToMany(prop.isMarkedAsOneToMany());
	}


	@Override
	protected EntityType initFieldType(String type, String pkg) throws GenerateException {
		return new EntityType(type, pkg);
	}



	public String getUpperUnderscoreName() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, getName());
	}


	public String getUpperCamelName() {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getName());
	}


	public void setManyToOne(boolean markedAsManyToOne) {
		this.manyToOne = markedAsManyToOne;
	}


	public void setOneToMany(boolean markedAsOneToMany) {
		this.oneToMany = markedAsOneToMany;
	}


	public void setOneToOne(boolean markedAsOneToOne) {
		this.oneToOne = markedAsOneToOne;
	}



	public boolean isMarkedAsManyToOne() {
		return manyToOne;
	}



	public boolean isMarkedAsOneToOne() {
		return oneToOne;
	}



	public boolean isOneToMany() {
		return oneToMany;
	}


}
