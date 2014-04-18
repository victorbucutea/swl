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
	private CreationContext ctxt;
	private ResourceFactory resourceFactory;
	private List<Technology> technologies;
	private Skeleton skeleton;


	public ProjectGenerator(Skeleton skeleton, List<Technology> technologies) {
		this.ctxt = new CreationContext();
		this.technologies = technologies;
		this.skeleton = skeleton;
	}


	public void create(ASTSwdlApp appModel) throws CreateException {
		resourceFactory = new JavaEEResourceFactory(appModel, ctxt);
		File skeletonRootFolder = skeleton.getSkeletonInstanceDir();

		root = resourceFactory.createRootResource(skeletonRootFolder);
		createResourceTree(root, skeletonRootFolder);
	}


	private void createResourceTree(Resource parent, File folder) throws CreateException {
		List<File> files = listFilesOrderedByTypeAndName(folder);
		for (File f : files) {
			List<? extends Resource> childRes = resourceFactory.createResource(parent, f);

			parent.addChildren(childRes);

			for (Resource r : childRes) {
				r.registerState(ctxt);
				createResourceTree(r, f);
				r.unregisterState(ctxt);
			}

		}
	}


	public void enhance(ASTSwdlApp appModel) throws CreateException {
		for (Technology tech : technologies) {
			tech.enhance(root, appModel);
			enhanceResourceTree(root, appModel, tech);
		}
	}


	private void enhanceResourceTree(Resource res, ASTSwdlApp appModel, Technology tech) throws CreateException {
		for (Resource r : res.getChildren()) {
			tech.enhance(r, appModel);
			enhanceResourceTree(r, appModel, tech);
		}
	}


	public void write(ASTSwdlApp appModel) throws WriteException {
		writeResourceTree(appModel, root);
	}


	private void writeResourceTree(ASTSwdlApp appModel, Resource res) throws WriteException {
		res.write();
		for (Resource child : res.getChildren()) {
			writeResourceTree(appModel, child);
		}

	}


	public ProjectRoot getProjectRoot() {
		return root;
	}


}
