package ro.swl.engine.generator;

import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.MANY_TO_ONE;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_MANY;
import static ro.swl.engine.generator.javaee.model.Relation.ONE_TO_ONE;

import java.util.List;

import ro.swl.engine.generator.javaee.exception.DuplicateDeclaredRelation;
import ro.swl.engine.generator.javaee.exception.RelatedEntityNotFoundException;
import ro.swl.engine.generator.javaee.exception.RelatedFieldNotFoundException;
import ro.swl.engine.generator.javaee.exception.WrongRelatedFieldTypeException;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTSwdlApp;


public class RelationsEnhancer extends Enhancer<ProjectRoot> {


	@Override
	public void enhance(ASTSwdlApp appModel, ProjectRoot r, GenerationContext ctxt) throws GenerateException {


		List<ASTEntity> entities = appModel.getChildNodesOfType(ASTEntity.class, true);

		for (ASTEntity entity : entities) {

			for (ASTProperty field : entity.getFields()) {

				if (field.isPrimitive()) {
					continue;
				}


				if (!field.hasDeclaredRelatedField()) {
					continue;
				}

				if (field.hasRelation()) {
					continue;
				}

				if (field.isXToMany()) {
					ASTProperty relatedField = findRelatedField(appModel, entity, field, field.getCollectionType());
					field.setRelatedField(relatedField);

					if (relatedField.isXToMany()) {
						field.setRelationType(MANY_TO_MANY);
						relatedField.setRelationType(MANY_TO_MANY);
						setRelationsAndDirection(field, relatedField);
						field.setOwning(true);
						continue;
					}

					if (relatedField.isXToOne()) {
						field.setRelationType(ONE_TO_MANY);
						relatedField.setRelationType(MANY_TO_ONE);
						setRelationsAndDirection(field, relatedField);
						relatedField.setOwning(true);
						continue;
					}
				}

				if (field.isXToOne()) {
					ASTProperty relatedField = findRelatedField(appModel, entity, field, field.getType());

					field.setRelatedField(relatedField);

					if (relatedField.isXToMany()) {
						field.setRelationType(MANY_TO_ONE);
						field.setOwning(true);// many-to-one is the owning part
						relatedField.setRelationType(ONE_TO_MANY);
						setRelationsAndDirection(field, relatedField);
						continue;
					}

					if (relatedField.isXToOne()) {
						field.setRelationType(ONE_TO_ONE);
						relatedField.setRelationType(ONE_TO_ONE);
						setRelationsAndDirection(field, relatedField);
						field.setOwning(true);
						continue;
					}
				}
			}
		}

		for (ASTEntity entity : entities) {

			for (ASTProperty field : entity.getFields()) {

				if (field.hasDeclaredRelatedField()) {
					continue;
				}

				if (field.hasRelation()) {
					continue;
				}

				if (field.isUnidirManyToOne()) {
					field.setRelationType(MANY_TO_ONE);
					field.setOwning(true);
					continue;
				}

				if (field.isUnidirOneToOne()) {
					field.setRelationType(ONE_TO_ONE);
					field.setOwning(true);
					continue;
				}

				if (field.isUnidirOneToMany()) {
					field.setRelationType(ONE_TO_MANY);
					field.setOwning(true);
					continue;
				}

				if (field.isUnidirManyToMany()) {
					field.setRelationType(MANY_TO_MANY);
					field.setOwning(true);
					continue;
				}
			}
		}
	}


	private void setRelationsAndDirection(ASTProperty field, ASTProperty relatedField) {
		relatedField.setRelatedField(field);
		relatedField.setBidirectional(true);
		field.setRelatedField(relatedField);
		field.setBidirectional(true);
	}


	private ASTProperty findRelatedField(ASTSwdlApp appModel, ASTEntity entity, ASTProperty field, String fieldType)
			throws GenerateException {
		ASTEntity relatedEntity = findEntity(appModel, fieldType);
		ASTProperty relatedField = relatedEntity.getFieldWithName(field.getRelatedPropertyName());
		String relatedEntityName = relatedEntity.getName();
		String entityName = entity.getName();
		String fieldName = field.getName();

		if (relatedField == null) {
			throw new RelatedFieldNotFoundException(relatedEntityName, field.getRelatedPropertyName(), entityName);
		}

		String relatedFieldName = relatedField.getName();

		if (relatedField.hasDeclaredRelatedField() || relatedField.isMarkedWithStar()) {
			throw new DuplicateDeclaredRelation(entityName, relatedEntityName, fieldName, relatedFieldName);
		}


		if (relatedField.isCollection()) {
			if (!relatedField.getCollectionType().equals(entityName)) {
				throw new WrongRelatedFieldTypeException(fieldName, entityName, relatedEntityName, relatedFieldName);
			}
		} else {
			if (!relatedField.getType().equals(entityName)) {
				throw new WrongRelatedFieldTypeException(fieldName, entityName, relatedEntityName, relatedFieldName);
			}
		}

		return relatedField;
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

}
