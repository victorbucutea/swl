package ro.swl.engine.generator.javaee.model;

import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_ONE;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_ONE;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.model.Field;
import ro.swl.engine.parser.ASTProperty;

import com.google.common.base.CaseFormat;


public class EntityField extends Field<EntityType> {

	private ASTProperty modelProp;


	public EntityField(ASTProperty prop, String package1) throws GenerateException {
		super(prop.getName(), prop.getType(), package1);
		this.modelProp = prop;
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



	public boolean isOneToMany() {
		return ONE_TO_MANY.equals(modelProp.getRelationType());
	}


	public boolean isOwningInRelation() {
		return modelProp.isOwning();
	}



	public boolean isManyToMany() {
		return MANY_TO_MANY.equals(modelProp.getRelationType());
	}



	public boolean isManyToOne() {
		return MANY_TO_ONE.equals(modelProp.getRelationType());
	}



	public boolean isOneToOne() {
		return ONE_TO_ONE.equals(modelProp.getRelationType());
	}


	public ASTProperty getModelProp() {
		return modelProp;
	}


	public boolean isUnidirectional() {
		return modelProp.isUnidirManyToOne() || modelProp.isUnidirOneToOne() || modelProp.isUnidirOneToMany()
				|| modelProp.isUnidirManyToMany();
	}


	public boolean isPrimitive() {
		return modelProp.isPrimitive();
	}


	public boolean isDate() {
		return "Date".equals(getType().getName());
	}


	public boolean isBlob() {
		return "Blob".equals(getType().getName());
	}


}
