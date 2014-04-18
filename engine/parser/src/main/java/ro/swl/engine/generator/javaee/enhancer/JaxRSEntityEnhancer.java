package ro.swl.engine.generator.javaee.enhancer;


import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.GlobalContext;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.CompoundStatement;
import ro.swl.engine.generator.java.model.IfStatement;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRSEntityEnhancer extends Enhancer<EntityResource> {


	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource entity) throws CreateException {


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
					throw new CreateException(
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
				// can only be a bidirectional one-to-one 
				if (field.isOwningInRelation()) {
					// owning side will be the back-reference
					field.addAnotation("org.codehaus.jackson.annotate.JsonBackReference");
				} else {
					field.addAnotation("org.codehaus.jackson.annotate.JsonIgnore");
					addSerializerMethod(entity, field);
					addDeserializerMethod(entity, field);
				}

			}

		}

	}


	private void addDeserializerMethod(EntityResource entity, EntityField field) throws CreateException {
		Annotation jsonProp = new Annotation("org.codehaus.jackson.annotate.JsonProperty");
		jsonProp.addPropertyLiteral("value", field.getName());
		field.addSetterAnnotation(jsonProp);

	}


	private void addSerializerMethod(EntityResource resource, EntityField field) throws CreateException {
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
		Type type = field.getType();
		m.setReturnType(type);

		String typeName = type.getSimpleClassName();
		String fieldClsName = null;
		if (type.isCollection()) {
			fieldClsName = type.getParameter();
		} else {
			fieldClsName = type.getSimpleClassName();
		}

		String fieldParamName = new Type(fieldClsName).getSimpleClassName();
		String fieldName = field.getName();

		Annotation jsonProp = new Annotation("org.codehaus.jackson.annotate.JsonProperty");
		jsonProp.addPropertyLiteral("value", fieldName);
		m.addAnnotation(jsonProp);
		m.addAnnotation("org.codehaus.jackson.annotate.JsonManagedReference");


		CompoundStatement initSet = new IfStatement(field.getName() + " == null");

		if (typeName.equals("Set")) {
			String importStmt = "java.util.HashSet";
			initSet.addChildStmt(new Statement("return new HashSet<" + fieldParamName + ">()", importStmt));
		} else if (typeName.equals("List")) {
			String importStmt = "java.util.ArrayList";
			initSet.addChildStmt(new Statement("return new ArrayList<" + fieldParamName + ">()", importStmt));
		} else {
			initSet.addChildStmt(new Statement("return null"));
		}

		CompoundStatement isInitIf = new IfStatement("PersistenceUtil.isinitialized(" + fieldName + ")");
		String pkg = GlobalContext.getGlobalCtxt().getDefaultPackage();
		isInitIf.addChildStmt(new Statement("return " + fieldName, pkg + ".base.util.PersistenceUtil"));


		Statement returnStmt = new Statement();
		if (typeName.equals("Set")) {
			returnStmt.setStatement("return new HashSet<" + fieldParamName + ">()");
		} else if (typeName.equals("List")) {
			returnStmt.setStatement("return  new ArrayList<" + fieldParamName + ">()");
		} else {
			returnStmt.setStatement("return null");
		}


		List<Statement> stmts = new ArrayList<Statement>();
		stmts.add(initSet);
		stmts.add(isInitIf);
		stmts.add(returnStmt);

		m.getBody().addAll(stmts);

		resource.addMethod(m);


	}
}
