package ro.swl.engine.parser.model;

import ro.swl.engine.parser.ASTInputLabel;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public abstract class InputComponent extends Component {

	public InputComponent(int id) {
		super(id);
	}

	@Override
	public void render(TagWriter writer) throws WriteException {
		//draw a label just before this input
		ASTInputLabel astInputLabel = new ASTInputLabel(0);
		astInputLabel.setRenderInline(true);
		astInputLabel.render(writer);
		super.render(writer);
	}

	@Override
	public void writeAttributes(TagWriter writer) throws WriteException {
		writer.append(getInputComponentType());
	}

	@Override
	protected boolean renderInline() {
		return true;
	}

	protected abstract String getInputComponentType();

}
