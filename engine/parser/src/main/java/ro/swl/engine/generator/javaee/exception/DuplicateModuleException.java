package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class DuplicateModuleException extends CreateException {

	private static final long serialVersionUID = 2268670396267474425L;


	public DuplicateModuleException(String cause) {
		super("Module " + cause + " is duplicate!");
	}

}
