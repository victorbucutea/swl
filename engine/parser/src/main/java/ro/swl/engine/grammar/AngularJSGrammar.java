package ro.swl.engine.grammar;

public class AngularJSGrammar implements Grammar {

	@Override
	public String inputText() {
		return "input";
	}


	@Override
	public String inputTextType() {
		return " type=\"text\"";
	}


	@Override
	public String inputArea() {
		return "input";
	}


	@Override
	public String inputAreaType() {
		return " type=\"area\"";
	}


	@Override
	public String inputFile() {
		return "input";
	}


	@Override
	public String inputFileType() {
		return " type=\"file\"";
	}


	@Override
	public String selectbox() {
		return "<select ";
	}


	@Override
	public String selectoption() {
		return "<option";
	}


	@Override
	public String checkbox() {
		return "input";
	}


	@Override
	public String checkboxType() {
		return " type=\"checkbox\"";
	}


	@Override
	public String checkboxClass() {
		return "checkbox";
	}


	@Override
	public String radio() {
		return "input";
	}


	@Override
	public String radioType() {
		return " type=\"radio\"";
	}


	@Override
	public String radioClass() {
		return "radio";
	}


	@Override
	public String radioName(String groupName) {
		return " name=\"" + groupName + "\"";
	}


	@Override
	public String img() {
		return "img";
	}


	@Override
	public String imgModelValueBinding(String modelBinding) {
		return " ng-src=\"{{" + modelBinding + "}}\"";
	}


	@Override
	public String imgSrc(String image) {
		return " src=\"" + image + "\"";
	}


	@Override
	public String horizontalLayout() {
		return "div";
	}


	@Override
	public String horizontalLayoutClass() {
		return "row-fluid";
	}


	@Override
	public String horizontalFormClass() {
		return "form-horizontal";
	}


	@Override
	public String horizontalFormGroupClass() {
		return "control-group";
	}


	@Override
	public String horizontalFormLabelClass() {
		return "control-label";
	}


	@Override
	public String horizontalFormControlClass() {
		return "controls";
	}


	@Override
	public String horizontalLayoutColumnClass(int span) {
		return "span" + span;
	}


	@Override
	public String horizontalLayoutColumn() {
		return "div";
	}


	@Override
	public String verticalLayout() {
		return "div";
	}


	@Override
	public String verticalLayoutRow() {
		return "div";
	}


	@Override
	public String section() {
		return "fieldset";
	}


	@Override
	public String sectionTitle() {
		return "legend";
	}


	@Override
	public String span() {
		return "span ";
	}


	@Override
	public String label() {
		return "label";
	}


	@Override
	public String labelForAttribute(String id) {
		return " for=\"" + id + "\"";
	}


	@Override
	public String text() {
		return "span";
	}


	@Override
	public String button() {
		return "button";
	}


	@Override
	public String buttonClassAttribute() {
		return "btn";
	}


	@Override
	public String inlineStyleAttribute(String value) {
		return " style=\"" + value + "\" ";
	}


	@Override
	public String styleClassAttribute(String value) {
		return " class=\"" + value + "\" ";
	}


	@Override
	public String verticalLayoutRowClass() {
		return "";
	}


	@Override
	public String idAttribute(String id) {
		return " id=\"" + id + "\" ";
	}

}
