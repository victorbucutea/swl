package ro.swl.engine.generator.javaee.model;

import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_ONE;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_ONE;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.parser.ASTProperty;



public class EntityField extends Field {

	public EntityField(ASTProperty prop, String package1) throws CreateException {
		super(prop, package1);
	}


	public EntityField(String type, String name, String pkg) throws CreateException {
		super(name, type, pkg);
	}


	@Override
	protected Type initFieldType(String type, String pkg) throws CreateException {
		if (modelProp != null) {
			if (modelProp.isCollection()) {
				return new EntityType(modelProp.getType(), modelProp.getCollectionType(), pkg);
			} else {
				return new EntityType(modelProp.getType(), pkg);
			}
		} else {
			return new EntityType(type, pkg);
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
		return "Date".equals(getType().getSwlName());
	}


	public boolean isBlob() {
		return "Blob".equals(getType().getSwlName());
	}


}
