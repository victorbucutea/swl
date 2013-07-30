package ro.swl.engine.writer;

public interface WritingComponent {

	public void beginBodyDeclaration(Writer writer) throws WriteException;

	public void endBodyDeclaration(Writer writer) throws WriteException;

	public void writeAttributes(Writer writer) throws WriteException;

	public void writeCssStyles(Writer writer) throws WriteException;

	public void endBody(Writer writer) throws WriteException;

	public void writeLabel(Writer writer) throws WriteException;
}
