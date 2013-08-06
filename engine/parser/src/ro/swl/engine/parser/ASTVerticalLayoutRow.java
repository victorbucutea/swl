package ro.swl.engine.parser;

import ro.swl.engine.parser.model.Component;

public class ASTVerticalLayoutRow extends Component {

	public ASTVerticalLayoutRow(int id) {
		super(id);
	}

	@Override
	protected String getComponentName() {
		return grammar.verticalLayoutRow();
	}

}
