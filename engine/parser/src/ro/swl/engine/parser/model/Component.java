package ro.swl.engine.parser.model;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ro.swl.engine.grammar.AngularJSGrammar;
import ro.swl.engine.grammar.Grammar;
import ro.swl.engine.parser.ASTModelVariable;
import ro.swl.engine.parser.SWLNode;
import ro.swl.engine.writer.TagWriter;
import ro.swl.engine.writer.WriteException;

/**
 * Abstract component is defined by
 * [component]([model variable or literal], [css class])
 * 
 * @author VictorBucutea
 * 
 */
public abstract class Component extends SWLNode {

	@Inject
	protected Grammar grammar = new AngularJSGrammar();

	public Component(int id) {
		super(id);
	}

	public void render(TagWriter writer) throws WriteException {
		writer.startTag(getComponentName());

		writeModelValueBinding(writer);

		writeAttributes(writer);

		writeCssStyles(writer);

		if (!hasChildComponents()) {
			writer.shortCloseTag(getComponentName());
			return;
		}

		writer.endTag(renderInline());

		renderContentBeforeChildren(writer);

		renderChildren(writer);

		renderContentAfterChildren(writer);

		writer.closeTag(getComponentName(), renderInline());
	}

	public void writeCssStyles(TagWriter writer) throws WriteException {
		List<String> cssClassNames = getCssClassNames();
		if (!cssClassNames.isEmpty()) {
			String clsString = join(cssClassNames.toArray(), " ");
			writer.append(grammar.styleClassAttribute(clsString));
		}

		List<String> styles = getCssInlineStyles();
		if (!styles.isEmpty()) {
			String inlineCss = join(styles.toArray(), "; ");
			writer.append(grammar.inlineStyleAttribute(inlineCss));
		}
	}

	protected boolean renderInline() {
		return false;
	}

	protected void writeModelValueBinding(TagWriter writer) throws WriteException {
	}

	protected void writeAttributes(TagWriter writer) throws WriteException {
	}

	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
	}

	protected void renderContentBeforeChildren(TagWriter writer) throws WriteException {
	}

	protected void renderContentAfterChild(TagWriter writer, Component child) throws WriteException {
	}

	protected boolean renderContentBeforeChild(TagWriter writer, Component child) throws WriteException {
		return true;
	}

	protected void renderChildren(TagWriter writer) throws WriteException {
		for (Component c : getChildComponents()) {
			if (renderContentBeforeChild(writer, c)) {
				c.render(writer);
			}
			renderContentAfterChild(writer, c);
		}
	}

	protected abstract String getComponentName();

	public ASTModelVariable getModelValueBinding() throws WriteException {
		List<ASTModelVariable> modelVars = getChildNodesOfType(ASTModelVariable.class, true);

		if (modelVars.size() > 0) {
			return modelVars.get(0);
		} else {
			throw new WriteException("Cannot identify model variable binding.");
		}
	}

	public List<String> getCssClassNames() {
		Description description = getDescription();
		if (description != null)
			return description.getCssClassNames();
		else
			return new ArrayList<String>();
	}

	public List<String> getCssInlineStyles() {
		Description description = getDescription();
		if (description != null)
			return description.getCssInlineStyles();
		else
			return new ArrayList<String>();
	}

	public Description getDescription() {
		return getFirstChildNodeOfType(Description.class, false);
	}

}
