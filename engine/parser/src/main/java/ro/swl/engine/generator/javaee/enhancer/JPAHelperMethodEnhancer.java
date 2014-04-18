package ro.swl.engine.generator.javaee.enhancer;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.java.model.CompoundStatement;
import ro.swl.engine.generator.java.model.ForStatement;
import ro.swl.engine.generator.java.model.IfStatement;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Method.Parameter;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class JPAHelperMethodEnhancer extends Enhancer<EntityResource> {



	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource entity) throws CreateException {

		for (EntityField field : entity.getFields()) {
			if (!field.getType().isCollection()) {
				continue;
			}


			Method m = new Method("add" + field.getUpperCamelName());
			m.getParameters().addAll(createVarArgParameter(entity, field));
			m.getBody().addAll(getAdderStmts(entity, field));
			entity.addMethod(m);

		}
	}


	private Collection<? extends Parameter> createVarArgParameter(EntityResource entity, EntityField field)
			throws CreateException {
		Method.Parameter param = new Method.Parameter(field.getName() + "ToAdd", field.getType().getParameter());
		param.setVarArg(true);
		return asList(param);
	}


	private List<Statement> getAdderStmts(EntityResource res, EntityField field) throws CreateException {
		List<Statement> stmts = new ArrayList<Statement>();
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
		String paramName = field.getName() + "ToAdd";
		String fieldClsName = field.getType().getParameter();

		CompoundStatement nullChk = new IfStatement(paramName + " == null");
		nullChk.addChildStmt(new Statement("return", ""));


		CompoundStatement initSet = new IfStatement(field.getName() + " == null");
		if (field.getType().getSimpleClassName().equals("Set")) {
			String importStmt = "java.util.HashSet";
			initSet.addChildStmt(new Statement(field.getName() + " = new HashSet<" + fieldClsName + ">()", importStmt));

		} else if (field.getType().getSimpleClassName().equals("List")) {
			String importStmt = "java.util.ArrayList";
			initSet.addChildStmt(new Statement(field.getName() + " = new ArrayList<" + fieldClsName + ">()", importStmt));
		}



		Statement forStmt = new Statement(paramName);
		ForStatement stmt = new ForStatement(new Type(fieldClsName), "obj", forStmt);

		stmt.addChildStmt(new Statement("obj.set" + res.getName() + "(this)", ""));
		stmt.addChildStmt(new Statement(field.getName() + ".add(obj)", ""));

		stmts.add(nullChk);
		stmts.add(initSet);
		stmts.add(stmt);

		return stmts;
	}
}
