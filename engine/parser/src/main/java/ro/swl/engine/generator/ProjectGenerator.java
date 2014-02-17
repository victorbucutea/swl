package ro.swl.engine.generator;

import static ro.swl.engine.util.FileUtil.listFilesOrderedByTypeAndName;

import java.io.File;
import java.util.List;

import ro.swl.engine.generator.javaee.factory.JavaEEResourceFactory;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.writer.ui.WriteException;


public class ProjectGenerator {

	private ProjectRoot root;
	private GenerationContext ctxt;
	private ResourceFactory resourceFactory;
	private List<Technology> technologies;



	public ProjectGenerator(GenerationContext ctxt, List<Technology> technologies) {
		this.ctxt = ctxt;
		this.technologies = technologies;
	}


	public void generate(ASTSwdlApp appModel) throws GenerateException {

		File templateRootFolder = ctxt.getTemplateRootDir();

		// TODO move validation outside generate cycle
		if (!templateRootFolder.exists() || !templateRootFolder.isDirectory()) {
			throw new GenerateException(templateRootFolder + " does not exist or is not a directory");
		}

		resourceFactory = new JavaEEResourceFactory(appModel, ctxt);
		root = resourceFactory.createRootResource(templateRootFolder);


		generateResourceTree(root, templateRootFolder);

	}


	public void enhance(ASTSwdlApp appModel) throws GenerateException {
		for (Technology tech : technologies) {
			tech.enhance(root, appModel);
			enhanceResourceTree(root, appModel, tech);
		}
	}


	public void write(ASTSwdlApp appModel) throws WriteException {
		root.write(ctxt);
	}


	private void generateResourceTree(Resource parent, File folder) throws GenerateException {
		List<File> files = listFilesOrderedByTypeAndName(folder);
		for (File f : files) {
			List<? extends Resource> childRes = resourceFactory.createResource(parent, f);

			parent.addChildren(childRes);

			if (f.isDirectory()) {
				for (Resource r : childRes) {
					r.registerStateInContext(ctxt);
					generateResourceTree(r, f);
					r.unregisterStateInContext(ctxt);
				}
			}

		}
	}



	private void enhanceResourceTree(Resource res, ASTSwdlApp appModel, Technology tech) throws GenerateException {
		for (Resource r : res.getChildren()) {
			tech.enhance(r, appModel);
			enhanceResourceTree(r, appModel, tech);
		}
	}


	public ProjectRoot getProjectRoot() {
		return root;
	}


}
