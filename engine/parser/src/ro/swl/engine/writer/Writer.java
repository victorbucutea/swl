package ro.swl.engine.writer;

public class Writer {

	StringBuffer buffer = new StringBuffer();

	public void append(String str) {
		buffer.append(str);
	}

	public void appendLine(String str) {
		buffer.append("\n" + str);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

}
