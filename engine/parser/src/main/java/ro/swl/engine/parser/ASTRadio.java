/* Generated By:JJTree: Do not edit this line. ASTRadio.java Version 4.3 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import static ro.swl.engine.parser.SWLConstants.RADIO;
import static ro.swl.engine.parser.SWLConstants.tokenImage;

import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;


public class ASTRadio extends Component {

	private ASTLabel valueLabel;
	private boolean wrappedInLabel;


	public ASTRadio(int id) {
		super(id);
		setImageWithoutQuote(tokenImage[RADIO]);
		setRenderId(true);//id is needed for selecting this input when clicking on label
	}


	@Override
	public void render(TagWriter writer) throws WriteException {
		if (!wrappedInLabel) {
			wrappedInLabel = true;
			valueLabel = new ASTRadioLabel(getParentComponent(), this);
			valueLabel.addChild(this);
			valueLabel.jjtSetValue(getLabelValue());
			valueLabel.render(writer);
		} else {
			super.render(writer);
		}
	}


	@Override
	protected String getComponentName() {
		return grammar.radio();
	}


	@Override
	public boolean hasExternalizableLabel() {
		// TODO label right now is the value of the property passed in as second param
		// that value will need to be a key to the i18n bundle
		return false;
	}


	@Override
	public void writeAttributes(TagWriter writer) throws WriteException {
		writer.append(grammar.radioType());
		writer.append(grammar.radioName(getModelValue()));
	}


	private String getLabelValue() throws WriteException {

		List<String> modelVars = getImageOfChildNodesOfType(ASTModelVariable.class, true);
		if (modelVars.size() > 1) {
			return modelVars.get(1);
		} else {
			throw new WriteException("No label value for this radio " + getId());
		}
	}


	private String getModelValue() throws WriteException {
		List<String> modelVars = getImageOfChildNodesOfType(ASTModelVariable.class, true);

		if (modelVars.size() > 0) {
			return modelVars.get(0);
		} else {
			throw new WriteException("Cannot identify name of radio input.");
		}

	}


	public ASTLabel getValueLabel() {
		return valueLabel;
	}

}
/*
 * JavaCC - OriginalChecksum=1f49b14ad67219d7d80b97f8500126cc (do not edit this
 * line)
 */
