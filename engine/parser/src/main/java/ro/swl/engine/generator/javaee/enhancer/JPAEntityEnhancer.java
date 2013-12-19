package ro.swl.engine.generator.javaee.enhancer;

import java.util.HashSet;
import java.util.Set;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.exception.CardinalityUnkownException;
import ro.swl.engine.generator.javaee.exception.RelatedEntityNotFoundException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.generator.model.Annotation;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAEntityEnhancer extends Enhancer<EntityResource> {


	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource r, GenerationContext ctxt) throws GenerateException {
		Set<String> entityNames = new HashSet<String>();

		for (ASTModule module : appModel.getModules()) {
			entityNames.addAll(module.getDomain().getEntityNames());
		}

		for (EntityField field : r.getFields()) {

			EntityType type = field.getType();


			if (type.isDate()) {
				// if non-collection or relation field add annotation according to type
				Annotation column = new Annotation("javax.persistence.Column");
				column.setPropertyLiteral("name", field.getUpperUnderscoreName());
				field.addAnotation(column);
				Annotation temporal = new Annotation("javax.persistence.Temporal");
				temporal.addProperty("value", "javax.persistence.TemporalType.TIMESTAMP");
				field.addAnotation(temporal);

			} else if (type.isBlob()) {
				// if non-collection or relation field add annotation according to type
				Annotation column = new Annotation("javax.persistence.Column");
				column.setPropertyLiteral("name", field.getUpperUnderscoreName());
				field.addAnotation(column);

				field.addAnotation("javax.persistence.Lob");
				field.getType();
			} else if (type.isCollection()) {
				Annotation oneToMany = new Annotation("javax.persistence.OneToMany");
				oneToMany.addProperty("cascade", "javax.persistence.CascadeType.ALL");
				oneToMany.setPropertyLiteral("orphanRemoval", "true");
				findEntity(appModel, type);
				field.addAnotation(oneToMany);

			} else if (type.isObject()) {

				ASTEntity relatedEntity = findEntity(appModel, type);
				ASTEntity currentEntity = findEntity(appModel, r.getName());

				// iterate through fields of related entity to see whether we have a field with type = currentType 
				ASTProperty relatedCollectionField = relatedEntity.getUnassignedFieldCollectionOf(r.getName());
				ASTProperty currentField = currentEntity.getFieldWithName(field.getName());
				boolean isBidirManyToOne = false;

				// mark field as assigned to relations so it won't be assigned to another field of the same type
				if (relatedCollectionField != null) {
					relatedCollectionField.setAssignedToRelation(true);
					isBidirManyToOne = true;
				}

				if (isBidirManyToOne || field.isMarkedAsManyToOne()) {
					Annotation manyToOne = new Annotation("javax.persistence.ManyToOne");
					field.setManyToOne(true);
					field.addAnotation(manyToOne);
					Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
					joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
					field.addAnotation(joinColumn);
				} else {
					// we have a one to one relation
					Annotation oneToOne = new Annotation("javax.persistence.OneToOne");
					field.addAnotation(oneToOne);

					if (field.isMarkedAsOneToOne()) {
						oneToOne.setProperty("cascade", "javax.persistence.CascadeType.ALL");
						Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
						joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
						field.addAnotation(joinColumn);
					} else {
						// not marked explicitly as a one to one, we have to auto-detect its relation
						boolean isBidirOneToOne = false;
						ASTProperty relationProp = relatedEntity.getUnassignedFieldWithType(r.getName());
						if (relationProp != null) {
							isBidirOneToOne = true;
						}



						if (isBidirOneToOne && !relationProp.isMarkedAsOneToOne()) {
							relationProp.setAssignedToRelation(true);

							// assume that current field is the owning side
							oneToOne.setProperty("cascade", "javax.persistence.CascadeType.ALL");
							Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
							joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
							field.addAnotation(joinColumn);
						}

						if ((relationProp == null) && !currentField.isMarkedAsOneToOne()) {
							throw new CardinalityUnkownException(r.getName(), field.getName());
						}

						if (!relationProp.isMarkedAsOneToOne()) {
							// neither current nor relation is marked as one to one
							// assume current is owning
							currentField.setMarkedAsOneToOne(true);
						}

					}

				}
			}


		}
	}


	private ASTEntity findEntity(ASTSwdlApp appModel, String name) throws RelatedEntityNotFoundException {
		for (ASTModule currentModule : appModel.getModules()) {
			ASTEntity entity = currentModule.findEntity(name);
			if (entity != null) {
				return entity;
			}
		}
		throw new RelatedEntityNotFoundException(name);
	}


	public ASTEntity findEntity(ASTSwdlApp appModel, EntityType type) throws GenerateException {
		String relatedClsName = type.getParameter() == null ? type.getSimpleClassName() : type.getParameter();
		return findEntity(appModel, relatedClsName);

	}
}