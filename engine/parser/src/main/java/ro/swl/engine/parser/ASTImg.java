/* Generated By:JJTree: Do not edit this line. ASTImg.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import static ro.swl.engine.parser.SWLConstants.IMG;
import static ro.swl.engine.parser.SWLConstants.tokenImage;
import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.TagWriter;
import ro.swl.engine.writer.ui.WriteException;


public class ASTImg extends Component {

	public ASTImg(int id) {
		super(id);
		setImageWithoutQuote(tokenImage[IMG]);
	}


	@Override
	protected String getComponentName() {
		return grammar.img();
	}


	@Override
	protected void writeModelValueBinding(TagWriter writer) throws WriteException {
		ASTModelVariable modelBinding = getModelValueBinding();

		if (modelBinding.isLiteral()) {
			writer.append(grammar.imgSrc(modelBinding.getImage()));
		} else {
			writer.append(grammar.imgModelValueBinding(modelBinding.getImage()));

		}
	}
}
/*
 * JavaCC - OriginalChecksum=ae3cbe4e6d8de1aa5a1c5b3011d2466f (do not edit this
 * line)
 */
