package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class DuplicateDeclaredRelation extends GenerateException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4280813229147733855L;


	public DuplicateDeclaredRelation(String srcEntity, String destEntity, String srcField, String destField) {
		super("Field " + srcEntity + "." + srcField + " and " + destEntity + "." + destField
				+ " reference each other via "
				+ "the '->' indicator. This is not acceptable since owning part cannot be established.");
	}
}
