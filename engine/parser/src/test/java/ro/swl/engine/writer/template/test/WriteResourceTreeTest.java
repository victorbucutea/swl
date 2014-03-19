package ro.swl.engine.writer.template.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.FileUtils.listFilesAndDirs;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.GenerateException;
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
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;


public class WriteResourceTreeTest extends GeneratorTest {


	private void generateEnhanceAndWrite(SWL swl) throws ParseException, GenerateException, WriteException {
		ctxt.setProperty(GlobalContext.PACKAGE, "ro.sft.somepackage");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-entity-and-static-res"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
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
		File generated = new File(generateDestDir, "module-entity-and-static-res");
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

		StringWriter writer = new StringWriter();
		generator.getProjectRoot().printTree(writer, 0);
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


		fold1.write();
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

		File generated = new File(generateDestDir, "module-entity-and-static-res");
		Collection<File> list = listFilesAndDirs(generated, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(list);
		System.out.println(files);

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
	public void servicesWrite() throws Exception {
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
										"			client    Client"+
										"		} " +
										"  }"+
						"}"));
		//@formatter:on
		generateEnhanceAndWrite(swl);

		assertCrudDaoInjected();

		assertNamedQueriesCreated();

	}


	private void assertCrudDaoInjected() throws Exception {
		File service = new File(generateDestDir,
				"module-entity-and-static-res/my-app/customer/ro/sft/somepackage/CustomerServiceBean.java");
		CompilationUnit cu = JavaParser.parse(service);

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


	private void assertNamedQueriesCreated() throws Exception {
		File client = new File(generateDestDir,
				"module-entity-and-static-res/my-app/customer/ro/sft/somepackage/model/Client.java");
		CompilationUnit cu = JavaParser.parse(client);

		TypeDeclaration cls = cu.getTypes().get(0);
		assertEquals(
				"[@Entity, @NamedQueries({ @NamedQuery(query = \"Select j from CV j where j.firstName = :firstName\", name = Client.WITH_FIRST_NAME), "
						+ "@NamedQuery(query = \"Select cert from Certification cert where cert.name like :name\","
						+ " name = Client.CERTIFICATIONS) })]", cls.getAnnotations().toString());

		FieldDeclaration static1 = (FieldDeclaration) cls.getMembers().get(0);
		int modifier1 = static1.getModifiers();
		assertEquals(0, modifier1 & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);
		assertEquals("WITH_FIRST_NAME = \"WithFirstName\"", static1.getVariables().get(0).toString());

		FieldDeclaration static2 = (FieldDeclaration) cls.getMembers().get(1);
		int modifier2 = static2.getModifiers();
		assertEquals(0, modifier2 & ModifierSet.FINAL & ModifierSet.STATIC & ModifierSet.PUBLIC);
		assertEquals("CERTIFICATIONS = \"Certifications\"", static2.getVariables().get(0).toString());

	}
}
