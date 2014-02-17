package ro.swl.engine.parser.model;

import ro.swl.engine.parser.ASTLabel;
import ro.swl.engine.writer.ui.TagWriter;
import ro.swl.engine.writer.ui.WriteException;


public abstract class InputComponent extends Component {

	private ASTLabel label;


	public InputComponent(int id) {
		super(id);
	}


	@Override
	public void render(TagWriter writer) throws WriteException {
		if (isRenderLabel()) {
			// render id because label's "for" attribute will focus this input component 
			setRenderId(true);
			label = new ASTLabel(getParentComponent(), this);
			label.setRenderInline(true);
			label.render(writer);
		}
		super.render(writer);
	}


	@Override
	public boolean hasExternalizableLabel() {
		return true;
	}


	@Override
	public void writeAttributes(TagWriter writer) throws WriteException {
		writer.append(getInputComponentType());
	}


	@Override
	protected boolean renderInline() {
		return true;
	}


	public ASTLabel getLabel() {
		return label;
	}


	protected abstract String getInputComponentType();

}
