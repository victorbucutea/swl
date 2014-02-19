package ro.swl.engine.generator.java.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.HashSet;
import java.util.Set;


/**
 * A Statement in Java language. In other words a simple line of code with no
 * flow control.
 * 
 * @author VictorBucutea
 * 
 */
public class Statement {

	private Set<String> imports = new HashSet<String>();

	private String statement = "";


	public Statement(String content, Set<String> imports) {
		this.statement = content;
		this.imports = imports;
	}


	public Statement(String content, String imprt) {
		this.statement = content;
		if (isNotEmpty(imprt))
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
		return statement + ";";
	}


	public String renderWithoutSemiCol() {
		return statement;
	}


	public Set<String> getImports() {
		return imports;
	}


	public void addImport(String imprt) {
		this.imports.add(imprt);
	}


	public void addImports(Set<String> imports) {
		this.imports.addAll(imports);
	}

}