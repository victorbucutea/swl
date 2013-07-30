package ro.swl.engine.writer;

/**
 * 
 * Any component which will display a label
 * 
 * @author VictorBucutea
 * 
 */
public interface LabeledComponent {

	public void writeLabel(Writer writer) throws WriteException;

}
