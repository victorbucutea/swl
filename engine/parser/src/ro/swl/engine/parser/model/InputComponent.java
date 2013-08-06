package ro.swl.engine.parser.model;

import static ro.swl.engine.writer.Signal.TURN_OFF_LABEL_RENDERING;
import ro.swl.engine.parser.ASTLabel;
import ro.swl.engine.writer.Signal;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

public abstract class InputComponent extends Component {

	private boolean labelRenderingOff;

	public InputComponent(int id) {
		super(id);
	}

	@Override
	public void receiveSignal(Signal signal) {
		if (!TURN_OFF_LABEL_RENDERING.equals(signal)) {
			this.labelRenderingOff = true;
		}
	}

	@Override
	public void render(TagWriter writer) throws WriteException {
		//draw a label just before this input if not signaled to do otherwise ( ASTHorizontalForm )
		if (labelRenderingOff) {
			ASTLabel astInputLabel = new ASTLabel(0);
			astInputLabel.setRenderInline(true);
			astInputLabel.render(writer);
		}
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
