package ro.swl.engine.generator.javaee.enhancer;

import java.util.HashSet;
import java.util.Set;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.generator.java.model.CompoundStatement;
import ro.swl.engine.generator.java.model.ForStatement;
import ro.swl.engine.generator.java.model.IfStatement;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAHelperMethodEnhancer extends Enhancer<EntityResource> {



	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource entity) throws GenerateException {


		for (AbstractField<EntityType> field : entity.getFields()) {
			if (!field.getType().isCollection()) {
				continue;
			}


			Method m = new Method("add" + field.getUpperCamelName());
			m.getBody().addAll(getAdderStmts(entity, field, "objcts"));
			entity.addMethod(m);

		}
	}


	private Set<Statement> getAdderStmts(EntityResource res, AbstractField<EntityType> field, String paramName) {
		Set<Statement> stmts = new HashSet<Statement>();
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
		CompoundStatement nullChk = new IfStatement(paramName + " == null");
		nullChk.addChildStmt(new Statement("return", ""));


		CompoundStatement initSet = new IfStatement(field.getUpperCamelName() + " == null");
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
