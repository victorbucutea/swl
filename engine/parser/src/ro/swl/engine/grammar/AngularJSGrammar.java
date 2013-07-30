package ro.swl.engine.grammar;

public class AngularJSGrammar implements Grammar {

	@Override
	public String input() {
		return "<input type=\"text\"";
	}

	@Override
	public String inputDeclarationEnd() {
		return ">";
	}

	@Override
	public String inputEnd() {
		return "</input>";
	}

	@Override
	public String inputArea() {
		return "<input type=\"area\"";
	}

	@Override
	public String inputFile() {
		return input();
	}

	@Override
	public String selectbox() {
		return "<select ";
	}

	@Override
	public String checkbox() {
		return "<input type=\"checkbox\" ";
	}

	@Override
	public String checkboxDeclarationEnd() {
		return ">";
	}

	@Override
	public String checkboxEnd() {
		return "</input>";
	}

	@Override
	public String horizontalLayout() {
		return "<div ";
	}

	@Override
	public String verticalLayout() {
		return "<div ";
	}

	@Override
	public String label() {
		return "<label ";
	}

	@Override
	public String labelDeclarationEnd() {
		return ">";
	}

	@Override
	public String labelEnd() {
		return "</label>";
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
