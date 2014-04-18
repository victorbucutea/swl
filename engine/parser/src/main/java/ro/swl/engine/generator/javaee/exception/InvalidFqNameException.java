package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class InvalidFqNameException extends CreateException {

	public InvalidFqNameException(String fqName) {
		super(" Cannot create a class name with no capital letters :" + fqName + ".");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2310691570914229586L;

}
