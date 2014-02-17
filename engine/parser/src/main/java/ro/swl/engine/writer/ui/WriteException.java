package ro.swl.engine.writer.ui;

@SuppressWarnings("serial")
public class WriteException extends Exception {

	public WriteException(String message) {
		super(message);
	}


	public WriteException(String message, Exception rootCause) {
		super(message, rootCause);
	}

}
