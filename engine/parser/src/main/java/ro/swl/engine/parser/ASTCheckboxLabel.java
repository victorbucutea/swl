package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.TagWriter;
import ro.swl.engine.writer.ui.WriteException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public class ASTCheckboxLabel extends ASTLabel {


	public ASTCheckboxLabel(Component parent, Component compThisLabelIsFor) {
		super(parent, compThisLabelIsFor);
	}


	@Override
	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
		writer.writeContent("input label TODO", renderInline);
	}


	@Override
	public List<String> getCssClassNames() {
		return asList(grammar.checkboxClass()); // TODO define how to set styles for non-declared labels
	}


	@Override
	public List<String> getCssInlineStyles() {  // TODO define how to set styles for non-declared labels
		return new ArrayList<String>();
	}

}
