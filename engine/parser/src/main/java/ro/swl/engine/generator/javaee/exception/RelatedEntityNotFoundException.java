package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class RelatedEntityNotFoundException extends GenerateException {

	public RelatedEntityNotFoundException(String fieldCls) {
		super("Cannot find related entity '" + fieldCls + "' in any of the declared modules.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



}
