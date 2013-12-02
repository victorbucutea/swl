package ro.swl.engine.parser.model;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.parser.ASTModelVariable;
import ro.swl.engine.parser.ASTMvcArea;


public abstract class LayoutComponent extends Component {

	public LayoutComponent(int id) {
		super(id);
	}


	@Override
	public List<Component> getChildComponents() {
		ASTMvcArea firstMvcArea = getFirstChildNodeOfType(ASTMvcArea.class, true);

		if (firstMvcArea == null)
			return new ArrayList<Component>();

		return firstMvcArea.getChildComponents();
	}


	@Override
	public ASTModelVariable getModelValueBinding() {
		return null;//Layouts typically do not have model value bindings
	}

}
