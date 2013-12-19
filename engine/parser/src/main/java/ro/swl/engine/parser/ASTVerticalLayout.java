/*
 * Generated By:JJTree: Do not edit this line. ASTVerticalLayout.java Version
 * 4.3
 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import static ro.swl.engine.parser.SWLConstants.VERTICAL_LAYOUT;
import static ro.swl.engine.parser.SWLConstants.tokenImage;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.parser.model.LayoutComponent;


public class ASTVerticalLayout extends LayoutComponent {

	public ASTVerticalLayout(int id) {
		super(id);
		setImageWithoutQuote(tokenImage[VERTICAL_LAYOUT]);
	}


	@Override
	protected String getComponentName() {
		return grammar.verticalLayout();
	}


	public List<ASTLayoutRow> getRows() {
		return getChildNodesOfType(ASTLayoutRow.class, false);
	}


	@Override
	public List<Component> getChildComponents() {
		return new ArrayList<Component>(getRows());
	}

}
/*
 * JavaCC - OriginalChecksum=e61f84ca8a8603278f2b0b129417587c (do not edit this
 * line)
 */