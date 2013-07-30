package ro.swl.engine.grammar;

/**
 * A grammar is a set of structural entities and rules that governs the
 * generation and behavior of a language.
 * 
 * A subclass will return the particular representation of the 'conceptual'
 * entity. For example in a plain HTML
 * grammar a representation of an input() component will be an '&lt;input&gt;
 * tag'.
 * 
 * In a JSF grammar context an input() will be an '&lt;h:inputText&gt'.
 * 
 * @author VictorBucutea
 * 
 */
public interface Grammar {

	public String input();

	public String inputDeclarationEnd();

	public String inputEnd();

	public String label();

	public String labelDeclarationEnd();

	public String labelEnd();

	public String inputArea();

	public String inputFile();

	public String selectbox();

	public String checkbox();

	public String checkboxDeclarationEnd();

	public String checkboxEnd();

	public String horizontalLayout();

	public String horizontalLayoutClass();

	public String horizontalLayoutColumn();

	public String horizontalLayoutColumnDeclarationEnd();

	public String horizontalLayoutColumnEnd();

	public String horizontalLayoutColumnClass(String span);

	public String horizontalLayoutDeclarationEnd();

	public String horizontalLayoutEnd();

	public String verticalLayout();

	public String inlineStyleAttribute(String value);

	public String styleClassAttribute(String value);

}
