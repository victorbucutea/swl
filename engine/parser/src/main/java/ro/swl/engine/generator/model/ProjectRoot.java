package ro.swl.engine.generator.model;

import java.io.File;


public class ProjectRoot extends Resource {

	public static final String ID = "__project__";
	private String projectName;


	public ProjectRoot(String projectName, File templateFile) {
		super(null, templateFile);
		this.projectName = projectName;
	}


	@Override
	public String getOutputFileName() {
		return projectName;
	}

}
