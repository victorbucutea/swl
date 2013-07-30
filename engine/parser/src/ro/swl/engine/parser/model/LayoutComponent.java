package ro.swl.engine.parser.model;

import java.util.List;

import ro.swl.engine.parser.ASTMvcArea;

public abstract class LayoutComponent extends Component {

	public LayoutComponent(int id) {
		super(id);
	}

	/**
	 * Layout components have class names in the description
	 */
	@Override
	public List<String> getCssClassNames() {
		return getDescription().getCssClassNames();
	}

	/**
	 * Layout components have style names in the description
	 */
	@Override
	public List<String> getCssInlineStyles() {
		return getDescription().getCssInlineStyles();
	}

	/**
	 * Layout components have child components inside a MVCArea
	 */
	@Override
	public List<Component> getChildComponents() {
		return getFirstChildNodeOfType(ASTMvcArea.class, false).getChildComponents();
	}

}
