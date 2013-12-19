package ro.swl.engine.generator.javaee.enhancer;

import java.util.HashSet;
import java.util.Set;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.model.Method;
import ro.swl.engine.generator.model.Method.ForStatement;
import ro.swl.engine.generator.model.Method.IfStatement;
import ro.swl.engine.generator.model.Method.Statement;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAHelperMethodEnhancer extends Enhancer<EntityResource> {



	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource entity, GenerationContext ctxt) throws GenerateException {


		for (EntityField field : entity.getFields()) {
			if (!field.getType().isCollection()) {
				continue;
			}


			Method m = new Method("add" + field.getUpperCamelName());
			m.getBody().addAll(getAdderStmts(entity, field, "objcts"));
			entity.addMethod(m);

		}
	}


	private Set<Method.Statement> getAdderStmts(EntityResource res, EntityField field, String paramName) {
		Set<Method.Statement> stmts = new HashSet<Method.Statement>();
		/*
		 * if (certs == null) {
		 * return;
		 * }
		 * 
		 * if (certifications == null) {
		 * certifications = newHashSet();
		 * }
		 * 
		 * for (Certification cert : certs) {
		 * cert.setCv(this);
		 * certifications.add(cert);
		 * }
		 */
		IfStatement nullChk = new IfStatement(paramName + " == null");
		nullChk.addChildStmt(new Statement("return", ""));


		IfStatement initSet = new IfStatement(field.getUpperCamelName() + " == null");
		if (field.getType().equals("Set")) {
			String importStmt = "java.util.HashSet";
			initSet.addChildStmt(new Statement(field.getName() + " = new HashSet<"
					+ field.getType().getSimpleClassName() + ">()", importStmt));

		} else if (field.getType().equals("List")) {
			String importStmt = "java.util.ArrayList";
			initSet.addChildStmt(new Statement(field.getName() + " = new ArrayList<"
					+ field.getType().getSimpleClassName() + ">()", importStmt));
		}


		Statement forExpr = new Statement(paramName, "");
		ForStatement stmt = new ForStatement(field.getType(), "obj", forExpr);

		stmt.addChildStmt(new Statement("obj.set" + res.getLowerCamelName() + "(this)", ""));
		stmt.addChildStmt(new Statement(field.getName() + ".add(obj)", ""));


		stmts.add(nullChk);
		stmts.add(initSet);
		stmts.add(forExpr);

		return stmts;
	}
}