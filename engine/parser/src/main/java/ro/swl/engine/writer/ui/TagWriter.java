package ro.swl.engine.writer.ui;

public class TagWriter extends Writer {

	public void startTag(String tagName) {
		indent();
		appendLine("<" + tagName);
	}

	public void endTag(boolean inline) {
		append(">");
	}

	public void writeContent(String content, boolean inline) {
		if (!inline) {
			appendLine(content);
		} else {
			append(content);
		}
	}

	public void closeTag(String tagName, boolean inline) {

		if (!inline) {
			appendLine("</" + tagName + ">");
		} else {
			append("</" + tagName + ">");
		}

		unIndent();
	}

	public void shortCloseTag(String componentName) {
		append("/>");
		unIndent();
	}
}
