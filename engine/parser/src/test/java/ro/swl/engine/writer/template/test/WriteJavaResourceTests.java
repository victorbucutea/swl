package ro.swl.engine.writer.template.test;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		JavaResource<Field> res = new JavaResource<Field>(root, "SomeClass", "ro.sft.somepkg");
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
		Field f8 = new Field(createProp("prop9", "Set<com.ibm.somepkg.Something>"), "java.util");
		Field f9 = new Field(createProp("PROP_10", "String"), "");
		f9.setInitializingExpression("\"PROP_10\"");

		res.addProperty(f);
		res.addProperty(f1);
		res.addProperty(f2);
		res.addProperty(f3);
		res.addProperty(f4);
		res.addProperty(f5);
		res.addProperty(f6);
		res.addProperty(f7);
		res.addProperty(f8);
		res.addStaticFinalProperty(f9);
		root.write();

		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertEquals(2, list.size());

		// TODO parse and assert output 
	}


	@Test
	public void methodsTest() throws Exception {
		File projectRootFile = new File(generateDestDir, "methodsTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot(projectRootFile);
		JavaResource<Field> res = new JavaResource<Field>(root, "SomeClass", "ro.sft.somepkg");
		res.setName("SomeClass");

		res.addMethod(createMethod("method1"));


		Method method2 = createMethod("method2");
		method2.setReturnType(new Type("ro.sft.pkg.SomeClasss<Y>"));
		method2.addParameter("param1", new Type("java.lang.String"));
		method2.addParameter("param2", new Type("ro.sft.pkg.Class<X>"));
		res.addMethod(method2);

		root.addChild(res);

		addStatements(method2);


		root.write();
		FileInputStream inStr = getGeneratedClass(projectRootFile);

		CompilationUnit cu = JavaParser.parse(inStr);

		assertClassStructureOk(cu);
		assertMethodsOk(cu);

	}


	private void addStatements(Method method2) throws GenerateException {
		Set<Statement> stmts = new HashSet<Statement>();
		CompoundStatement nullChk = new IfStatement("someVar == null");
		nullChk.addChildStmt(new Statement("return", ""));


		CompoundStatement initSet = new IfStatement("orders == null");
		String importStmt = "java.util.HashSet";
		initSet.addChildStmt(new Statement(" orders = new HashSet<Order>()", importStmt));


		Statement forExpr = new Statement("ord", "");
		ForStatement forStmt = new ForStatement(new Type("ro.sft.pkg.orders.Order"), "obj", forExpr);

		forStmt.addChildStmt(new Statement("obj.setSomeVar(this)", ""));
		forStmt.addChildStmt(new Statement("orders.add(obj)", ""));

		IfStatement nestedIf = new IfStatement("someVar != 0", 4);
		nestedIf.addChildStmt(new Statement("orders.remove(this)"));
		forStmt.addChildStmt(nestedIf);

		stmts.add(nullChk);
		stmts.add(initSet);
		stmts.add(forStmt);
		method2.getBody().add(nullChk);
		method2.getBody().add(initSet);
		method2.getBody().add(forStmt);
	}


	private FileInputStream getGeneratedClass(File projectRootFile) throws FileNotFoundException {
		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> arList = new ArrayList<File>(list);
		assertEquals(2, arList.size());
		FileInputStream inStr = new FileInputStream(arList.get(1));
		return inStr;
	}


	private void assertMethodsOk(CompilationUnit cu) {
		MethodDeclaration method1 = (MethodDeclaration) cu.getTypes().get(0).getMembers().get(0);
		assertEquals("method1", method1.getName());
		MethodDeclaration method2 = (MethodDeclaration) cu.getTypes().get(0).getMembers().get(1);
		assertEquals("method2", method2.getName());

		// check method 2 params 

		Parameter param1 = method2.getParameters().get(0);
		assertEquals("param1", param1.getId().toString());
		assertEquals("String", param1.getType().toString());
		Parameter param2 = method2.getParameters().get(1);
		assertEquals("param2", param2.getId().toString());
		assertEquals("Class<X>", param2.getType().toString());

		// check method 2 annotations 
		assertEquals("@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, "
				+ "prop = @SomeAnnotation(name = \"someAnnName\"))", method2.getAnnotations().get(0).toString());

		// check statments;

		assertEquals(3, method2.getBody().getStmts().size());

		japa.parser.ast.stmt.Statement stmt1 = method2.getBody().getStmts().get(0);
		IfStmt ifstmt = (IfStmt) stmt1;
		assertEquals("someVar == null", ifstmt.getCondition().toString());
		japa.parser.ast.stmt.Statement thenStmt = ifstmt.getThenStmt();
		assertEquals("{\n    return;\n}", thenStmt.toString());


		japa.parser.ast.stmt.Statement stmt2 = method2.getBody().getStmts().get(2);
		ForeachStmt forstmt = (ForeachStmt) stmt2;
		assertEquals("Order obj", forstmt.getVariable().toString());
		assertEquals("ord", forstmt.getIterable().toString());
		BlockStmt body = (BlockStmt) forstmt.getBody();
		assertEquals("{\n" + "    obj.setSomeVar(this);\n" + "    orders.add(obj);\n" + "    if (someVar != 0) {\n"
				+ "        orders.remove(this);\n    }\n}", body.toString());
	}


	private void assertClassStructureOk(CompilationUnit cu) {
		ImportDeclaration imprt0 = cu.getImports().get(0);
		assertEquals("java.util.HashSet", imprt0.getName().toString());
		ImportDeclaration imprt1 = cu.getImports().get(1);
		assertEquals("ro.sft.pkg.SomeClasss", imprt1.getName().toString());
		ImportDeclaration imprt2 = cu.getImports().get(2);
		assertEquals("ro.sft.pkg.Class", imprt2.getName().toString());
		//		ImportDeclaration imprt3 = cu.getImports().get(2);
		//		assertEquals("java.util.Set", imprt3.getName().toString());

		TypeDeclaration typeDecl = cu.getTypes().get(0);
		assertEquals("SomeClass", typeDecl.getName());
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
