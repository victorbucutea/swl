package ro.swl.engine.generator.java.model;



public class ForStatement extends CompoundStatement {

	private Type itType;

	private String itVar;

	private Statement expression;


	public ForStatement(Type iteratingType, String iteratingVar, Statement expression) {
		super(DFLT_INDENTATION);
		this.itType = iteratingType;
		this.itVar = iteratingVar;
		this.expression = expression;
	}


	@Override
	public String render() {

		//@formatter:off
		return "for (" + itType.getSimpleClassName() + " " +itVar+ " : "+ expression.renderWithoutSemiCol()+") {\r\n" +
					 renderChildren() +
			defaultIndentation+"}\r\n";
		//@formatter:on
	}


	public Type getItType() {
		return itType;
	}



	public void setItType(Type itType) {
		this.itType = itType;
	}



	public String getItVar() {
		return itVar;
	}



	public void setItVar(String itVar) {
		this.itVar = itVar;
	}



	public Statement getExpression() {
		return expression;
	}



	public void setExpression(Statement expression) {
		this.expression = expression;
	}

}