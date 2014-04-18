package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAEntityEnhancer extends Enhancer<EntityResource> {


	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource r) throws CreateException {

		for (EntityField field : r.getFields()) {

			if (field.isPrimitive()) {
				Annotation column = new Annotation("javax.persistence.Column");
				column.setPropertyLiteral("name", field.getUpperUnderscoreName());
				field.addAnotation(column);
				if (field.isDate()) {
					Annotation temporal = new Annotation("javax.persistence.Temporal");
					temporal.addProperty("value", "javax.persistence.TemporalType.TIMESTAMP");
					field.addAnotation(temporal);
				}
				continue;
			}


			if (field.isBlob()) {
				Annotation lob = new Annotation("javax.persistence.Lob");
				field.addAnotation(lob);
				continue;
			}

			if (field.isManyToMany()) {
				Annotation manyToMany = new Annotation("javax.persistence.ManyToMany");
				if (!field.isOwningInRelation()) {
					manyToMany.addProperty("cascade", "javax.persistence.CascadeType.ALL");
					manyToMany.setPropertyLiteral("orphanRemoval", "true");
				}
				field.addAnotation(manyToMany);
			}

			if (field.isOneToMany()) {
				Annotation oneToMany = new Annotation("javax.persistence.OneToMany");
				oneToMany.addProperty("cascade", "javax.persistence.CascadeType.ALL");
				oneToMany.setPropertyLiteral("orphanRemoval", "true");
				field.addAnotation(oneToMany);
			}

			if (field.isManyToOne()) {
				Annotation manyToOne = new Annotation("javax.persistence.ManyToOne");
				field.addAnotation(manyToOne);
			}

			if (field.isOneToOne()) {
				Annotation oneToOne = new Annotation("javax.persistence.OneToOne");
				oneToOne.setProperty("cascade", "javax.persistence.CascadeType.ALL");
				field.addAnotation(oneToOne);
			}


			addOwningAnnotation(field);
		}
	}


	private void addOwningAnnotation(EntityField field) throws CreateException {
		if (field.isOwningInRelation()) {
			Annotation joinColumn = new Annotation("javax.persistence.JoinColumn");
			joinColumn.setPropertyLiteral("name", field.getUpperUnderscoreName() + "_ID");
			field.addAnotation(joinColumn);
		}
	}
}