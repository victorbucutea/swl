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
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("SomeClass", cname.getFqName());
		assertEquals("SomeClass", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("int");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("int", cname.getFqName());
		assertEquals("int", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("int[]");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("int[]", cname.getFqName());
		assertEquals("int[]", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertTrue(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("byte");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("byte", cname.getFqName());
		assertEquals("byte", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("long");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("long", cname.getFqName());
		assertEquals("long", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("double");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("double", cname.getFqName());
		assertEquals("double", cname.getSimpleName());
		assertFalse(cname.isObject());
		assertFalse(cname.isArray());
		assertTrue(cname.isPrimitive());

		cname = new QualifiedClassName("randomString");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("randomString", cname.getFqName());
		assertEquals("randomString", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("true");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("true", cname.getFqName());
		assertEquals("true", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}


	@Test
	public void qualifiedClass() throws GenerateException {

		QualifiedClassName cname = new QualifiedClassName("javax.persistence.Column");
		assertEquals("javax.persistence.Column", cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("javax.persistence.Column", cname.getFqName());
		assertEquals("Column", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}


	@Test
	public void innerClasses() throws GenerateException {

		QualifiedClassName cname = new QualifiedClassName("javax.persistence.TemporalType.TIMESTAMP");
		assertEquals("javax.persistence.TemporalType", cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("javax.persistence.TemporalType.TIMESTAMP", cname.getFqName());
		assertEquals("TemporalType.TIMESTAMP", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());


		cname = new QualifiedClassName("TemporalType.TIMESTAMP");
		assertNull(cname.getImport());
		assertNull(cname.getParameterType());
		assertEquals("TemporalType.TIMESTAMP", cname.getFqName());
		assertEquals("TemporalType.TIMESTAMP", cname.getSimpleName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

	}


	@Test
	public void parameterizedClasses() throws GenerateException {
		QualifiedClassName cname = new QualifiedClassName("java.util.Set<SomeClass>");
		assertEquals("java.util.Set", cname.getImport());
		assertEquals("SomeClass", cname.getParameterType());
		assertEquals("java.util.Set", cname.getFqName());
		assertEquals("Set", cname.getSimpleName());
		assertEquals("Set<SomeClass>", cname.getParameterizedName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());

		cname = new QualifiedClassName("java.util.List<SomeClass>");
		assertEquals("java.util.List", cname.getImport());
		assertEquals("SomeClass", cname.getParameterType());
		assertEquals("java.util.List", cname.getFqName());
		assertEquals("List", cname.getSimpleName());
		assertEquals("List<SomeClass>", cname.getParameterizedName());
		assertTrue(cname.isObject());
		assertFalse(cname.isArray());
		assertFalse(cname.isPrimitive());
	}

}
