package ro.swl.engine.generator;

import static java.util.Arrays.asList;
import static ro.swl.engine.util.FileUtil.listFilesOrderedByTypeAndName;

import java.io.File;
import java.util.List;

import ro.swl.engine.generator.javaee.enhancer.JPATechnology;
import ro.swl.engine.generator.javaee.enhancer.JaxRSTechnology;
import ro.swl.engine.generator.javaee.factory.JavaEEResourceFactory;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public class ProjectGenerator {

	private ProjectRoot root;
	private GenerationContext ctxt;
	private ResourceFactory resourceFactory;
	private List<Technology> technologies;


	public ProjectGenerator(GenerationContext ctxt) {
		this.ctxt = ctxt;
		this.technologies = asList(new JPATechnology(ctxt), new JaxRSTechnology(ctxt));
	}


	public void generate(ASTSwdlApp appModel, File templateRootFolder) throws GenerateException {

		if (!templateRootFolder.exists() || !templateRootFolder.isDirectory()) {
			throw new GenerateException(templateRootFolder + " does not exist or is not a directory");
		}

		resourceFactory = new JavaEEResourceFactory(appModel, ctxt);
		root = resourceFactory.createRootResource(templateRootFolder);


		generateResourceTree(root, templateRootFolder);

	}


	private void generateResourceTree(Resource parent, File folder) throws GenerateException {
		List<File> files = listFilesOrderedByTypeAndName(folder);
		for (File f : files) {
			List<? extends Resource> childRes = resourceFactory.createResource(parent, f);

			parent.addChildren(childRes);

			if (f.isDirectory()) {
				parent.registerStateInContext(ctxt);
				for (Resource r : childRes) {
					generateResourceTree(r, f);
				}
				parent.unregisterStateInContext(ctxt);
			}

		}
	}



	public void enhance(ASTSwdlApp appModel) throws GenerateException {
		enhanceResourceTree(root, appModel);
	}


	private void enhanceResourceTree(Resource res, ASTSwdlApp appModel) throws GenerateException {
		for (Resource r : res.getChildren()) {
			for (Technology tech : technologies) {
				tech.enhance(r, appModel);
			}

			enhanceResourceTree(r, appModel);
		}
	}


	public void write(File destinationRootFolder) throws GenerateException {
	}



	public ProjectRoot getProjectRoot() {
		return root;
	}


}
