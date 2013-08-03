package ro.swl.engine.parser.model;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.List;

import javax.inject.Inject;

import ro.swl.engine.grammar.AngularJSGrammar;
import ro.swl.engine.grammar.Grammar;
import ro.swl.engine.parser.ASTCssClassName;
import ro.swl.engine.parser.ASTCssInlineStyle;
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

	protected boolean labelRendered = false;

	protected boolean hasBody = true;

	public Component(int id) {
		super(id);
	}

	public void render(TagWriter writer) throws WriteException {
		writer.startTag(getComponentName());

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

	protected void writeAttributes(TagWriter writer) throws WriteException {
	}

	protected void renderContentAfterChildren(TagWriter writer) throws WriteException {
	}

	protected void renderContentBeforeChildren(TagWriter writer) throws WriteException {
	}

	protected void renderChildren(TagWriter writer) throws WriteException {
		for (Component c : getChildComponents()) {
			c.render(writer);
		}
	}

	protected abstract String getComponentName();

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
