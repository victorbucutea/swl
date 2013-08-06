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

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.parser.model.LayoutComponent;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public class ASTVerticalLayout extends LayoutComponent {
	public ASTVerticalLayout(int id) {
		super(id);
	}

	@Override
	protected String getComponentName() {
		return grammar.verticalLayout();
	}

	@Override
	protected boolean renderContentBeforeChild(TagWriter writer, Component child) throws WriteException {
		ASTVerticalLayoutRow row = new ASTVerticalLayoutRow(0);
		row.jjtAddChild(child, 0);
		row.render(writer);
		return false;
	}

}
/*
 * JavaCC - OriginalChecksum=e61f84ca8a8603278f2b0b129417587c (do not edit this
 * line)
 */
