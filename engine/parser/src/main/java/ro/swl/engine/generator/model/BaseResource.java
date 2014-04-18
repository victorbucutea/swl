package ro.swl.engine.generator.model;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.CreationContext;
import ro.swl.engine.writer.ui.WriteException;


/**
 * Base class for a Resource. Will provide tree operation methods (
 * {@link #getParent()} , {@link #addChild(Resource)} , etc )
 * and Resource lifecycle methods ( {@link #write()})
 * 
 * @author VictorBucutea
 * 
 */
abstract class BaseResource {

	protected Resource parent;
	private List<Resource> children = new ArrayList<Resource>();


	public abstract void write() throws WriteException;


	public void addChild(Resource resource) {
		children.add(resource);
	}


	public Resource getParent() {
		return parent;
	}


	public Resource getChild(int idx) {
		return children.get(idx);
	}


	@SuppressWarnings("unchecked")
	public <T extends Resource> T getChildCast(int idx) {
		return (T) children.get(idx);
	}


	public List<Resource> getChildren() {
		return children;
	}


	public void addChildren(List<? extends Resource> children) {
		for (Resource res : children) {
			addChild(res);
		}
	}



	public void printTree(Writer writer) throws IOException {
		writer.append(toString() + "\n");

		if (isEmpty(children))
			return;

		for (BaseResource res : children) {
			res.printTree(writer);
		}

	}


	public void registerState(CreationContext ctxt) {
	}


	public void unregisterState(CreationContext ctxt) {
	}

}
