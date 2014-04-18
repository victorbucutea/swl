package ro.swl.engine.writer;

import ro.swl.engine.generator.javaee.model.WebXml;
import ro.swl.engine.writer.ui.WriteException;


public class XmlTemplateWriter extends VelocityTemplateWriter {

	public XmlTemplateWriter(String template, WebXml model) throws WriteException {
		super(template, model);
	}



}
