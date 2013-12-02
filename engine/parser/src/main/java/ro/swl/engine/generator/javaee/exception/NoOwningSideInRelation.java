package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.GenerateException;


public class NoOwningSideInRelation extends GenerateException {

	public NoOwningSideInRelation(String entity, String field, String relatedField) {
		super("Entity " + entity + " has relation of field " + field + " with field " + relatedField
				+ " but no owning side (specified by a '*' as an entity property)");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8088852682309602037L;

}
