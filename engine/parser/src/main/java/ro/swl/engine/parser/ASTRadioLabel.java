package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.TagWriter;
import ro.swl.engine.writer.ui.WriteException;

import java.util.ArrayList;
import java.util.List;


public class ASTRadioLabel extends ASTLabel {

	public ASTRadioLabel(Component parent, Component compWithThisLabel) {
		super(parent, compWithThisLabel);
		addClass(grammar.radioClass());
	}


	@Override
	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
		writer.writeContent(jjtGetValue().toString(), renderInline);
	}


	@Override
	public List<String> getCssInlineStyles() {  // TODO define how to set styles for non-declared labels
		return new ArrayList<String>();
	}
}
