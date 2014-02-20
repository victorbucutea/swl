package ro.swl.engine.parser;

import static org.apache.commons.lang3.StringUtils.remove;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.ui.Signal;


public class SWLNode extends SimpleNode {

	private String image;


	public SWLNode(int i) {
		super(i);
	}


	public SWLNode(SWL swl, int i) {
		super(swl, i);
	}


	public void addChild(SWLNode node) {
		jjtAddChild(node, jjtGetNumChildren());
	}


	public List<Component> getChildComponents() {
		return getChildNodesOfType(Component.class, true);
	}


	public boolean hasChildComponents() {
		return !getChildComponents().isEmpty();
	}


	public void receiveSignal(Signal signal) {
		// override to listen to signals 
	}


	public void broadcastSignal(Signal signal) {
		for (Component comp : getChildComponents()) {
			comp.receiveSignal(signal);
			comp.broadcastSignal(signal);
		}
	}


	public <T extends SWLNode> boolean hasParent(Class<T> cls) {
		SWLNode parent = getParent();
		return cls.isAssignableFrom(parent.getClass());
	}


	public SWLNode getParent() {
		return (SWLNode) jjtGetParent();
	}


	public Component getParentComponent() {
		SWLNode node = getParent();

		if (node == null)
			return null;

		if (node instanceof Component)
			return (Component) node;

		return node.getParentComponent();

	}


	public void setParent(Node node) {
		jjtSetParent(node);
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


	public <T extends SWLNode> List<T> getParentNodesOfType(Class<T> cls, boolean recursive) {
		List<T> parents = new ArrayList<T>();
		addParents(this, parents, recursive, cls);
		return parents;
	}


	public <T extends SWLNode> T getFirstParentNodeOfType(Class<T> cls, boolean recursive) {
		List<T> parents = getParentNodesOfType(cls, recursive);
		if (parents.isEmpty()) {
			return null;
		}
		return parents.get(0);
	}


	public <T extends SWLNode> List<T> getChildNodesOfType(Class<T> cls, boolean recursive) {
		List<T> children = new ArrayList<T>();
		addChildren(this, children, recursive, cls);
		return children;
	}


	public <T extends SWLNode> T getFirstChildNodeOfType(Class<T> cls, boolean recursive) {
		List<T> children = getChildNodesOfType(cls, recursive);
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
	private <T> void addChildren(Node n, List<T> childrenList, boolean recursive, Class<T> type) {
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			Node child = n.jjtGetChild(i);
			if (type.isAssignableFrom(child.getClass())) {
				childrenList.add((T) child);
			}

			if (recursive) {
				addChildren(child, childrenList, recursive, type);
			}
		}
	}


	@SuppressWarnings("unchecked")
	private <T> void addParents(Node n, List<T> parentsList, boolean recursive, Class<T> type) {
		Node parent = n.jjtGetParent();
		if (parent == null) {
			return;
		}

		if (type.isAssignableFrom(parent.getClass())) {
			parentsList.add((T) parent);
		}

		if (recursive) {
			addParents(parent, parentsList, recursive, type);
		}

	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public void setImageWithoutQuote(String image) {
		if (image == null)
			return;

		this.image = remove(image, '"').replace("'", "");
	}


	@Override
	public String toString() {
		return super.toString() + " - " + image;
	}


	public String getNodeName() {
		return SWLTreeConstants.jjtNodeName[id];
	}

}
