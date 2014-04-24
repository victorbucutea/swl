package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class InvalidServiceReferenceException extends CreateException {

	private static final long serialVersionUID = -3312694652707699678L;


	public InvalidServiceReferenceException(String extIfName, String service) {
		super("External interface " + extIfName + " references inexisting service " + service);
	}

}
