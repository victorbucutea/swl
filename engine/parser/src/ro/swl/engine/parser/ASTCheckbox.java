/* Generated By:JJTree: Do not edit this line. ASTCheckbox.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public class ASTCheckbox extends Component {

	/**
	 * flag to indicate whether we already wrapped this checkbox in a label,
	 * because on the subsequent calls to render(), we need not wrap again
	 */
	private boolean wrappedLabel;

	public ASTCheckbox(int id) {
		super(id);
	}

	@Override
	public void render(TagWriter writer) throws WriteException {
		if (!wrappedLabel) {
			ASTInputLabel label = new ASTInputLabel(0);
			label.jjtAddChild(this, 0);
			wrappedLabel = true;
			label.render(writer);
		} else {
			super.render(writer);
		}
	}

	@Override
	protected String getComponentName() {
		return grammar.checkbox();
	}

	@Override
	public void writeAttributes(TagWriter writer) throws WriteException {
		writer.append(grammar.checkboxType());
	}

}
/*
 * JavaCC - OriginalChecksum=26ad9a61dc0f3ee28c3664e80a122b1a (do not edit this
 * line)
 */
