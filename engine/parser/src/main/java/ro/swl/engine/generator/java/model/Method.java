package ro.swl.engine.generator.java.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.swl.engine.generator.CreateException;


public class Method {

	private Set<String> imports = new HashSet<String>();

	private List<Parameter> parameters = new ArrayList<Parameter>();

	private List<Annotation> annotations = new ArrayList<Annotation>();

	private List<Statement> body = new ArrayList<Statement>();

	private String name;

	private Type returnType = Type.VOID;

	private String accessModifier = "public";


	public Method(String name) {
		this.name = name;
	}


	public Method(String name, List<Statement> body) {
		this.name = name;
		if (body != null) {
			body.addAll(body);
		}
	}


	public void addAnnotation(Annotation annotation) throws CreateException {
		annotations.add(annotation);
	}


	public void addAnnotation(String annotation) throws CreateException {
		annotations.add(new Annotation(annotation));
	}



	public static class Parameter {

		private String name;

		private Type type;

		private boolean isVarArg;



		public Parameter(String name, Type type) {
			this.name = name;
			this.type = type;
		}


		public Parameter(String name, String type) throws CreateException {
			this.name = name;
			this.type = new Type(type);
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


		public Set<String> getImports() {
			return type.getImports();
		}


	}


	public Set<String> getImports() {

		for (Parameter param : getParameters()) {
			imports.addAll(param.getImports());
		}

		for (Statement stmt : getBody()) {
			imports.addAll(stmt.getImports());
		}

		for (Annotation ann : getAnnotations()) {
			imports.addAll(ann.getImports());
		}

		imports.addAll(returnType.getImports());

		return imports;
	}


	public void setImports(Set<String> imports) {
		this.imports = imports;
	}


	public List<Parameter> getParameters() {
		return parameters;
	}


	public void addParameter(Parameter param) {
		this.parameters.add(param);
	}


	public void addParameter(String name, Type type) {
		this.parameters.add(new Parameter(name, type));
	}


	public List<Statement> getBody() {
		return body;
	}


	public List<Statement> getStatements() {
		return getBody();
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



	public List<Annotation> getAnnotations() {
		return annotations;
	}

}
