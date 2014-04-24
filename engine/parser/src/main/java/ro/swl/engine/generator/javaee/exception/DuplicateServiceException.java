package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class DuplicateServiceException extends CreateException {

	private static final long serialVersionUID = 474739719023908546L;


	public DuplicateServiceException(String cause) {
		super("Service " + cause + " is duplicate !");
	}

}
