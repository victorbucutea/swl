package ro.swl.engine.parser.model;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.List;

import javax.inject.Inject;

import ro.swl.engine.grammar.AngularJSGrammar;
import ro.swl.engine.grammar.Grammar;
import ro.swl.engine.parser.ASTCssClassName;
import ro.swl.engine.parser.ASTCssInlineStyle;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.parser.SWLNode;
import ro.swl.engine.writer.WriteException;
import ro.swl.engine.writer.Writer;
import ro.swl.engine.writer.WritingComponent;

/**
 * Abstract component is defined by
 * [component]([model variable or literal], [css class])
 * 
 * @author VictorBucutea
 * 
 */
public abstract class Component extends SWLNode implements WritingComponent {

	@Inject
	protected Grammar grammar = new AngularJSGrammar();

	protected boolean labelRendered = false;

	protected boolean hasBody = true;

	public Component(int id) {
		super(id);
	}

	public Component(SWL language, int id) {
		super(language, id);
	}

	public final void renderComponent(Writer writer) throws WriteException {

		if (!labelRendered) {
			writeLabel(writer);
		}

		beginBodyDeclaration(writer);
		writeCssStyles(writer);
		writeAttributes(writer);
		endBodyDeclaration(writer);
		renderChildren(writer);
		endBody(writer);

	}

	protected void renderChildren(Writer writer) throws WriteException {
		for (Component c : getChildComponents()) {
			writer.indent();
			c.renderComponent(writer);
			writer.unIndent();
		}
	}

	@Override
	public void beginBodyDeclaration(Writer writer) throws WriteException {
		// set flag to skip rendering. Makes no sense 
		// to render anything if we don't start a body
		hasBody = false;
	}

	@Override
	public void writeCssStyles(Writer writer) throws WriteException {
		if (!hasBody) {
			return;
		}

		String clsString = join(getCssClassNames().toArray(), " ");
		writer.append(grammar.styleClassAttribute(clsString));

		String inlineCss = join(getCssInlineStyles().toArray(), "; ");
		writer.append(grammar.inlineStyleAttribute(inlineCss));
	}

	@Override
	public void writeAttributes(Writer writer) throws WriteException {
	}

	@Override
	public void endBodyDeclaration(Writer writer) throws WriteException {
	}

	@Override
	public void writeLabel(Writer writer) throws WriteException {
	}

	@Override
	public void endBody(Writer writer) throws WriteException {
	}

	public List<String> getCssClassNames() {
		return getImageOfChildNodesOfType(ASTCssClassName.class, true);
	}

	public List<String> getCssInlineStyles() {
		return getImageOfChildNodesOfType(ASTCssInlineStyle.class, true);
	}

	public Description getDescription() {
		return getFirstChildNodeOfType(Description.class, false);
	}

}
