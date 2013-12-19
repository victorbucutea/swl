/* Generated By:JJTree: Do not edit this line. ASTLayoutRow.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import static ro.swl.engine.parser.SWLConstants.ROW;
import static ro.swl.engine.parser.SWLConstants.tokenImage;
import static ro.swl.engine.parser.SWLTreeConstants.JJTLAYOUTROW;
import ro.swl.engine.parser.model.Component;
import ro.swl.engine.parser.model.LayoutComponent;


public class ASTLayoutRow extends LayoutComponent {

	protected ASTLayoutRow(int id) {
		super(id);
		setImageWithoutQuote(tokenImage[ROW]);
	}


	public ASTLayoutRow() {
		this(JJTLAYOUTROW);
	}


	public ASTLayoutRow(Component parent) {
		this();
		setParent(parent);
	}


	@Override
	protected String getComponentName() {
		return grammar.verticalLayoutRow();
	}


	@Override
	public boolean hasExternalizableLabel() {
		return true;
	}

}
/*
 * JavaCC - OriginalChecksum=0f704bdf304d136165e470bd4480c31d (do not edit this
 * line)
 */