package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class DuplicateEntityException extends CreateException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4280813189147733855L;


	public DuplicateEntityException(String entityName) {
		super("Entity " + entityName + " is duplicate.");
	}
}
