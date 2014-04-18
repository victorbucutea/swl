package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class EmptyFqNameException extends CreateException {

	public EmptyFqNameException() {
		super(" Cannot create a class with an empty FQ name");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2310691570934229586L;

}
