package ro.swl.engine.generator.model;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.leftPad;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.writer.ui.WriteException;


/**
 * Base class for a Resource. Will provide tree operation methods (
 * {@link #getParent()} , {@link #addChild(Resource)} , etc )
 * and Resource lifecycle methods ( {@link #write(GenerationContext)}
 * {@link #writeChildren(GenerationContext)} ,etc. )
 * 
 * @author VictorBucutea
 * 
 */
abstract class BaseResource {

	protected Resource parent;
	private List<Resource> children = new ArrayList<Resource>();


	public void write() throws WriteException {
		writeSelf();
		writeChildren();
	}


	protected abstract void writeSelf() throws WriteException;


	protected void writeChildren() throws WriteException {
		for (Resource child : children) {
			child.write();
		}
	}


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



	public void printTree(Writer writer, int depthLvl) throws IOException {
		String padding = leftPad("", depthLvl, "\t");
		writer.append(padding + " -> " + toString() + "\n\n");

		if (isEmpty(children))
			return;
		else
			depthLvl++;

		for (BaseResource res : children) {
			res.printTree(writer, depthLvl);
		}

	}


	public void registerState(GenerationContext ctxt) {
	}


	public void unregisterState(GenerationContext ctxt) {
	}

}
