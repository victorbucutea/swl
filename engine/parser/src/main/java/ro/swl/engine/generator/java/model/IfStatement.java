package ro.swl.engine.generator.java.model;



public class IfStatement extends CompoundStatement {

	private String condition;


	public IfStatement(String condition) {
		this(condition, DFLT_INDENTATION);
	}


	public IfStatement(String condition, int childIndentation) {
		super(childIndentation);
		this.condition = condition;
	}


	@Override
	public String toJavaRepresentation() {
		StringBuilder blder = renderChildren();
		//@formatter:off
		return "if ( " + condition + " ) {" +CR_LF +
					blder +
			defaultIndentation+"}" +CR_LF;
		//@formatter:on
	}
}