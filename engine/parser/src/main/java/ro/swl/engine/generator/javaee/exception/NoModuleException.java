package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;


public class NoModuleException extends CreateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2955696756219295457L;


	public NoModuleException() {
		super("Cannot create entities/services which are not part of any module. Put them in a '__module__' folder");
	}


	public NoModuleException(String expl) {
		super(expl);
	}

}
