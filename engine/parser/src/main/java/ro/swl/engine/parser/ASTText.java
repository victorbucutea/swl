/* Generated By:JJTree: Do not edit this line. ASTText.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.TagWriter;
import ro.swl.engine.writer.ui.WriteException;

import static ro.swl.engine.parser.SWLConstants.TEXT;
import static ro.swl.engine.parser.SWLConstants.tokenImage;


public class ASTText extends Component {

	public ASTText(int id) {
		super(id);
		setImageWithoutQuote(tokenImage[TEXT]);
	}


	@Override
	protected String getComponentName() {
		return grammar.text();
	}


	@Override
	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
		writer.writeContent("label TODO", true);
	}


	@Override
	public boolean hasChildComponents() {
		return true;// pretend we have children to not short close (/>) outer text tag
	}


	@Override
	protected boolean renderInline() {
		return true;
	}
}
/*
 * JavaCC - OriginalChecksum=399697d02cc9fed0c98a77157a1b1dd4 (do not edit this
 * line)
 */
