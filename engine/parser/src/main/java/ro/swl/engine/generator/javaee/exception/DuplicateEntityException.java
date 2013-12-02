package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class DuplicateEntityException extends GenerateException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4280813189147733855L;


	public DuplicateEntityException(String entityName) {
		super("Entity " + entityName + " is duplicate.");
	}
}
