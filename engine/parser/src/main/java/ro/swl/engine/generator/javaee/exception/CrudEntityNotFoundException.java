package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class CrudEntityNotFoundException extends CreateException {

	private static final long serialVersionUID = -8009122730344161786L;


	public CrudEntityNotFoundException(String entityName, String serviceName) {
		super("Entity " + entityName + " referenced in service " + serviceName + " is not valid.");
	}



}
