package ro.swl.engine.generator;

import java.io.File;
import java.io.Writer;


public class VelocityTemplateProvider implements TemplateProvider {

	public VelocityTemplateProvider() {
		//Velocity.init();
	}


	@Override
	public String mergeName(String name) {
		return null;
	}


	@Override
	public void mergeTemplate(File template, Writer writer) {
		System.out.println("merging file " + template);
	}

}
