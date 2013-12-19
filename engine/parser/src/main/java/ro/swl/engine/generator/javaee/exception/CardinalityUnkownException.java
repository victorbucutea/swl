package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class CardinalityUnkownException extends GenerateException {

	public CardinalityUnkownException(String entity, String field) {
		super("Entity " + entity + " has a relation of field " + field + " with unkown field. "
				+ "Cannot determine whether it is a one-to-one (specified by a '*') "
				+ " or a one-to-many (specified by a '&')");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8088852682309602037L;

}
