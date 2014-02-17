package ro.swl.engine.writer.ui;

public class Writer {

	StringBuffer buffer = new StringBuffer();

	private int indent;

	public void indent() {
		indent++;
	}

	public void unIndent() {
		indent--;
	}

	public void append(String str) {
		buffer.append(str);
	}

	private void appendIndentation() {
		for (int i = 0; i < indent; i++) {
			buffer.append("\t");
		}
	}

	public void appendLine(String str) {
		buffer.append("\n");
		appendIndentation();
		buffer.append(str);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

}
