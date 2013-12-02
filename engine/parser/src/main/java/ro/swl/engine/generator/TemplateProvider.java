package ro.swl.engine.generator;

import java.io.File;
import java.io.Writer;


public interface TemplateProvider {

	public String mergeName(String name);


	public void mergeTemplate(File template, Writer writer);

}
