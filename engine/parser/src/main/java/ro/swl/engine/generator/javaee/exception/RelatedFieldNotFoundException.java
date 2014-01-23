package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class RelatedFieldNotFoundException extends GenerateException {

	public RelatedFieldNotFoundException(String fieldCls, String field, String entity) {
		super("Cannot find related field '" + field + "' of type '" + fieldCls + "' in entity '" + entity);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 11123L;



}
