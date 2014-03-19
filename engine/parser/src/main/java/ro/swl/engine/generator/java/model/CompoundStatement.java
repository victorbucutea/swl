package ro.swl.engine.generator.java.model;



import static org.apache.commons.lang3.StringUtils.rightPad;

import java.util.ArrayList;
import java.util.List;


public class CompoundStatement extends Statement {

	protected static final int DFLT_INDENTATION = 3;
	protected static final String CR_LF = "\r\n";

	protected List<Statement> childStmts = new ArrayList<Statement>();
	protected String childIndentation;
	protected String defaultIndentation;


	public CompoundStatement(int childIndentation) {
		this.childIndentation = rightPad("", childIndentation, '\t');
		this.defaultIndentation = rightPad("", childIndentation - 1, '\t');
	}


	public List<Statement> getChildStmts() {
		return childStmts;
	}


	public void addChildStmt(Statement childStmts) {
		this.childStmts.add(childStmts);
		addImports(childStmts.getImports());
	}


	protected StringBuilder renderChildren() {
		StringBuilder blder = new StringBuilder();

		for (Statement stmt : childStmts) {
			blder.append(childIndentation);
			blder.append(stmt.render() + CR_LF);
		}
		return blder;
	}

}