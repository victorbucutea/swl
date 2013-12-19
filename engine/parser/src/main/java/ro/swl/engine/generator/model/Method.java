package ro.swl.engine.generator.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ro.swl.engine.generator.GenerateException;


public class Method {

	private Set<String> imports;

	private Set<Parameter> parameters;

	private Set<Annotation> annotations = new TreeSet<Annotation>();

	private List<Statement> body = new ArrayList<Statement>();

	private String name;

	private Type returnType;

	private String accessModifier = "public";


	public Method(String name) {
		this.name = name;
	}


	public void addAnnotation(Annotation annotation) throws GenerateException {
		annotations.add(annotation);
	}


	public void addAnnotation(String annotation) throws GenerateException {
		annotations.add(new Annotation(annotation));
	}


	public Method(String name, List<Statement> body) {
		this.name = name;
		if (body != null) {
			body.addAll(body);
		}
	}

	public static class Parameter {

		private String name;

		private Type type;

		private boolean isVarArg;



		public Parameter(String name, Type type) {
			super();
			this.name = name;
			this.type = type;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public Type getType() {
			return type;
		}


		public void setType(Type type) {
			this.type = type;
		}


		public boolean isVarArg() {
			return isVarArg;
		}


		public void setVarArg(boolean isVarArg) {
			this.isVarArg = isVarArg;
		}


	}


	public static class Statement {

		private Set<String> imports = new HashSet<String>();

		private String statement;


		public Statement(String content, Set<String> imports) {
			this.statement = content;
			this.imports = imports;
		}


		public Statement(String content, String imprt) {
			this.statement = content;
			if (!isNotEmpty(imprt))
				this.imports.add(imprt);
		}


		public Statement() {

		}


		public String getStatement() {
			return statement;
		}


		public void setStatement(String statement) {
			this.statement = statement;
		}


		public String render() {
			return statement + ";\n";
		}


		public Set<String> getImports() {
			return imports;
		}


		public void addImport(String imprt) {
			this.imports.add(imprt);
		}

	}


	public static class IfStatement extends Statement {

		private String condition;

		private Set<Statement> childStmts = new HashSet<Method.Statement>();


		public IfStatement(String condition) {
			this.condition = condition;
		}



		public Set<Statement> getChildStmts() {
			return childStmts;
		}


		public void addChildStmt(Statement childStmts) {
			this.childStmts.add(childStmts);
		}


		@Override
		public String render() {
			StringBuilder blder = new StringBuilder();
			for (Statement stmt : childStmts) {
				blder.append(stmt);
			}
			//@formatter:off
			return "if ( " + condition + " ) {\n" +
						blder +
				"}";
			//@formatter:on
		}
	}


	public static class ForStatement extends Statement {

		private Type itType;

		private String itVar;

		private Statement expression;

		private Set<Statement> childStmts = new HashSet<Method.Statement>();


		public ForStatement(Type iteratingType, String iteratingVar, Statement expression) {
			this.itType = iteratingType;
			this.itVar = iteratingVar;
			this.expression = expression;
		}


		public Set<Statement> getChildStmts() {
			return childStmts;
		}


		public void addChildStmt(Statement childStmts) {
			this.childStmts.add(childStmts);
		}


		@Override
		public String render() {
			StringBuilder blder = new StringBuilder();
			for (Statement stmt : childStmts) {
				blder.append(stmt);
			}
			//@formatter:off
			return "for ( " +itType.getSimpleClassName()  + " "+itVar+ ":"+ expression+" ) {\n" +
						blder +
				"}\n\n";
			//@formatter:on
		}



		public Type getItType() {
			return itType;
		}



		public void setItType(Type itType) {
			this.itType = itType;
		}



		public String getItVar() {
			return itVar;
		}



		public void setItVar(String itVar) {
			this.itVar = itVar;
		}



		public Statement getExpression() {
			return expression;
		}



		public void setExpression(Statement expression) {
			this.expression = expression;
		}

	}


	public Set<String> getImports() {
		return imports;
	}


	public void setImports(Set<String> imports) {
		this.imports = imports;
	}


	public Set<Parameter> getParameters() {
		return parameters;
	}


	public void setParameters(Set<Parameter> parameters) {
		this.parameters = parameters;
	}


	public List<Statement> getBody() {
		return body;
	}


	public void setBody(List<Statement> body) {
		this.body = body;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Type getReturnType() {
		return returnType;
	}



	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}



	public String getAccessModifier() {
		return accessModifier;
	}



	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

}
