package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ro.swl.engine.generator.model.QualifiedClassName;


public class QualifiedClassNameTests {


	@Test
	public void nullOrEmptyTests() {

		try {
			new QualifiedClassName(null);
			fail("Should not be able to build a FQ Class name with null or empty");
		} catch (GenerateException e) {
		}

		try {
			new QualifiedClassName("");
			fail("Should not be able to build a FQ Class name with null or empty");
		} catch (GenerateException e) {
		}

		try {
			new QualifiedClassName(null, null);
			fail("Should not be able to build a FQ Class name with null or empty");
		} catch (GenerateException e) {
		}

		try {
			new QualifiedClassName("", null);
			fail("Should not be able to build a FQ Class name with null or empty");
		} catch (GenerateException e) {
		}

		try {
			new QualifiedClassName("", "");
			fail("Should not be able to build a FQ Class name with null or empty");
		} catch (GenerateException e) {
		}

	}


	@Test
	public void simpleClassesAndPrimitives() throws GenerateException {

		QualifiedClassName cname = new QualifiedClassName("SomeClass");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertEquals("SomeClass", cname.getFqName());
		assertEquals("SomeClass", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("int");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertEquals("int", cname.getFqName());
		assertEquals("int", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("int[]");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertEquals("int[]", cname.getFqName());
		assertEquals("int[]", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertTrue(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("byte");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("byte", cname.getFqName());
		assertEquals("byte", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("long");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("long", cname.getFqName());
		assertEquals("long", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("double");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("double", cname.getFqName());
		assertEquals("double", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("randomString");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("randomString", cname.getFqName());
		assertEquals("randomString", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("true");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("true", cname.getFqName());
		assertEquals("true", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}


	@Test
	public void qualifiedClass() throws GenerateException {

		QualifiedClassName cname = new QualifiedClassName("javax.persistence.Column");
		assertImportContains(cname, "javax.persistence.Column");
		assertImportSize(cname, 1);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("javax.persistence.Column", cname.getFqName());
		assertEquals("Column", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}


	@Test
	public void innerClasses() throws GenerateException {

		QualifiedClassName cname = new QualifiedClassName("javax.persistence.TemporalType.TIMESTAMP");
		assertImportContains(cname, "javax.persistence.TemporalType");
		assertImportSize(cname, 1);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("javax.persistence.TemporalType.TIMESTAMP", cname.getFqName());
		assertEquals("TemporalType.TIMESTAMP", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());


		cname = new QualifiedClassName("TemporalType.TIMESTAMP");
		assertImportSize(cname, 0);
		assertNull(cname.getParameterType());
		assertNull(cname.getFqParameterType());
		assertEquals("TemporalType.TIMESTAMP", cname.getFqName());
		assertEquals("TemporalType.TIMESTAMP", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

	}


	@Test
	public void parameterizedClasses() throws GenerateException {
		QualifiedClassName cname = new QualifiedClassName("java.util.Set<ro.sft.SomeClass>");
		assertImportContains(cname, "java.util.Set", "ro.sft.SomeClass");
		assertImportSize(cname, 2);
		assertEquals("SomeClass", cname.getParameterType());
		assertEquals("ro.sft.SomeClass", cname.getFqParameterType());
		assertEquals("java.util.Set", cname.getFqName());
		assertEquals("Set", cname.getSimpleName());
		assertEquals("Set<SomeClass>", cname.getParameterizedName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("java.util.List<SomeClass>");
		assertImportContains(cname, "java.util.List");
		assertImportSize(cname, 1);
		assertEquals("SomeClass", cname.getParameterType());
		assertEquals("SomeClass", cname.getFqParameterType());
		assertEquals("java.util.List", cname.getFqName());
		assertEquals("List", cname.getSimpleName());
		assertEquals("List<SomeClass>", cname.getParameterizedName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());


		cname = new QualifiedClassName("java.pkg.ParameterizedCls<SomeClass>");
		assertImportContains(cname, "java.pkg.ParameterizedCls");
		assertImportSize(cname, 1);
		assertEquals("SomeClass", cname.getParameterType());
		assertEquals("java.pkg.ParameterizedCls", cname.getFqName());
		assertEquals("ParameterizedCls", cname.getSimpleName());
		assertEquals("ParameterizedCls<SomeClass>", cname.getParameterizedName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}


	private void assertImportSize(QualifiedClassName cname, int size) {
		assertEquals(size, cname.getImports().size());
	}


	private void assertImportContains(QualifiedClassName cname, String... literalClsNames) {
		for (String imprt : literalClsNames) {
			assertTrue(cname.getImports().contains(imprt));
		}
	}

}
