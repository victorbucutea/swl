/* Generated By:JJTree: Do not edit this line. ASTMvcArea.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.parser.model.SWLNode;

import java.util.List;

import static ro.swl.engine.parser.SWLTreeConstants.JJTMVCAREA;


public class ASTMvcArea extends SWLNode {

	public ASTMvcArea() {
		this(JJTMVCAREA);
	}


	protected ASTMvcArea(int id) {
		super(id);
	}


	@Override
	public List<Component> getChildComponents() {
		return getChildNodesOfType(Component.class, false);
	}

}
/*
 * JavaCC - OriginalChecksum=62571ca9d46169c6493b96bcf95831a6 (do not edit this
 * line)
 */
