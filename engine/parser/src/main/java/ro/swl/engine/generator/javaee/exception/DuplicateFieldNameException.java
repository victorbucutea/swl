package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class DuplicateFieldNameException extends GenerateException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4280813149147733855L;


	public DuplicateFieldNameException(String entityName, String fieldName) {
		super("Entity " + entityName + " has duplicate field " + fieldName + ".");
	}
}
