package ro.swl.engine.writer.template.test;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.*;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.java.model.*;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.parser.ASTProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;
import static org.junit.Assert.assertTrue;


public class WriteJavaResourceTests extends GeneratorTest {



	@Test
	public void fieldsTest() throws Exception {

		File projectRootFile = new File(generateDestDir, "fieldsTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot("fieldsTest", projectRootFile);
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

		res.addField(f);
		res.addField(f1);
		res.addField(f2);
		res.addField(f3);
		res.addField(f4);
		res.addField(f5);
		res.addField(f6);
		res.addField(f7);
		res.addField(f8);
		res.addStaticFinalProperty(f9);
		res.write();

		Collection<File> list = listFilesAndDirs(projectRootFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		assertEquals(2, list.size());

		File file = new File(projectRootFile, "SomeClass.java");

		assertImportsOk(file);
		assertFieldsOk(file);
		assertGetterAndSettersOk(file);
	}


	private void assertImportsOk(File file) throws ParseException, IOException {
		CompilationUnit cu = JavaParser.parse(file);

		assertEquals("import com.ibm.somepkg.Something;", cu.getImports().get(0).toString().trim());
		assertEquals("import ro.sft.pkg.Experience;", cu.getImports().get(1).toString().trim());
		assertEquals("import javax.persistence.Column;", cu.getImports().get(2).toString().trim());
		assertEquals("import org.codehaus.jackson.annotate.JsonIgnore;", cu.getImports().get(3).toString().trim());
		assertEquals("import javax.persistence.SomeAnnotation;", cu.getImports().get(4).toString().trim());
		assertEquals("import javax.persistence.CascadeType;", cu.getImports().get(5).toString().trim());
		assertEquals("import javax.persistence.OneToMany;", cu.getImports().get(6).toString().trim());
		assertEquals("import java.util.Set;", cu.getImports().get(7).toString().trim());
		assertEquals("import java.util.Date;", cu.getImports().get(8).toString().trim());
		assertEquals("import ro.sft.otherpkg.Customer;", cu.getImports().get(9).toString().trim());

	}


	private void assertGetterAndSettersOk(File javaFile) throws ParseException, IOException {
		CompilationUnit cu = JavaParser.parse(javaFile);

		TypeDeclaration cls = cu.getTypes().get(0);
		MethodDeclaration getter1 = (MethodDeclaration) cls.getMembers().get(10);
		assertEquals("getProp1", getter1.getName());
		assertEquals("Customer", getter1.getType().toString());

		MethodDeclaration setter1 = (MethodDeclaration) cls.getMembers().get(11);
		assertEquals("setProp1", setter1.getName());
		assertEquals("Customer", setter1.getParameters().get(0).getType().toString());


		MethodDeclaration getter25 = (MethodDeclaration) cls.getMembers().get(26);
		assertEquals("getProp9", getter25.getName());
		assertEquals("Set<Something>", getter25.getType().toString());

		MethodDeclaration setter25 = (MethodDeclaration) cls.getMembers().get(27);
		assertEquals("setProp9", setter25.getName());
		assertEquals("Set<Something>", setter25.getParameters().get(0).getType().toString());

	}


	private void assertFieldsOk(File javaFile) throws ParseException, IOException {
		CompilationUnit cu = JavaParser.parse(javaFile);

		TypeDeclaration cls = cu.getTypes().get(0);
		FieldDeclaration field1 = (FieldDeclaration) cls.getMembers().get(0);
		assertEquals("PROP_10", field1.getVariables().get(0).getId().toString());
		int modifiers = field1.getModifiers();
		assertEquals(0, modifiers & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);


		FieldDeclaration prop1 = (FieldDeclaration) cls.getMembers().get(1);
		assertEquals("prop1", prop1.getVariables().get(0).toString());
		assertEquals("Customer", prop1.getType().toString());
		assertEquals("@Column", prop1.getAnnotations().get(0).toString());

		FieldDeclaration prop2 = (FieldDeclaration) cls.getMembers().get(2);
		assertEquals("prop2", prop2.getVariables().get(0).toString());
		assertEquals("Customer", prop2.getType().toString());

		FieldDeclaration prop3 = (FieldDeclaration) cls.getMembers().get(3);
		assertEquals("prop3", prop3.getVariables().get(0).toString());
		assertEquals("Experience", prop3.getType().toString());

		FieldDeclaration prop4 = (FieldDeclaration) cls.getMembers().get(4);
		assertEquals("prop4", prop4.getVariables().get(0).toString());
		assertEquals("int", prop4.getType().toString());

		FieldDeclaration prop5 = (FieldDeclaration) cls.getMembers().get(5);
		assertEquals("prop5", prop5.getVariables().get(0).toString());
		assertEquals("Integer", prop5.getType().toString());

		FieldDeclaration prop6 = (FieldDeclaration) cls.getMembers().get(6);
		assertEquals("prop6", prop6.getVariables().get(0).toString());
		assertEquals("Date", prop6.getType().toString());

		FieldDeclaration prop7 = (FieldDeclaration) cls.getMembers().get(7);
		assertEquals("prop7", prop7.getVariables().get(0).toString());
		assertEquals("byte[]", prop7.getType().toString());

		FieldDeclaration prop8 = (FieldDeclaration) cls.getMembers().get(8);
		assertEquals("prop8", prop8.getVariables().get(0).toString());
		assertEquals("Long", prop8.getType().toString());

		FieldDeclaration prop9 = (FieldDeclaration) cls.getMembers().get(9);
		assertEquals("prop9", prop9.getVariables().get(0).toString());
		assertEquals("Set<Something>", prop9.getType().toString());
	}


	@Test
	public void methodsTest() throws Exception {
		File projectRootFile = new File(generateDestDir, "methodsTest");
		projectRootFile.mkdir();
		ProjectRoot root = new ProjectRoot("methodsTest", projectRootFile);
		JavaResource<Field> res = new JavaResource<Field>(root, "SomeClass", "ro.sft.somepkg");
		res.setName("SomeClass");

		res.addMethod(createMethod("method1"));


		Method method2 = createMethod("method2");
		method2.setReturnType(new Type("ro.sft.pkg.SomeClasss<Y>"));
		method2.addParameter("param1", new Type("java.lang.String"));
		method2.addParameter("param2", new Type("ro.sft.pkg.Class<X>"));
        Method.Parameter param = new Method.Parameter("param3",new Type("java.util.Set<SomeClass>"));
        Annotation ann  = new Annotation("javax.ws.rs.PathParam");
        param.addAnnotation(ann);
        method2.addParameter(param);
		res.addMethod(method2);


		root.addChild(res);
		addStatements(method2);


		res.write();
		FileInputStream inStr = getGeneratedClass(projectRootFile);

		CompilationUnit cu = JavaParser.parse(inStr);

		assertClassStructureOk(cu);
		assertMethodsOk(cu);

	}


	private void addStatements(Method method2) throws CreateException {
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

        Parameter param3 = method2.getParameters().get(2);
        assertEquals("param3", param3.getId().toString());
        assertEquals("@PathParam", param3.getAnnotations().get(0).toString());
        assertEquals("Set<SomeClass>", param3.getType().toString());

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
		List<ImportDeclaration> imports = cu.getImports();
		List<String> expectedImports = asList("java.util.HashSet", "ro.sft.pkg.SomeClasss",
				"javax.persistence.SomeAnnotation", "javax.persistence.CascadeType", "javax.persistence.OneToMany",
				"ro.sft.pkg.Class", "java.util.Set", "javax.ws.rs.PathParam");
		for (ImportDeclaration imprt : imports) {
			assertTrue(expectedImports.contains(imprt.getName().toString()));
		}

		TypeDeclaration typeDecl = cu.getTypes().get(0);
		assertEquals("SomeClass", typeDecl.getName());
	}



	private Method createMethod(String string) throws CreateException {
		Method m = new Method(string);

		m.addAnnotation(createAnnotation());
		return m;
	}


	private Annotation createAnnotation() throws CreateException {
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
