package ro.swl.engine.writer.template.test;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import ro.swl.engine.generator.java.model.Annotation;


public class AnnotationTests {


	@Test
	public void simpleAnnotation() throws Exception {

		Annotation ann = new Annotation("javax.persistence.Column");
		assertEquals("@Column", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "COLUMN");
		assertEquals("@Column(name = \"COLUMN\")", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "true");
		assertEquals("@Column(name = true)", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "3");
		assertEquals("@Column(name = 3)", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("value", "someString");
		assertEquals("@Column(\"someString\")", ann.toJavaRepresentation());
	}


	@Test
	public void annPropertyArrayLiteral() throws Exception {

		Annotation ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "COLUMN");
		ann.addPropertyLiteral("name", "COLUMN2");
		assertEquals("@Column(name = {\"COLUMN\" , \"COLUMN2\"})", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "true");
		ann.addPropertyLiteral("name", "false");
		assertEquals("@Column(name = {true , false})", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("name", "3");
		ann.addPropertyLiteral("name", "4");
		assertEquals("@Column(name = {3 , 4})", ann.toJavaRepresentation());

		ann = new Annotation("javax.persistence.Column");
		ann.addPropertyLiteral("value", "someString");
		ann.addPropertyLiteral("value", "someString2");
		assertEquals("@Column({\"someString\" , \"someString2\"})", ann.toJavaRepresentation());
	}


	@Test
	public void annPropertyType() throws Exception {

		Annotation ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("cascade", "javax.persistence.CascadeType.ALL");
		ann.addProperty("orphanRemoval", "true");
		assertEquals("@OneToMany(orphanRemoval = true , cascade = CascadeType.ALL)", ann.toJavaRepresentation());


		ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("value", "javax.persistence.CascadeType.ALL");
		ann.addPropertyLiteral("orphanRemoval", "true");
		assertEquals("@OneToMany(orphanRemoval = true , value = CascadeType.ALL)", ann.toJavaRepresentation());


		ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("value", "javax.persistence.CascadeType.ALL");
		assertEquals("@OneToMany(CascadeType.ALL)", ann.toJavaRepresentation());
	}


	@Test
	public void annPropertyTypeArray() throws Exception {
		Annotation ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("cascade", "javax.persistence.CascadeType.ALL");
		ann.addProperty("cascade", "javax.persistence.CascadeType.UPDATE");
		assertEquals("@OneToMany(cascade = {CascadeType.ALL , CascadeType.UPDATE})", ann.toJavaRepresentation());


		ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("value", "javax.persistence.CascadeType.ALL");
		ann.addProperty("value", "javax.persistence.CascadeType.UPDATE");
		ann.addPropertyLiteral("orphanRemoval", "true");
		assertEquals("@OneToMany(orphanRemoval = true , value = {CascadeType.ALL , CascadeType.UPDATE})",
				ann.toJavaRepresentation());
	}


	/**
	 * @NamedQueries(value = {
	 * @NamedQuery(name = CV.ALL, query = "Select j from CV j"),
	 * @NamedQuery(name = CV.FIRST_NAME, query =
	 *                  "Select j from CV j where j.firstName = :firstName"),
	 *                  .....
	 * @throws Exception
	 */
	@Test
	public void annPropertyAnnotation() throws Exception {

		Annotation ann = new Annotation("javax.persistence.NamedQueries");
		ann.addPropertyAnnotation("value", "javax.persistence.NamedQuery");
		ann.addProperty("orphanRemoval", "true");
		assertEquals("@NamedQueries(orphanRemoval = true , value = @NamedQuery)", ann.toJavaRepresentation());


		ann = new Annotation("javax.persistence.NamedQueries");

		Annotation namedQry1 = new Annotation("javax.persistence.NamedQuery");
		namedQry1.addProperty("name", "ro.sft.somepkg.CV.ALL");
		namedQry1.addPropertyLiteral("query", "Select j from CV j");

		Annotation namedQry2 = new Annotation("javax.persistence.NamedQuery");
		namedQry2.addProperty("name", "ro.sft.somepkg.CV.FIRST_NAME");
		namedQry2.addPropertyLiteral("query", "Select j from CV j where j.firstName = :firstName");


		ann.addPropertyAnnotation("value", namedQry1);
		ann.addPropertyAnnotation("value", namedQry2);
		assertEquals(
				"@NamedQueries({@NamedQuery(query = \"Select j from CV j\" , name = CV.ALL) , "
						+ "@NamedQuery(query = \"Select j from CV j where j.firstName = :firstName\" , name = CV.FIRST_NAME)})",
				ann.toJavaRepresentation());


		ann = new Annotation("javax.persistence.NamedQueries");

		namedQry1 = new Annotation("javax.persistence.NamedQuery");
		namedQry1.addProperty("name", "ro.sft.somepkg.CV.ALL");
		namedQry1.addPropertyLiteral("query", "Select j from CV j");

		namedQry2 = new Annotation("javax.persistence.NamedQuery");
		namedQry2.addProperty("name", "ro.sft.somepkg.CV.FIRST_NAME");
		namedQry2.addPropertyLiteral("query", "Select j from CV j where j.firstName = :firstName");


		ann.addPropertyAnnotation("notValue", namedQry1);
		ann.addPropertyAnnotation("notValue", namedQry2);
		assertEquals(
				"@NamedQueries(notValue = {@NamedQuery(query = \"Select j from CV j\" , name = CV.ALL) , "
						+ "@NamedQuery(query = \"Select j from CV j where j.firstName = :firstName\" , name = CV.FIRST_NAME)})",
				ann.toJavaRepresentation());

	}

}
