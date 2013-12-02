package ro.swl.engine.generator.javaee.enhancer;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.javaee.exception.NoOwningSideInRelation;
import ro.swl.engine.generator.javaee.exception.RelatedEntityNotFoundException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.generator.model.Annotation;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTEntity;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTProperty;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPATechnology extends Technology {


	static class JPAEntityEnhancer extends Enhancer<EntityResource> {


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
					findEntity(appModel, type.getParameter());

					field.addAnotation(oneToMany);

				} else if (type.isObject()) {
					String relatedClsName = type.getParameter() == null ? type.getSimpleClassName() : type
							.getParameter();
					ASTEntity relatedEntity = findEntity(appModel, relatedClsName);

					// iterate through fields of related entity to see whether we have a field with type = currentType 
					boolean isManyToOne = relatedEntity.getFieldCollectionOf(r.getName()) != null;

					if (!isManyToOne) {
						Annotation oneToOne = new Annotation("javax.persistence.OneToOne");
						field.addAnotation(oneToOne);

						if (field.isOwning()) {
							oneToOne.setProperty("cascade", "javax.persistence.CascadeType.ALL");
							Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
							joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
							field.addAnotation(joinColumn);
						} else {
							ASTProperty relationProp = relatedEntity.getFieldWithType(r.getName());
							if ((relationProp == null) || !relationProp.isOwning()) {
								throw new NoOwningSideInRelation(r.getName(), field.getName(), relatedEntity.getName());
							}
						}

					} else {
						Annotation manyToOne = new Annotation("javax.persistence.ManyToOne");
						field.addAnotation(manyToOne);
						Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
						joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
						field.addAnotation(joinColumn);
					}
				}


			}
		}


		public ASTEntity findEntity(ASTSwdlApp appModel, String fieldCls) throws GenerateException {
			for (ASTModule currentModule : appModel.getModules()) {
				ASTEntity entity = currentModule.findEntity(fieldCls);
				if (entity != null) {
					return entity;
				}
			}
			throw new RelatedEntityNotFoundException(fieldCls);
		}
	}

	static class JPAPomXmlEnhancer extends Enhancer<PomXml> {

		@Override
		public void enhance(ASTSwdlApp appModel, PomXml r, GenerationContext ctxt) throws GenerateException {

		}
	}


	public JPATechnology(GenerationContext ctxt) {
		super(ctxt);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		return asList(new JPAEntityEnhancer(), new JPAPomXmlEnhancer());
	}
}
