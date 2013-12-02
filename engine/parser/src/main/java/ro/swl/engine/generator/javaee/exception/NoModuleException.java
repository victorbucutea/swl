package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class NoModuleException extends GenerateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2955696756219295457L;


	public NoModuleException() {
		super("Cannot create entities which are not part of any module. Add a '__module__' folder");
	}

}
