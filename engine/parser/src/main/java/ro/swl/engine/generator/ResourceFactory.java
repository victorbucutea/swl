package ro.swl.engine.generator;

import static java.util.Arrays.asList;
import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import ro.swl.engine.generator.model.BinaryFileResource;
import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.FolderResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class ResourceFactory {

	public static final String[] TEXT_FILE_EXTENSIONS = { "txt", "xml", "java", "properties", "html", "js", "css" };

	private ASTSwdlApp appModel;
	private CreationContext ctxt;



	public ResourceFactory(ASTSwdlApp appmodel, CreationContext generationContext) {
		this.appModel = appmodel;
		this.ctxt = generationContext;
	}


	public ProjectRoot createRootResource(File templateFile) {
		return new ProjectRoot(getGlobalCtxt().getProjectName(), templateFile);
	}


	public List<? extends Resource> createResource(Resource parent, File templateFile) throws CreateException {
		if (templateFile.isDirectory()) {
			return asList(new FolderResource(parent, templateFile));
		} else if (isTextFile(templateFile)) {
			return asList(new FileResource(parent, templateFile));
		} else {
			return asList(new BinaryFileResource(parent, templateFile));
		}
	}


	private boolean isTextFile(File templateFile) {
		return FilenameUtils.isExtension(templateFile.getName(), TEXT_FILE_EXTENSIONS);
	}


	public ASTSwdlApp getAppModel() {
		return this.appModel;
	}



	public CreationContext getCtxt() {
		return ctxt;
	}

}
