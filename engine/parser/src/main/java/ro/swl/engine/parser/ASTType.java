/* Generated By:JJTree: Do not edit this line. ASTType.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import ro.swl.engine.parser.model.SWLNode;

public class ASTType extends SWLNode {

	private boolean primitive;
	private String typeName;


	public ASTType(int id) {
		super(id);
	}


	public void setPrimitive(boolean b) {
		this.primitive = b;
	}



	public boolean isPrimitive() {
		return primitive;
	}


	public void setTypeName(String string) {
		this.typeName = string;
	}



	public String getTypeName() {
		return typeName;
	}



}
/*
 * JavaCC - OriginalChecksum=3bccdcd92f1a297258c10d0e3b4bfe05 (do not edit this
 * line)
 */
