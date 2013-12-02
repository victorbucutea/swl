package ro.swl.engine.parser.model;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ro.swl.engine.grammar.AngularJSGrammar;
import ro.swl.engine.grammar.Grammar;
import ro.swl.engine.parser.ASTModelVariable;
import ro.swl.engine.parser.SWLNode;
import ro.swl.engine.writer.IdGenerator;
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

	@Inject
	protected IdGenerator keyGenerator = new IdGenerator();

	protected List<String> extraCssClasses = new ArrayList<String>();

	protected String componentId;

	private boolean renderLabel = true;

	private boolean renderId = false;


	public Component(int id) {
		super(id);
	}


	public void render(TagWriter writer) throws WriteException {

		writer.startTag(getComponentName());

		writeModelValueBinding(writer);

		writeId(writer);

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


	protected boolean renderInline() {
		return false;
	}


	public void generateComponentId() throws WriteException {
		componentId = keyGenerator.generate(this);
	}


	private void writeId(TagWriter writer) throws WriteException {
		if (componentId == null) {
			generateComponentId();
		}

		if (renderId) {
			writer.append(grammar.idAttribute(getId()));
		}
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


	protected abstract String getComponentName();


	public boolean hasExternalizableLabel() {
		return false;
	}


	public ASTModelVariable getModelValueBinding() {
		return getFirstChildNodeOfType(ASTModelVariable.class, true);
	}


	public List<String> getCssClassNames() {
		List<String> allClasses = new ArrayList<String>();
		allClasses.addAll(extraCssClasses);
		Description description = getDescription();
		if (description != null) {
			allClasses.addAll(description.getCssClassNames());
		}
		return allClasses;
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


	public void addClass(String klass) {
		extraCssClasses.add(klass);
	}


	public void setNoLabelRendering() {
		this.renderLabel = false;
	}


	public void setRenderId(boolean render) {
		this.renderId = render;
	}


	public boolean isRenderLabel() {
		return renderLabel;
	}


	public String getId() {
		if (componentId == null) {
			try {
				generateComponentId();
			} catch (WriteException we) {
				we.printStackTrace();
			}
		}
		return componentId;
	}


	public void setId(String id) {
		this.componentId = id;
	}


	@SuppressWarnings("unchecked")
	public String getImageWithIdx() {
		String calculatedImg = getImage();
		if (getModelValueBinding() != null) {
			if (!getModelValueBinding().isLiteral()) {
				calculatedImg = getModelValueBinding().getImage();
			}
		}

		SWLNode parent = getParent();
		if (parent == null) {
			return calculatedImg;
		}


		List<Component> childComponents = (List<Component>) parent.getChildNodesOfType(getClass(), false);

		int compIdx = getComponentIndex(childComponents);

		if (childComponents.size() > 1)
			return calculatedImg + "." + (compIdx);
		else
			return calculatedImg;
	}


	private int getComponentIndex(List<Component> childComponents) {
		int idx = 1;
		int compIdx = 0;

		for (Component comp : childComponents) {
			if (comp == this) {
				compIdx = idx;
			}
			idx++;
		}
		return compIdx;
	}
}
