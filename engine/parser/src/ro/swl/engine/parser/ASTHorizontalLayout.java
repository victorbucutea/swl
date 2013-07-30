/*
 * Generated By:JJTree: Do not edit this line. ASTHorizontalLayout.java Version
 * 4.3
 */
/*
 * JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false
 * ,
 * NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true
 */
package ro.swl.engine.parser;

import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.parser.model.LayoutComponent;
import ro.swl.engine.writer.WriteException;
import ro.swl.engine.writer.Writer;

public class ASTHorizontalLayout extends LayoutComponent {

	public ASTHorizontalLayout(int id) {
		super(id);
	}

	@Override
	public ASTHorizontalLayoutDescription getDescription() {
		return getFirstChildNodeOfType(ASTHorizontalLayoutDescription.class, false);
	}

	@Override
	public void beginBodyDeclaration(Writer writer) throws WriteException {
		writer.appendLine(grammar.horizontalLayout());
	}

	@Override
	public void endBodyDeclaration(Writer writer) throws WriteException {
		writer.append(grammar.horizontalLayoutDeclarationEnd());
	}

	@Override
	public void endBody(Writer writer) throws WriteException {
		writer.appendLine(grammar.horizontalLayoutEnd());
	}

	@Override
	public List<String> getCssClassNames() {
		List<String> cssClassNames = getDescription().getCssClassNames();
		cssClassNames.add(grammar.horizontalLayoutClass());
		return cssClassNames;
	}

	@Override
	protected void renderChildren(Writer writer) throws WriteException {
		List<ASTColumnDescription> columns = getColumns();
		List<Component> childComponents = getChildComponents();

		for (ASTColumnDescription descr : columns) {
			String colspan = descr.getImage();
			String columnClass = grammar.horizontalLayoutColumnClass(colspan);

			writer.indent();
			writer.appendLine(grammar.horizontalLayoutColumn());
			writer.append(grammar.styleClassAttribute(columnClass));
			writer.append(grammar.horizontalLayoutColumnDeclarationEnd());

			if (childComponents.isEmpty()) {
				throw new WriteException("Declared fewer components than columns in a horizontal_layout");
			}
			writer.indent();
			childComponents.remove(0).renderComponent(writer);
			writer.unIndent();

			writer.appendLine(grammar.horizontalLayoutColumnEnd());
			writer.unIndent();
		}
	}

	public List<ASTColumnDescription> getColumns() {
		return getDescription().getChildNodesOfType(ASTColumnDescription.class, true);
	}

}
/*
 * JavaCC - OriginalChecksum=ca22731bbbdc3c566291c82b1e7bd3d2 (do not edit this
 * line)
 */
