package ro.swl.engine.parser;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.Signal;

public class SWLNode extends SimpleNode {

	private String image;

	public SWLNode(int i) {
		super(i);
	}

	public SWLNode(SWL swl, int i) {
		super(swl, i);
	}

	public List<Component> getChildComponents() {
		return getChildNodesOfType(Component.class, true);
	}

	public boolean hasChildComponents() {
		return !getChildComponents().isEmpty();
	}

	public void receiveSignal(Signal signal) {
		// override if you want to listen to signals 
	}

	public <T extends SWLNode> void broadcastSignal(Class<T> cls, Signal signal) {
		for (T comp : getChildNodesOfType(cls, true)) {
			comp.receiveSignal(signal);
			comp.broadcastSignal(cls, signal);
		}
	}

	public <T extends SWLNode> List<String> getImageOfChildNodesOfType(Class<T> cls, boolean recursive) {
		List<T> childNodes = getChildNodesOfType(cls, recursive);
		List<String> result = new ArrayList<String>();

		for (T childNode : childNodes) {
			result.add(childNode.getImage());
		}

		return result;
	}

	public <T extends SWLNode> List<T> getChildNodesOfType(Class<T> cls, boolean recursive) {
		List<T> children = new ArrayList<T>();
		addChildren(this, children, recursive, cls);
		return children;
	}

	public <T extends SWLNode> T getFirstChildNodeOfType(Class<T> cls, boolean recursive) {
		List<T> children = new ArrayList<T>();
		addChildren(this, children, recursive, cls);
		if (children.isEmpty()) {
			return null;
		}

		return children.get(0);
	}

	protected <P extends SWLNode, C extends SWLNode> List<String> getImageOfChildNodesWithParentOfType(
			Class<C> childClass, Class<P> parentClass) {
		List<String> clsNames = new ArrayList<String>();
		List<P> descriptions = getChildNodesOfType(parentClass, true);

		for (P desc : descriptions) {
			clsNames.addAll(desc.getImageOfChildNodesOfType(childClass, true));
		}
		return clsNames;
	}

	@SuppressWarnings("unchecked")
	private <T> void addChildren(Node n, List<T> children, boolean recursive, Class<T> type) {
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			Node child = n.jjtGetChild(i);
			if (type.isAssignableFrom(child.getClass())) {
				children.add((T) child);
			}

			if (recursive) {
				addChildren(child, children, recursive, type);
			}
		}
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
