package ro.swl.engine.writer.ui;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.inject.Inject;

import ro.swl.engine.parser.model.Component;
import ro.swl.engine.writer.i18n.I18nBundle;


public class IdGenerator {

	@Inject
	private I18nBundle bundle = new I18nBundle();

	@Inject
	//should be a singleton
	public static Set<String> keys;

	static {
		keys = new HashSet<String>();
	}


	public String generate(Component comp) throws WriteException {
		// default generation method is to walk up the parent tree and record each component on the way
		// e.g. screen X { horizontal_layout () { column () { input(someValue) } } } --> X.horizontal_layout.someValue
		StringBuffer key = new StringBuffer();

		appendParentPath(comp, key);

		appendCurrentComponentId(comp, key);

		assertKeyUnique(key);

		if (comp.hasExternalizableLabel()) {
			bundle.addProperty(key);
		}

		return key.toString();
	}


	private void assertKeyUnique(StringBuffer key) throws WriteException {
		if (keys.contains(key.toString())) {

			// a bit harsh but makes sure no duplicates EVER pop up.
			throw new WriteException("Duplicate key detected :" + key);
		}
		keys.add(key.toString());
	}


	private void appendCurrentComponentId(Component comp, StringBuffer key) throws WriteException {
		String currentId = comp.getImageWithIdx();
		if (isBlank(currentId)) {
			throw new WriteException("A component cannot have a blank or null image:" + comp);
		}

		appendValue(key, currentId);
	}



	private void appendParentPath(Component comp, StringBuffer key) {
		Iterator<Component> listIterator = createParentComponentList(comp).descendingIterator();

		while (listIterator.hasNext()) {
			Component currComp = listIterator.next();
			appendValue(key, currComp.getImageWithIdx());
		}
	}


	private void appendValue(StringBuffer buffer, String value) {
		if (isBlank(value))
			return;

		if (buffer.length() == 0)
			buffer.append(value);
		else
			buffer.append("." + value);

	}


	/**
	 * walks up the tree recording parent components in the hierarchy order.
	 * 
	 * @param comp
	 * @return
	 */
	private LinkedList<Component> createParentComponentList(Component comp) {
		LinkedList<Component> parentList = new LinkedList<Component>();
		Component parent = comp.getParentComponent();

		while (parent != null) {
			parentList.add(parent);
			parent = parent.getParentComponent();
		}

		return parentList;
	}
}
