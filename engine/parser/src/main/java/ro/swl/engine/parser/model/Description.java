package ro.swl.engine.parser.model;

import ro.swl.engine.parser.*;

import java.util.List;


/**
 * Standard Description of a component.
 * 
 * {@link ASTInput}, {@link ASTInputArea}, {@link ASTLabel}, etc.
 * 
 * Syntax :
 * 
 * {layout-or-input-component} ( {model-variable} , "{css class or style}" )
 * 
 * @author VictorBucutea
 * 
 */
public abstract class Description extends SWLNode {

	public Description(int i) {
		super(i);
	}


	public Description(SWL language, int id) {
		super(language, id);
	}


	/**
	 * iterates recursively through child elements. It is safe because a
	 * Description may not have child nodes which in turn have css styles ( so
	 * by searching for an {@link ASTCssInlineStyle} we do not bump into styles
	 * of child nodes )
	 * 
	 * Override if you define descriptions ( ... ) which may have child CSS
	 * which we don't want to return here
	 * 
	 * @return
	 */
	public List<String> getCssInlineStyles() {
		return getImageOfChildNodesOfType(ASTCssInlineStyle.class, true);
	}


	public List<String> getCssClassNames() {
		return getImageOfChildNodesOfType(ASTCssClassName.class, true);
	}

}
