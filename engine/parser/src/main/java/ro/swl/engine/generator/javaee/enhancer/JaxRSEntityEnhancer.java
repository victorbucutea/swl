package ro.swl.engine.generator.javaee.enhancer;


import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.CompoundStatement;
import ro.swl.engine.generator.java.model.IfStatement;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRSEntityEnhancer extends Enhancer<EntityResource> {


	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource entity) throws GenerateException {


		for (EntityField field : entity.getFields()) {

			if (field.isUnidirectional()) {
				if (!field.isOneToOne()) {
					field.addAnotation("org.codehaus.jackson.annotate.JsonIgnore");
					addSerializerMethod(entity, field);
					addDeserializerMethod(entity, field);
				}
				continue;
			}

			if (field.isManyToMany()) {

				if (!field.isUnidirectional())
					throw new GenerateException(
							"Bidirectional Many to many relations are not supported by current Jackson implementation.");

				field.addAnotation("org.codehaus.jackson.annotate.JsonIgnore");
				addSerializerMethod(entity, field);
				addDeserializerMethod(entity, field);
				continue;
			}

			if (field.isManyToOne()) {
				field.addAnotation("org.codehaus.jackson.annotate.JsonBackReference");
				continue;
			}



			if (field.isOneToMany()) {
				field.addAnotation("org.codehaus.jackson.annotate.JsonIgnore");
				addSerializerMethod(entity, field);
				addDeserializerMethod(entity, field);
				continue;
			}


			if (field.getType().isObject()) {

				if (field.isOwningInRelation()) {
					// owning side will be the back-reference
					field.addAnotation("org.codehaus.jackson.annotate.JsonBackReference");
				} else {
					field.addAnotation("org.codehaus.jackson.annotate.JsonManagedReference");
				}

			}

		}

	}


	private void addDeserializerMethod(EntityResource entity, AbstractField<EntityType> field) throws GenerateException {
		Annotation jsonProp = new Annotation("org.codehaus.jackson.annotate.JsonProperty");
		jsonProp.addProperty("value", field.getName());
		field.addSetterAnnotation(jsonProp);

	}


	private void addSerializerMethod(EntityResource resource, AbstractField<EntityType> field) throws GenerateException {
		/*
		 * @JsonProperty("experiences")
		 * 
		 * @JsonManagedReference
		 * public Set<Experience> serializeExperiences() {
		 * if (experiences == null) {
		 * return new HashSet<Experience>();
		 * }
		 * 
		 * if (isinitialized(experiences)) {
		 * return experiences;
		 * }
		 * 
		 * return new HashSet<Experience>();
		 * }
		 */

		Method m = new Method("serialize" + field.getUpperCamelName(), null);
		EntityType type = field.getType();
		m.setReturnType(type);
		String splClsName = type.getSimpleClassName();
		String fieldName = field.getName();

		Annotation jsonProp = new Annotation("org.codehaus.jackson.annotate.JsonProperty");
		jsonProp.addProperty("value", fieldName);
		m.addAnnotation(jsonProp);
		m.addAnnotation("org.codehaus.jackson.annotate.JsonManagedReference");

		List<Statement> stmts = new ArrayList<Statement>();

		CompoundStatement initSet = new IfStatement(field.getUpperCamelName() + " == null");


		if (type.equals("Set")) {
			String importStmt = "java.util.HashSet";
			initSet.addChildStmt(new Statement("return new HashSet<" + splClsName + ">()", importStmt));
		} else if (type.equals("List")) {
			String importStmt = "java.util.ArrayList";
			initSet.addChildStmt(new Statement("return new ArrayList<" + splClsName + ">()", importStmt));
		}

		CompoundStatement isInitIf = new IfStatement("isinitialized(" + fieldName + ")");
		if (type.equals("Set")) {
			initSet.addChildStmt(new Statement("return " + fieldName, ""));
		} else if (type.equals("List")) {
			initSet.addChildStmt(new Statement("return " + fieldName, ""));
		}

		Statement returnStmt = new Statement();

		if (type.equals("Set")) {
			returnStmt.setStatement("return new HashSet<" + splClsName + ">()");
		} else if (type.equals("List")) {
			returnStmt.setStatement("return  new ArrayList<" + splClsName + ">()");
		}


		stmts.add(initSet);
		stmts.add(isInitIf);
		stmts.add(returnStmt);

		m.getBody().addAll(stmts);

		resource.addMethod(m);


	}
}
