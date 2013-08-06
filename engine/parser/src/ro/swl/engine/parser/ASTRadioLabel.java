package ro.swl.engine.parser;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public class ASTRadioLabel extends ASTLabel {

	public ASTRadioLabel(int id) {
		super(id);
	}

	@Override
	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
		writer.writeContent("input label TODO", renderInline);
	}

	@Override
	public List<String> getCssClassNames() {
		return asList(grammar.radioClass()); // TODO define how to set styles for non-declared labels
	}

	@Override
	public List<String> getCssInlineStyles() {  // TODO define how to set styles for non-declared labels
		return new ArrayList<String>();
	}
}
