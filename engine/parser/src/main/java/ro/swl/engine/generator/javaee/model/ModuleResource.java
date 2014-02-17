package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;


public class ModuleResource extends Resource {

	private String moduleName;


	public ModuleResource(Resource parent, File templateFile) {
		super(parent, templateFile);
	}


	@Override
	public void registerStateInContext(GenerationContext ctxt) {
		ctxt.setCurrentModule(moduleName);
	}


	public String getModuleName() {
		return moduleName;
	}


	@Override
	public String getOutputFileName() {
		return moduleName;
	}


	public void setModuleName(String name) {
		this.moduleName = name;
	}


	@Override
	public String toString() {
		return moduleName;
	}

}
