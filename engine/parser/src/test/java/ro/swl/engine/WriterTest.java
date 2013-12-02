package ro.swl.engine;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.swl.engine.writer.IdGenerator;
import ro.swl.engine.writer.TagWriter;


public class WriterTest extends AbstractTest {

	private SAXParser saxParser;
	private TestHandler handler;
	protected TagWriter writer;


	public WriterTest() {
		super();
	}


	@Before
	public void setUpXmlParser() throws ParserConfigurationException, SAXException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		saxParser = factory.newSAXParser();
		writer = new TagWriter();
		IdGenerator.keys = new HashSet<String>();

	}


	public void parse(TagWriter buffer) {
		parse(buffer.toString());
	}


	public void parse(String content) {
		String toParse = "<?xml version=\"1.0\"?>  <headerTag> " + content + "</headerTag>";
		InputStream stream = IOUtils.toInputStream(toParse);
		handler = new TestHandler();
		try {
			saxParser.parse(stream, handler);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Element getElement(String name, int idx) {

		Element e = handler.getElements().get(idx);
		if (!e.getName().equals(name)) {
			fail("No element found with name '" + name + "' at index " + idx);
		}

		return e;
	}

	public static class TestHandler extends DefaultHandler {

		private LinkedList<Element> elementRegistry = new LinkedList<Element>();

		private LinkedList<Element> elements = new LinkedList<Element>();


		@Override
		public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
			Element element = new Element(qName);
			for (int i = 0; i <= attrs.getLength(); i++) {
				if (attrs.getQName(i) == null)
					continue;
				element.addAttribute(attrs.getQName(i), attrs.getValue(i));
			}
			element.setParent(elementRegistry.peekLast());
			elementRegistry.add(element);
			elements.add(element);
		}


		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Element last = elementRegistry.removeLast();
			last.setClosed(true);
		}


		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			elements.peekLast().addTextContent(new String(ch, start, length));
		}


		public List<Element> getElements() {
			return elements;
		}
	}

	/**
	 * a structure representing an XML element which provides assertions on
	 * content and syntax
	 * 
	 * @author VictorBucutea
	 * 
	 */
	public static class Element {

		private String textContent = "";
		private String trailingText = "";
		private String name;
		private Map<String, String> attrs;
		private boolean closed;
		private Element parent;


		public Element(String name) {
			this.name = name;
			this.attrs = new HashMap<String, String>();
		}


		public void setParent(Element peekLast) {
			parent = peekLast;
		}


		public void addAttribute(String qName, String value) {
			this.attrs.put(qName, value);
		}


		public String getName() {
			return name;
		}


		public String getTextContent() {
			return textContent;
		}


		public void addTextContent(String textContent) {
			if (closed) {
				trailingText = textContent;
			} else {
				this.textContent = textContent;
			}
		}


		public void assertNewLineAfterClose() {
			assertTrue(trailingText.contains("\n"));
		}


		public void assertNoNewLineAfterClose() {
			assertFalse(trailingText.contains("\n"));
		}


		public void assertIndentAfterClose(int indentAmount) {
			String tab = "";

			for (int i = 0; i < indentAmount; i++) {
				tab += "\t";
			}

			assertTrue(trailingText.contains(tab));
		}


		public void assertDoesNotContainAttribute(String name) {
			assertFalse("Attribute '" + name + "' found", attrs.containsKey(name));
		}


		public String getAttribute(String attrName) {
			if (!attrs.containsKey(attrName)) {
				fail("The attribute " + attrName + " was not found.");
			}

			return attrs.get(attrName);
		}


		public void assertContainsAttribute(String name, String value) {
			boolean hasName = attrs.containsKey(name);
			if (!hasName) {
				fail("The attribute '" + name + "' was not found.");
			}

			boolean hasNameAndValue = attrs.get(name).equals(value);
			if (!hasNameAndValue) {
				fail("The attribute '" + name + "' was found, but its value is '" + attrs.get(name) + "'");
			}
		}


		public void assertContainsText(String string) {
			assertEquals("Body text is not what expected", string, textContent.trim());
		}


		public void assertNewLineAfterOpenTag() {
			assertTrue(textContent.contains("\n"));
		}


		public void assertNoNewLineAfterOpenTag() {
			assertFalse(textContent.contains("\n"));
		}


		public void assertIndentAfterOpenTag(int indentAmount) {
			String tab = "";

			for (int i = 0; i < indentAmount; i++) {
				tab += "\t";
			}

			assertTrue(textContent.contains(tab));
		}


		public void assertTrailingText(String string) {
			assertEquals("Trailing text is not what expected", string, trailingText.trim());
		}


		public void assertHasParent(Element el) {
			assertTrue("Parent is not " + el, parent == el);
		}


		public void setClosed(boolean closed) {
			this.closed = closed;
		}


		@Override
		public String toString() {
			return "!Start -- <" + name + " " + attrs + ">" + textContent + "</" + name + ">" + trailingText
					+ " -- End!";

		}

	}
}