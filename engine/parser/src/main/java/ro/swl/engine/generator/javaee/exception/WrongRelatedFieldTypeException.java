package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class WrongRelatedFieldTypeException extends CreateException {

	private static final long serialVersionUID = 1152707595679874547L;


	public WrongRelatedFieldTypeException(String srcField, String srcType, String entity, String field) {
		super("The field " + srcField + " relates to " + entity + "." + field + " but " + entity + "." + field
				+ " is not of type " + srcType + ".");
	}

}
