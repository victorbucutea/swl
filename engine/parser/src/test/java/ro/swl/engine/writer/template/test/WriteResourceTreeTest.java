package ro.swl.engine.writer.template.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.ReturnStmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.GlobalContext;
import ro.swl.engine.generator.InternalEnhancers;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.PackageResource;
import ro.swl.engine.generator.javaee.enhancer.EJBTechnology;
import ro.swl.engine.generator.javaee.enhancer.JPATechnology;
import ro.swl.engine.generator.javaee.enhancer.JaxRSTechnology;
import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.FolderResource;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;


public class WriteResourceTreeTest extends GeneratorTest {


	private void generateEnhanceAndWrite(SWL swl) throws ParseException, CreateException, WriteException {
		ctxt.setProperty(GlobalContext.PACKAGE, "ro.sft.somepackage");
		skeleton.setSkeletonName("module-entity-and-static-res");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		generator.write(appModel);
	}


	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> list = new ArrayList<Technology>();

		list.add(new InternalEnhancers());
		list.add(new JPATechnology());
		list.add(new JaxRSTechnology());
		list.add(new EJBTechnology());


		return list;
	}


	@Test
	public void noEntityNoService() throws Exception {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'moduleTest' \n\t\n" +
						" module cv {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {}" +
						"}" +
						"module customer {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {}"+
						"}"));
		//@formatter:on
		generateEnhanceAndWrite(swl);
		File generated = new File(generateDestDir, "test-gen");
		Collection<File> list = listFilesAndDirs(generated, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(list);
		assertEquals("test-gen", files.get(0).getName());
		assertEquals("my-app", files.get(1).getName());
		assertEquals(new File(generated, "my-app"), files.get(1).getAbsoluteFile());
		assertEquals("customer", files.get(2).getName());
		assertEquals("ro", files.get(3).getName());
		assertEquals("sft", files.get(4).getName());
		assertEquals("somepackage", files.get(5).getName());
		assertEquals("img.jpg", files.get(6).getName());
		assertEquals(347796, files.get(6).length());
		assertEquals("model", files.get(7).getName());
		assertEquals("somefile.txt", files.get(8).getName());
		assertEquals("somefile.xml", files.get(9).getName());

		assertEquals("cv", files.get(10).getName());
		assertEquals("ro", files.get(11).getName());
		assertEquals("sft", files.get(12).getName());
		assertEquals("somepackage", files.get(13).getName());
		assertEquals("img.jpg", files.get(14).getName());
		assertEquals(347796, files.get(14).length());
		assertEquals("model", files.get(15).getName());
		assertEquals("somefile.txt", files.get(16).getName());
		assertEquals("somefile.xml", files.get(17).getName());
		assertEquals(18, files.size());

	}


	@Test
	public void projectRootReplacedWithPrjName() throws Exception {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'moduleTest' \n\t\n" +
								"module cv {" +
											"  ui     {} " +
											"  logic  {}" +
											"  domain {}" +
								"}" +
								"module customer {" +
											"  ui     {} " +
											"  logic  {}" +
											"  domain {}"+
								"}"));
		//@formatter:on
		ctxt.setProjectName("test-project-name");
		ctxt.setDefaultPackage("ro.sft.somepackage");
		skeleton.setSkeletonName("simple-template");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		generator.write(appModel);

		assertEquals("test-project-name", generator.getProjectRoot().getOutputFileName());

		File generated = new File(generateDestDir, "test-project-name");
		List<File> list = new ArrayList<File>(listFilesAndDirs(generated, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE));
		assertEquals("test-project-name", list.get(0).getName());
		assertEquals("customer-base", list.get(1).getName());

	}


	@SuppressWarnings("unchecked")
	@Test
	public void programmaticalyCreatedResourcesWrite() throws Exception {


		FolderResource fold1 = new FolderResource(null, "folder");
		FileResource f1 = new FileResource(fold1, "somefile", true);
		PackageResource pkg = new PackageResource(fold1, "ro.sft.pkg");
		FolderResource subPkg = new FolderResource(pkg, "model");
		pkg.addChild(subPkg);

		JavaResource<Field> j1 = new JavaResource<Field>(subPkg, "SomeClass", "ro.sft.pkg");
		JavaResource<Field> j2 = new JavaResource<Field>(subPkg, "SomeClass2", "ro.sft.pkg");
		JavaResource<Field> j3 = new JavaResource<Field>(subPkg, "SomeClass3", "ro.sft.pkg");
		subPkg.addChildren(asList(j1, j2, j3));

		fold1.addChildren(asList(f1, pkg));


		writeResourceTree(null, fold1);

		File generated = new File(generateDestDir, "folder");
		Collection<File> list = listFilesAndDirs(generated, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(list);


		assertEquals("folder", files.get(0).getName());
		assertEquals(generated.getAbsoluteFile(), files.get(0).getAbsoluteFile());
		assertEquals("ro", files.get(1).getName());
		assertEquals("sft", files.get(2).getName());
		assertEquals("pkg", files.get(3).getName());
		assertEquals("model", files.get(4).getName());
		assertEquals("SomeClass.java", files.get(5).getName());
		assertEquals("SomeClass2.java", files.get(6).getName());
		assertEquals("SomeClass3.java", files.get(7).getName());
		assertEquals("somefile", files.get(8).getName());
	}


	private void writeResourceTree(ASTSwdlApp appModel, Resource res) throws WriteException {
		res.write();
		for (Resource child : res.getChildren()) {
			writeResourceTree(appModel, child);
		}

	}


	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void entityWrite() throws Exception {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'moduleTest' \n\t\n" +
						" module cv {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		Customer {"+
									"			startDate Date,"+
									"			endDate   Date," +
									"			exp		  Experience -> *"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			field 	  Blob," +
									"			someProp  String," +
									"			someProp2 double"+
									"		} " +
									"  }" +
									"}" +
						"module customer {" +
										"  ui     {} " +
										"  logic  {}" +
										"  domain {" +
										"		Client {"+
										"			startDate Date,"+
										"			endDate   Date,"+
										"			orders Set<Order> -> client"+		
										"		}"+
										""+	
										"	    Order {"+
										"			startDate Date,"+
										"			endDate   Date,"+
										"			field 	  Blob," +
										"			client    Client"+
										"		} " +
										"  }"+
						"}"));
		//@formatter:on
		generateEnhanceAndWrite(swl);

		File generated = new File(generateDestDir, "test-gen");
		Collection<File> list = listFilesAndDirs(generated, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(list);

		assertEquals("my-app", files.get(1).getName());
		assertEquals(new File(generated, "my-app"), files.get(1).getAbsoluteFile());
		assertEquals("customer", files.get(2).getName());
		assertEquals("ro", files.get(3).getName());
		assertEquals("sft", files.get(4).getName());
		assertEquals("somepackage", files.get(5).getName());
		assertEquals("img.jpg", files.get(6).getName());
		assertEquals(347796, files.get(6).length());
		assertEquals("model", files.get(7).getName());
		assertEquals("Client.java", files.get(8).getName());
		assertEquals("Order.java", files.get(9).getName());
		assertEquals("somefile.txt", files.get(10).getName());

		assertEquals("somefile.xml", files.get(11).getName());
		assertEquals("cv", files.get(12).getName());
		assertEquals("ro", files.get(13).getName());
		assertEquals("sft", files.get(14).getName());
		assertEquals("somepackage", files.get(15).getName());
		assertEquals("img.jpg", files.get(16).getName());
		assertEquals(347796, files.get(6).length());
		assertEquals("model", files.get(7).getName());
		assertEquals("Client.java", files.get(8).getName());
		assertEquals("Order.java", files.get(9).getName());
		assertEquals("somefile.txt", files.get(10).getName());
		assertEquals(22, files.size());
	}


	@Test
	public void velocityPlainFileWrite() throws Exception {
		SWL swl = complexSwl();

		ctxt.setProjectName("test-project-name");
		ctxt.setDefaultPackage("ro.sft.somepackage");
		ctxt.setProperty("customProperty", "customPropertyValue");
		ctxt.setInitialVersion("1.0");
		skeleton.setSkeletonName("simple-velocity-template");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		generator.write(appModel);

		File generated = new File(generateDestDir, "test-project-name");
		List<File> list = new ArrayList<File>(listFilesAndDirs(generated, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE));
		assertEquals("customer", list.get(1).getName());
		assertPomXmlVelocityTemplateFilled(list);

		assertEquals("somepackage", list.get(5).getName());

		assertPlainFileVelocityTemplateFilledIn(list.get(9), "customer");
		assertPlainFileVelocityTemplateFilledIn(list.get(17), "cv");

	}



	@Test
	public void servicesAndEntitiesWrite() throws Exception {
		SWL swl = complexSwl();
		generateEnhanceAndWrite(swl);

		assertCrudMethodsInService();

		assertImportsInEntity();

		assertNamedQueriesCreated();

		assertEntityHelperMethodsCreated();

	}


	private SWL complexSwl() {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'moduleTest' \n\t\n" +
						" module cv {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		Customer {"+
									"			startDate Date,"+
									"			endDate   Date," +
									"			exp		  Experience -> *"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			field 	  Blob," +
									"			someProp  String," +
									"			someProp2 double"+
									"		} " +
									"  }" +
									"}" +
						"module customer {" +
										"  ui     {} " +
										"  logic  {" +
										"		service CustomerService { " +
										"			crud Client { " +
										"				searcher WithFirstName {" +
										"					\"Select j from CV j where j.firstName = :firstName\"" +
										"				}" +
										"				searcher Certifications {"+ 
										"					\"Select cert from Certification cert where cert.name like :name\""+
										"				}" +
										"			}" +
										"			crud Order { " +
										"					searcher WithOrderId {" +
										"						\"Select j from CV j where j.firstName = :firstName\"" +
										"					}"+
										"			}"+
										"			void someAction() {"+
										"			}" +
										"		}	" +
										"}" +
										"  domain {" +
										"		Client {"+
										"			startDate Date,"+
										"			endDate   Date,"+
										"			orders Set<Order> -> client"+		
										"		}"+
										""+	
										"	    Order {"+
										"			startDate Date,"+
										"			endDate   Date,"+
										"			field 	  Blob," +
										"			client    Client," +
										"			someStr	  String"+
										"		} " +
										"  }"+
						"}"));
		//@formatter:on
		return swl;
	}



	private void assertEntityHelperMethodsCreated() throws Exception {
		File client = new File(generateDestDir, "test-gen/my-app/customer/ro/sft/somepackage/model/Client.java");
		CompilationUnit cu = JavaParser.parse(client);

		TypeDeclaration cls = cu.getTypes().get(0);

		assertAdderMethodPresent(cls);
		assertClientSerializerMethodPresent(cls);


		client = new File(generateDestDir, "test-gen/my-app/cv/ro/sft/somepackage/model/Customer.java");
		cu = JavaParser.parse(client);
		cls = cu.getTypes().get(0);

		assertCustomerSerializerMethodPresent(cls);
	}


	private void assertClientSerializerMethodPresent(TypeDeclaration cls) {
		MethodDeclaration srlzer = (MethodDeclaration) cls.getMembers().get(7);
		assertOrdersPropSerializerMethodPresent(srlzer);
	}


	private void assertCustomerSerializerMethodPresent(TypeDeclaration cls) {
		MethodDeclaration srlzer = (MethodDeclaration) cls.getMembers().get(3);

		assertEquals("serializeExp", srlzer.getName());
		assertNull(srlzer.getParameters());

		AnnotationExpr jsonProp = srlzer.getAnnotations().get(0);
		assertEquals("@JsonProperty(\"exp\")", jsonProp.toString());

		AnnotationExpr jsonManagedRef = srlzer.getAnnotations().get(1);
		assertEquals("@JsonManagedReference", jsonManagedRef.toString());



		BlockStmt body = srlzer.getBody();
		IfStmt ifstmt = (IfStmt) body.getStmts().get(0);
		assertEquals("exp == null", ifstmt.getCondition().toString());
		assertEquals("{returnnull;}", deleteWhitespace(ifstmt.getThenStmt().toString()));


		IfStmt ifstmt2 = (IfStmt) body.getStmts().get(1);
		assertEquals("PersistenceUtil.isinitialized(exp)", ifstmt2.getCondition().toString());
		assertEquals("{returnexp;}", deleteWhitespace(ifstmt2.getThenStmt().toString()));


		ReturnStmt ret = (ReturnStmt) body.getStmts().get(2);
		assertEquals("returnnull;", deleteWhitespace(ret.toString()));


		MethodDeclaration desrlzer = (MethodDeclaration) cls.getMembers().get(9);

		assertEquals("@JsonProperty(\"exp\")", desrlzer.getAnnotations().get(0).toString());
	}


	private void assertOrdersPropSerializerMethodPresent(MethodDeclaration srlzer) {
		assertEquals("serializeOrders", srlzer.getName());
		assertNull(srlzer.getParameters());

		AnnotationExpr jsonProp = srlzer.getAnnotations().get(0);
		assertEquals("@JsonProperty(\"orders\")", jsonProp.toString());

		AnnotationExpr jsonManagedRef = srlzer.getAnnotations().get(1);
		assertEquals("@JsonManagedReference", jsonManagedRef.toString());



		BlockStmt body = srlzer.getBody();
		IfStmt ifstmt = (IfStmt) body.getStmts().get(0);
		assertEquals("orders == null", ifstmt.getCondition().toString());
		assertEquals("{returnnewHashSet<Order>();}", deleteWhitespace(ifstmt.getThenStmt().toString()));


		IfStmt ifstmt2 = (IfStmt) body.getStmts().get(1);
		assertEquals("PersistenceUtil.isinitialized(orders)", ifstmt2.getCondition().toString());
		assertEquals("{returnorders;}", deleteWhitespace(ifstmt2.getThenStmt().toString()));


		ReturnStmt ret = (ReturnStmt) body.getStmts().get(2);
		assertEquals("returnnewHashSet<Order>();", deleteWhitespace(ret.toString()));
	}


	private void assertAdderMethodPresent(TypeDeclaration cls) {
		MethodDeclaration adder = (MethodDeclaration) cls.getMembers().get(6);

		assertEquals("addOrders", adder.getName());

		assertEquals("Order", adder.getParameters().get(0).getType().toString());
		assertTrue(adder.getParameters().get(0).isVarArgs());
		assertEquals("ordersToAdd", adder.getParameters().get(0).getId().toString());


		BlockStmt body = adder.getBody();
		IfStmt ifstmt = (IfStmt) body.getStmts().get(0);
		assertEquals("ordersToAdd == null", ifstmt.getCondition().toString());
		assertEquals("{return;}", deleteWhitespace(ifstmt.getThenStmt().toString()));

		IfStmt ifstmt2 = (IfStmt) body.getStmts().get(1);
		assertEquals("orders == null", ifstmt2.getCondition().toString());
		assertEquals("{orders=newHashSet<Order>();}", deleteWhitespace(ifstmt2.getThenStmt().toString()));


		ForeachStmt forstmt = (ForeachStmt) body.getStmts().get(2);

		assertEquals("ordersToAdd", forstmt.getIterable().toString());
		assertEquals("Order obj", forstmt.getVariable().toString());

		BlockStmt addRelation = (BlockStmt) forstmt.getBody();

		assertEquals("obj.setClient(this);", addRelation.getStmts().get(0).toString());
		assertEquals("orders.add(obj);", addRelation.getStmts().get(1).toString());
	}


	private void assertCrudMethodsInService() throws Exception {
		File service = new File(generateDestDir, "test-gen/my-app/customer/ro/sft/somepackage/CustomerService.java");
		CompilationUnit cu = JavaParser.parse(service);

		List<ImportDeclaration> imports = cu.getImports();
		List<String> expected = asList("ro.sft.somepackage.base.dao.CrudDao", "javax.ejb.Stateless", "javax.ejb.EJB",
				"ro.sft.somepackage.base.dao.SearchInfo", "java.util.List", "ro.sft.somepackage.model.Order",
				"ro.sft.somepackage.model.Client");

		assertTrue(expected.contains(imports.get(0).getName().toString()));
		assertTrue(expected.contains(imports.get(1).getName().toString()));
		assertTrue(expected.contains(imports.get(2).getName().toString()));
		assertTrue(expected.contains(imports.get(3).getName().toString()));
		assertTrue(expected.contains(imports.get(4).getName().toString()));
		assertTrue(expected.contains(imports.get(5).getName().toString()));
		assertTrue(expected.contains(imports.get(6).getName().toString()));


		TypeDeclaration cls = cu.getTypes().get(0);
		FieldDeclaration dao1 = (FieldDeclaration) cls.getMembers().get(0);
		assertEquals("clientDao", dao1.getVariables().get(0).toString());
		FieldDeclaration dao2 = (FieldDeclaration) cls.getMembers().get(1);
		assertEquals("orderDao", dao2.getVariables().get(0).toString());

		MethodDeclaration someAction = (MethodDeclaration) cls.getMembers().get(2);
		assertEquals("someAction", someAction.getName());

		MethodDeclaration saveClient = (MethodDeclaration) cls.getMembers().get(3);
		assertEquals("saveClient", saveClient.getName());

		MethodDeclaration getClient = (MethodDeclaration) cls.getMembers().get(4);
		assertEquals("getAllClients", getClient.getName());

		MethodDeclaration findClient = (MethodDeclaration) cls.getMembers().get(5);
		assertEquals("findClient", findClient.getName());

		MethodDeclaration searchClient = (MethodDeclaration) cls.getMembers().get(6);
		assertEquals("searchClient", searchClient.getName());

		MethodDeclaration saveOrder = (MethodDeclaration) cls.getMembers().get(7);
		assertEquals("saveOrder", saveOrder.getName());

		MethodDeclaration getOrder = (MethodDeclaration) cls.getMembers().get(8);
		assertEquals("getAllOrders", getOrder.getName());

		MethodDeclaration findOrder = (MethodDeclaration) cls.getMembers().get(9);
		assertEquals("findOrder", findOrder.getName());

		MethodDeclaration searchOrder = (MethodDeclaration) cls.getMembers().get(10);
		assertEquals("searchOrder", searchOrder.getName());
	}


	private void assertImportsInEntity() throws Exception {
		List<String> expected = asList("javax.persistence.Column", "javax.persistence.TemporalType",
				"org.codehaus.jackson.annotate.JsonIgnore", "javax.persistence.CascadeType",
				"ro.sft.somepackage.base.util.PersistenceUtil", "java.util.Date",
				"org.codehaus.jackson.annotate.JsonProperty", "org.codehaus.jackson.annotate.JsonManagedReference",
				"java.util.HashSet", "javax.persistence.NamedQueries", "javax.persistence.Temporal",
				"javax.persistence.OneToMany", "javax.persistence.Entity", "javax.persistence.NamedQuery",
				"java.util.Set", "ro.sft.somepackage.base.model.EntityBase");
		File client = new File(generateDestDir, "test-gen/my-app/customer/ro/sft/somepackage/model/Client.java");
		CompilationUnit cu = JavaParser.parse(client);
		List<ImportDeclaration> imports = cu.getImports();

		assertEquals(expected.size(), imports.size());

		for (ImportDeclaration imprt : imports) {
			assertTrue(expected.contains(imprt.getName().toString()));
		}


		File order = new File(generateDestDir, "test-gen/my-app/customer/ro/sft/somepackage/model/Order.java");
		CompilationUnit cu2 = JavaParser.parse(order);
		List<ImportDeclaration> imports2 = cu2.getImports();
		List<String> expected2 = asList("javax.persistence.ManyToOne",
				"org.codehaus.jackson.annotate.JsonBackReference", "javax.persistence.Column",
				"javax.persistence.TemporalType", "javax.persistence.NamedQueries", "javax.persistence.Temporal",
				"javax.persistence.NamedQuery", "javax.persistence.Entity", "javax.persistence.JoinColumn",
				"java.util.Date", "ro.sft.somepackage.model.Client", "ro.sft.somepackage.base.model.EntityBase");

		assertEquals(expected2.size(), imports2.size());

		for (ImportDeclaration imprt : imports2) {
			assertTrue(expected2.contains(imprt.getName().toString()));
		}
	}


	private void assertNamedQueriesCreated() throws Exception {
		File client = new File(generateDestDir, "test-gen/my-app/customer/ro/sft/somepackage/model/Client.java");
		CompilationUnit cu = JavaParser.parse(client);

		TypeDeclaration cls = cu.getTypes().get(0);
		assertEquals(//@formatter:off
				"[@Entity, @NamedQueries({ " +
						"@NamedQuery(query = \"Select j from Client\", name = Client.ALL), " +
						"@NamedQuery(query = \"Select j from CV j where j.firstName = :firstName\", name = Client.WITH_FIRST_NAME), " +
						"@NamedQuery(query = \"Select cert from Certification cert where cert.name like :name\", name = Client.CERTIFICATIONS) })]",
						cls.getAnnotations().toString());//@formatter:on

		FieldDeclaration static1 = (FieldDeclaration) cls.getMembers().get(0);
		int modifier1 = static1.getModifiers();
		assertEquals(0, modifier1 & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);
		assertEquals("ALL = \"all\"", static1.getVariables().get(0).toString());



		FieldDeclaration static2 = (FieldDeclaration) cls.getMembers().get(1);
		int modifier2 = static2.getModifiers();
		assertEquals(0, modifier2 & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);
		assertEquals("WITH_FIRST_NAME = \"WithFirstName\"", static2.getVariables().get(0).toString());

		FieldDeclaration static3 = (FieldDeclaration) cls.getMembers().get(2);
		int modifier3 = static3.getModifiers();
		assertEquals(0, modifier3 & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);
		assertEquals("CERTIFICATIONS = \"Certifications\"", static3.getVariables().get(0).toString());

	}


	private void assertPlainFileVelocityTemplateFilledIn(File file, String module) throws IOException {
		assertEquals("plain-file.txt", file.getName());
		Properties props = new Properties();
		FileInputStream fileInputStream = new FileInputStream(file);
		props.load(fileInputStream);
		fileInputStream.close();

		// assert properties from global context filled in
		assertEquals("ro.sft.somepackage", props.getProperty("basePackage"));
		assertEquals(module, props.getProperty("module"));
		assertEquals("customPropertyValue", props.getProperty("customProperty"));
		assertEquals("1.0", props.getProperty("version"));
	}


	private void assertPomXmlVelocityTemplateFilled(List<File> list) throws Exception {
		assertEquals("pom.xml", list.get(2).getName());
		PomXmlHandler handler = new PomXmlHandler("customer");
		parseXmlWithHandler(list, handler);
		assertTrue(handler.getModulesInPom().containsAll(
				asList("cv-ear", "cv-base", "cv-ui", "cv-ejb", "customer-ear", "customer-ui", "customer-ejb",
						"customer-base")));

	}


	public static class PomXmlHandler extends DefaultHandler {

		private StringBuffer buffer;

		private boolean isInParentTag;

		private boolean isInDepTag;

		private String module;

		private boolean isInPlugin;

		private List<String> modulesInPom = new ArrayList<String>();

		private boolean isInModule;


		public PomXmlHandler(String module) {
			this.module = module;
		}


		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			buffer = new StringBuffer();

			if ("dependency".equals(qName)) {
				isInDepTag = true;
			}

			if ("parent".equals(qName)) {
				isInParentTag = true;
			}

			if ("plugin".equals(qName)) {
				isInPlugin = true;
			}

			if ("module".equals(qName)) {
				isInModule = true;
			}
		}


		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String content = new String(ch, start, length);
			if (isInModule) {
				modulesInPom.add(content);
			}
			buffer.append(content);
		}


		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {

			if ("dependency".equals(qName)) {
				isInDepTag = false;
			}

			if ("parent".equals(qName)) {
				isInParentTag = false;
			}

			if ("plugin".equals(qName)) {
				isInPlugin = true;
			}


			if ("groupId".equals(qName)) {
				assertEquals("ro.sft.somepackage", buffer.toString());
			} else if ("artifactId".equals(qName) && !isInParentTag && !isInDepTag && !isInPlugin) {
				assertEquals(module + "-ejb", buffer.toString());
			} else if ("artifactId".equals(qName) && isInParentTag && !isInDepTag && !isInPlugin) {
				assertEquals("test-project-name", buffer.toString());
			} else if ("artifactId".equals(qName) && !isInParentTag && isInDepTag && !isInPlugin) {
				assertEquals(module + "-base", buffer.toString());
			} else if ("name".equals(qName)) {
				assertEquals(module + "-ejb", buffer.toString());
			} else if ("version".equals(qName)) {
				assertEquals("1.0", buffer.toString());
			}
		}



		public List<String> getModulesInPom() {
			return modulesInPom;
		}
	}

}
