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

	public String inputText();


	public String inputTextType();


	public String label();


	public String labelForAttribute(String id);


	public String inputArea();


	public String inputAreaType();


	public String inputFile();


	public String inputFileType();


	public String selectbox();


	public String selectoption();


	public String checkbox();


	public String checkboxType();


	public String checkboxClass();


	public String radio();


	public String radioType();


	public String radioClass();


	public String horizontalLayout();


	public String horizontalLayoutClass();


	public String horizontalFormClass();


	public String horizontalFormControlClass();


	public String horizontalFormGroupClass();


	public String horizontalFormLabelClass();


	public String horizontalLayoutColumn();


	public String horizontalLayoutColumnClass(int span);


	public String verticalLayout();


	public String section();


	public String sectionTitle();


	public String inlineStyleAttribute(String value);


	public String styleClassAttribute(String value);


	public String idAttribute(String id);


	public String span();


	public String button();


	public String img();


	public String imgModelValueBinding(String modelBinding);


	public String radioName(String groupName);


	public String imgSrc(String image);


	public String verticalLayoutRow();


	public String text();


	public String verticalLayoutRowClass();


	public String buttonClassAttribute();



}
