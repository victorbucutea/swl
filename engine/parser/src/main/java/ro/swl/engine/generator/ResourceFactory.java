package ro.swl.engine.generator;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;

import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.FolderResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class ResourceFactory {

	private ASTSwdlApp appModel;
	private GenerationContext ctxt;


	public ResourceFactory(ASTSwdlApp appmodel, GenerationContext generationContext) {
		this.appModel = appmodel;
		this.ctxt = generationContext;
	}


	public ProjectRoot createRootResource(File templateFile) {
		return new ProjectRoot(templateFile);
	}


	public List<? extends Resource> createResource(Resource parent, File templateFile) throws GenerateException {
		if (templateFile.isDirectory()) {
			return asList(new FolderResource(parent, templateFile));
		} else {
			return asList(new FileResource(parent, templateFile));
		}
	}


	public ASTSwdlApp getAppModel() {
		return this.appModel;
	}



	public GenerationContext getCtxt() {
		return ctxt;
	}


	public GlobalContext getGlobalCtxt() {

	}

}
