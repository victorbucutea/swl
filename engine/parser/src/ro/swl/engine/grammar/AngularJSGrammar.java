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
		return "<input type=\"radio\"";
	}

	@Override
	public String radioClass() {
		return "radio";
	}

	@Override
	public String horizontalLayout() {
		return "<div ";
	}

	@Override
	public String horizontalLayoutClass() {
		return "row-fluid";
	}

	@Override
	public String horizontalLayoutColumnClass(String span) {
		return "span" + span;
	}

	@Override
	public String horizontalLayoutColumn() {
		return "<div ";
	}

	@Override
	public String verticalLayout() {
		return "<div ";
	}

	@Override
	public String span() {
		return "<span ";
	}

	@Override
	public String label() {
		return "label";
	}

	@Override
	public String button() {
		return "button";
	}

	@Override
	public String inlineStyleAttribute(String value) {
		return " style=\"" + value + "\" ";
	}

	@Override
	public String styleClassAttribute(String value) {
		return " class=\"" + value + "\" ";
	}

}
