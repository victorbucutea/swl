package ro.swl.engine.generator.javaee.model;

import java.io.File;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.model.Resource;


public class ModuleResource extends Resource {

	public static String ID = "__module__";

	private String moduleName;


	public ModuleResource(Resource parent, File templateFile) {
		super(parent, templateFile);
	}


	@Override
	public void registerState(GenerationContext ctxt) {
		ctxt.setCurrentModule(moduleName);
	}


	@Override
	public void unregisterState(GenerationContext ctxt) {
		ctxt.setCurrentModule("");
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String name) {
		this.moduleName = name;
	}


	@Override
	public String getOutputFileName() {
		String originalFileName = super.getOutputFileName();
		return originalFileName.replaceAll(ID, getModuleName());
	}
}
