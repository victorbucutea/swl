package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class RelatedEntityNotFoundException extends CreateException {

	public RelatedEntityNotFoundException(String fieldCls) {
		super("Cannot find related type '" + fieldCls + "' .");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 11123L;



}
