package ro.swl.engine.generator.javaee.model;

import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_ONE;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_ONE;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.parser.ASTProperty;



public class EntityField extends AbstractField<EntityType> {

	public EntityField(ASTProperty prop, String package1) throws GenerateException {
		super(prop, package1);
	}


	@Override
	protected EntityType initFieldType(String type, String pkg) throws GenerateException {
		if (modelProp.isCollection()) {
			return new EntityType(modelProp.getType(), modelProp.getCollectionType(), pkg);
		} else {
			return new EntityType(modelProp.getType(), pkg);
		}
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
