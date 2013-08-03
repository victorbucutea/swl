package ro.swl.engine.parser;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public class ASTInputLabel extends Component {

	private boolean renderInline = false;

	public ASTInputLabel(int id) {
		super(id);
	}

	@Override
	protected String getComponentName() {
		return grammar.label();
	}

	@Override
	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
		writer.writeContent("input label TODO", renderInline);
	}

	@Override
	protected boolean renderInline() {
		return renderInline;
	}

	public void setRenderInline(boolean value) {
		renderInline = value;
	}

	@Override
	public List<String> getCssClassNames() {
		return asList(grammar.checkboxClass()); // TODO define how to set styles for labels
	}

	@Override
	public List<String> getCssInlineStyles() {
		return new ArrayList<String>();
	}

	@Override
	public boolean hasChildComponents() {
		// AST input label has artificial child components
		return true;
	}
}
