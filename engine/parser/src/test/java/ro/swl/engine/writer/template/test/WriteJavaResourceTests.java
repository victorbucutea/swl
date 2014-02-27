package ro.swl.engine.writer.template.test;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.CompoundStatement;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.ForStatement;
import ro.swl.engine.generator.java.model.IfStatement;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.parser.ASTProperty;


public class WriteJavaResourceTests extends GeneratorTest {



	@Test
	public void fieldsTest() throws Exception {

		File projectRootFile = new File(generateDestDir, "fieldsTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot(projectRootFile);
		JavaResource<Type, Field> res = new JavaResource<Type, Field>(root, "SomeClass", "ro.sft.somepkg");
		root.addChild(res);


		Field f = new Field(createProp("prop1", "Customer"), "ro.sft.otherpkg");
		f.addAnotation(new Annotation("javax.persistence.Column"));

		Field f1 = new Field(createProp("prop2", "Customer"), "ro.sft.otherpkg");
		f1.addAnotation(createAnnotation());


		Field f2 = new Field(createProp("prop3", "Experience"), "ro.sft.pkg");
		f2.setHasGetter(true);
		f2.addGetterAnotation("org.codehaus.jackson.annotate.JsonIgnore");
		f2.setHasSetter(true);
		f2.addSetterAnnotation("org.codehaus.jackson.annotate.JsonIgnore");


		Field f3 = new Field(createProp("prop4", "int"), "");
		Field f4 = new Field(createProp("prop5", "Integer"), "java.lang");
		Field f5 = new Field(createProp("prop6", "Date"), "java.util");
		Field f6 = new Field(createProp("prop7", "byte[]"), "");
		Field f7 = new Field(createProp("prop8", "Long"), "");
		Field f8 = new Field(createProp("prop9", "Set<Something>"), "java.util");

		res.addProperty(f);
		res.addProperty(f1);
		res.addProperty(f2);
		res.addProperty(f3);
		res.addProperty(f4);
		res.addProperty(f5);
		res.addProperty(f6);
		res.addProperty(f7);
		res.addProperty(f8);
		root.write(ctxt);

		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertEquals(2, list.size());

		// TODO parse and test output 
	}


	@Test
	public void methodsTest() throws Exception {
		File projectRootFile = new File(generateDestDir, "methodsTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot(projectRootFile);
		JavaResource<Type, Field> res = new JavaResource<Type, Field>(root, "SomeClass", "ro.sft.somepkg");
		res.setName("SomeClass");

		res.addMethod(createMethod("method1"));


		Method method2 = createMethod("method2");
		method2.setReturnType(new Type("ro.sft.pkg.SomeClasss<Y>"));
		method2.addParameter("param1", new Type("java.lang.String"));
		method2.addParameter("param2", new Type("ro.sft.pkg.Class<X>"));
		res.addMethod(method2);



		root.write(ctxt);
		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertEquals(2, list.size());
	}


	@Test
	public void methodBodyTest() throws Exception {
		File projectRootFile = new File(generateDestDir, "methodsBodyTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot(projectRootFile);
		JavaResource<Type, Field> res = new JavaResource<Type, Field>(root, "SomeClass", "ro.sft.somepkg");
		root.addChild(res);
		res.setName("SomeClass");


		Method method2 = createMethod("method2");
		method2.setReturnType(new Type("ro.sft.pkg.SomeClasss<Y>"));
		method2.addParameter("param1", new Type("java.lang.String"));
		method2.addParameter("param2", new Type("ro.sft.pkg.Class<X>"));

		List<Statement> body = method2.getBody();

		CompoundStatement initSet = new IfStatement("field == null");
		initSet.addChildStmt(new Statement("return new HashSet<SomeClass>()", "java.util.HashSet"));

		CompoundStatement isInitIf = new IfStatement("isinitialized(x)");
		isInitIf.addChildStmt(new Statement("return x", ""));

		CompoundStatement emtpyIf = new IfStatement("someMethod()");

		Statement returnStmt = new Statement("return new HashSet<SomeOtherCls>()", "");


		Statement iteratingExpr = new Statement("collection.getItClasses()", "");
		Statement forStmt = new ForStatement(new Type("ro.sft.somepkg.ForIteratingClass"), "itVar", iteratingExpr);


		Statement simple = new Statement("Class y = new Class(s)", "");
		Statement simple2 = new Statement("someObject.someMethod()", "");

		body.add(initSet);
		body.add(isInitIf);
		body.add(emtpyIf);
		body.add(forStmt);
		body.add(simple);
		body.add(simple2);
		body.add(returnStmt);

		res.addMethod(method2);

		root.write(ctxt);
		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertEquals(2, list.size());
	}


	private Method createMethod(String string) throws GenerateException {
		Method m = new Method(string);

		m.addAnnotation(createAnnotation());
		return m;
	}


	private Annotation createAnnotation() throws GenerateException {
		Annotation ann = new Annotation("javax.persistence.OneToMany");
		ann.addProperty("cascade", "javax.persistence.CascadeType.ALL");
		ann.addProperty("orphanRemoval", "true");

		Annotation annProp = new Annotation("javax.persistence.SomeAnnotation");
		annProp.addPropertyLiteral("name", "someAnnName");

		ann.addPropertyAnnotation("prop", annProp);
		return ann;
	}


	private ASTProperty createProp(String name, String type) {
		ASTProperty prop = new ASTProperty(0);
		prop.setName(name);
		prop.setType(type);
		return prop;
	}


	@Override
	public List<Technology> getTechsUnderTest() {
		return new ArrayList<Technology>();
	}

}
