package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ro.swl.engine.generator.GlobalContext.AUTO_DETECT_PACKAGE;
import static ro.swl.engine.generator.GlobalContext.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.PackageResource;
import ro.swl.engine.generator.javaee.exception.DuplicateEntityException;
import ro.swl.engine.generator.javaee.exception.DuplicateFieldNameException;
import ro.swl.engine.generator.javaee.exception.InvalidPackageException;
import ro.swl.engine.generator.javaee.exception.NoModuleException;
import ro.swl.engine.generator.javaee.model.ModuleResource;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.generator.javaee.model.ServiceResource;
import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.FolderResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class GenerateResourceTreeTests extends GeneratorTest {

	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> techs = new ArrayList<Technology>();
		return techs;
	}


	@Test
	public void simpleResourceTreeGeneration() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();
		ctxt.setTemplateRootDir(new File(testTemplateDir, "simple-template"));
		generator.generate(appModel);

		ProjectRoot root = generator.getProjectRoot();

		List<Resource> projects = root.getChildren();

		assertEquals(5, projects.size());
		assertTrue(projects.get(0) instanceof FolderResource);
		assertTrue(projects.get(1) instanceof FolderResource);
		assertTrue(projects.get(2) instanceof FolderResource);
		assertTrue(projects.get(3) instanceof FolderResource);
		assertTrue(projects.get(4) instanceof FileResource);

		Resource ejbSource = projects.get(2).getChildren().get(0);
		assertTrue(ejbSource instanceof FolderResource);
		assertEquals("src", ejbSource.getOutputFileName());
		assertEquals("test", projects.get(2).getChildren().get(1).getOutputFileName());
		assertEquals("pom.xml", projects.get(2).getChildren().get(2).getOutputFileName());
		assertEquals("test.xml", projects.get(2).getChildren().get(3).getOutputFileName());

		Resource rootArtifactId = ejbSource.getChildren().get(0).getChildren().get(0).getChildren().get(0);

		Resource moduleTemplate = rootArtifactId.getChildren().get(0);
		assertTrue(moduleTemplate instanceof ModuleResource);

		Resource moduleTemplate2 = rootArtifactId.getChildren().get(1);
		assertTrue(moduleTemplate2 instanceof ModuleResource);


		Resource resources = ejbSource.getChildren().get(0).getChildren().get(1);
		Resource metaInf = resources.getChildren().get(0);
		assertEquals("META-INF", metaInf.getOutputFileName());
		assertEquals(2, metaInf.getChildren().size());

		assertEquals("beans.xml", metaInf.getChildren().get(0).getOutputFileName());

		Resource persistencexml = metaInf.getChildren().get(1);
		assertTrue(persistencexml instanceof PersistenceXml);
		assertEquals("persistence.xml", persistencexml.getOutputFileName());
	}


	@Test
	public void moduleWithNoEntitiesInModel() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();

		generator.generate(appModel);

		ProjectRoot projectRoot = generator.getProjectRoot();


		List<Resource> modules = projectRoot.getChildren();

		assertEquals(2, modules.size());

		Resource module1 = modules.get(0);
		Resource module2 = modules.get(1);

		assertTrue(module1 instanceof ModuleResource);
		assertTrue(module2 instanceof ModuleResource);

		assertEquals(1, module1.getChildren().size());
		assertEquals(1, module2.getChildren().size());

		Resource pkg1 = module1.getChild(0);
		assertTrue(pkg1 instanceof PackageResource);
		Resource pkg2 = module2.getChild(0);
		assertTrue(pkg2 instanceof PackageResource);

		Resource modelFolder = pkg1.getChild(0);
		assertTrue(modelFolder.getChildren().isEmpty());

		Resource modelFolder2 = pkg2.getChild(1);
		assertTrue(modelFolder2.getChildren().isEmpty());

	}


	@Test(expected = NoModuleException.class)
	public void entityInModelWithNoModuleInTemplate() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
				" module CV {" +
							"  ui     {} " +
							"  logic  {}" +
							"  domain {"+
							"	    Experience {"+
							"			startDate Date,"+
							"			endDate   Date,"+
							"			companyType CompanyType,"+
							"			responsabilities String,"+
							"			projects Set<Project>,"+
							" 			otherProjects List<Project>"+
							"		} " +
							"  }" +
				"}"));
		//@formatter:on
		ctxt.setTemplateRootDir(new File(testTemplateDir, "entity-no-module"));
		generator.generate(swl.SwdlApp());
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0);
		assertTrue(modelFolder instanceof FolderResource);

		assertTrue(modelFolder.getChild(0) instanceof FileResource);
	}


	@Test
	public void entityInModelButNotInTemplate() throws GenerateException, ParseException {
		//@formatter:off
				SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
						" module CV {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {"+
									"	    Experience {"+
									"			startDate Date"+
									"		} " +
									"  }" +
						"}"));
		//@formatter:on
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-no-entity"));
		generator.generate(swl.SwdlApp());

		ProjectRoot root = generator.getProjectRoot();

		ModuleResource module = (ModuleResource) root.getChild(0).getChild(0);
		assertEquals("CV", module.getModuleName());

		Resource file = module.getChild(0).getChild(0);

		assertFalse(file instanceof JavaResource);
	}


	@Test
	public void entityInTemplateButNotInModel() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n module SS {} ";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		generator.generate(appModel);

		ProjectRoot root = generator.getProjectRoot();
		assertTrue(root.getChild(0) instanceof ModuleResource);

	}


	@Test(expected = NoModuleException.class)
	public void moduleInModelButNotInTemplate() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();
		ctxt.setTemplateRootDir(new File(testTemplateDir, "entity-no-module"));
		generator.generate(appModel);

		ProjectRoot root = generator.getProjectRoot();

		Resource projectEjb = root.getChild(0);
		assertFalse(projectEjb instanceof ModuleResource);
		assertTrue(projectEjb instanceof FolderResource);

		Resource modelFolder = projectEjb.getChild(0);
		assertFalse(modelFolder instanceof ModuleResource);
		assertTrue(modelFolder instanceof FolderResource);

		assertTrue(modelFolder.getChild(0) instanceof PackageResource);
		assertTrue(modelFolder.getChild(0).getChild(0) instanceof FileResource);
	}


	@Test(expected = GenerateException.class)
	public void templateRootNotFound() throws Exception {
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module"));
		generator.generate(new ASTSwdlApp(0));
	}


	@Test(expected = DuplicateEntityException.class)
	public void entityDuplicate() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
						" module CV {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		SomeEntity {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			companyType CompanyType,"+
									"			responsabilities String,"+
									"			projects Set<Project>,"+
									" 			otherProjects List<Project>"+
									"		} " +
									"  }" +
						"}"+
						" module CV2 {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		SomeEntity {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Certification {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			companyType CompanyType,"+
									"			responsabilities String,"+
									"			projects Set<Project>,"+
									" 			otherProjects List<Project>"+
									"		}" +
									" }" +
						"}"));
				//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.recruiter");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
	}


	@Test(expected = DuplicateFieldNameException.class)
	public void entityDuplicateFieldName() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
						" module CV {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		SomeEntity {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			companyType CompanyType,"+
									"			responsabilities String,"+
									"			responsabilities Set<Project>,"+//duplicate
									" 			otherProjects List<Project>"+
									"		} " +
									"  }" +
						"}"+
						" module CV2 {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		SomeOtherEntity {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Certification {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			companyType CompanyType,"+
									"			responsabilities String,"+
									"			projects Set<Project>,"+
									" 			otherProjects List<Project>"+
									"		}" +
									" }" +
						"}"));
				//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.recruiter");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
	}


	@SuppressWarnings("rawtypes")
	@Test
	public void entityMeta() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
				" module CV {" +
							"  ui     {} " +
							"  logic  {}" +
							"  domain {" +
							"		SomeEntity {"+
							"			startDate Date,"+
							"			endDate   Date"+
							"		}"+
							""+	
							"	    Experience {"+
							"			startDate Date,"+
							"			endDate   Date,"+
							"			companyType CompanyType,"+
							"			responsabilities String,"+
							"			projects Set<Project>,"+
							" 			otherProjects List<Project>"+
							"		} " +
							"  }" +
				"}"+
				" module CV2 {" +
							"  ui     {} " +
							"  logic  {}" +
							"  domain {" +
							"		SomeDifferentEntity {"+
							"			startDate Date,"+
							"			endDate   Date"+
							"		}"+
							""+	
							"	    Certification {"+
							"			startDate Date,"+
							"			endDate   Date,"+
							"			companyType CompanyType,"+
							"			responsabilities String,"+
							"			projects Set<Project>,"+
							" 			otherProjects List<Project>"+
							"		}" +
							" }" +
				"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.recruiter");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);

		ProjectRoot projectRoot = generator.getProjectRoot();
		List<Resource> children = projectRoot.getChildren();

		Resource module1 = children.get(0);
		assertEquals("CV", ((ModuleResource) module1).getModuleName());

		Resource packageFolder = module1.getChild(0);
		assertTrue(packageFolder instanceof PackageResource);
		assertEquals(3, packageFolder.getChildren().size());

		assertTrue(packageFolder.getChild(1) instanceof ServiceResource);

		Resource modelFolder = packageFolder.getChild(0);
		assertTrue(modelFolder instanceof FolderResource);

		Resource entity1 = modelFolder.getChildren().get(0);
		assertTrue(entity1 instanceof JavaResource);
		JavaResource ent1 = (JavaResource) entity1;
		assertEquals("SomeEntity", ent1.getName());
		assertEquals(2, ent1.getFields().size());

		Resource entity2 = modelFolder.getChildren().get(1);
		assertTrue(entity2 instanceof JavaResource);
		JavaResource ent2 = (JavaResource) entity2;
		assertEquals(6, ent2.getFields().size());



		Resource module2 = children.get(1);

		Resource packageFolder2 = module2.getChild(0);
		assertTrue(packageFolder2 instanceof PackageResource);
		assertEquals(3, packageFolder2.getChildren().size());

		modelFolder = packageFolder2.getChildren().get(0);
		assertTrue(modelFolder instanceof FolderResource);

		Resource entity21 = modelFolder.getChildren().get(0);
		assertTrue(entity21 instanceof JavaResource);
		JavaResource ent21 = (JavaResource) entity21;
		assertEquals("SomeDifferentEntity", ent21.getName());
		assertEquals(2, ent21.getFields().size());

		Resource entity22 = modelFolder.getChildren().get(1);
		assertTrue(entity22 instanceof JavaResource);
		JavaResource ent22 = (JavaResource) entity22;
		assertEquals(6, ent22.getFields().size());

	}



	@Test
	public void packageNotInModule() throws ParseException, GenerateException {

	}


	@Test(expected = InvalidPackageException.class)
	public void entityNotInPackage() throws ParseException, GenerateException {
		//@formatter:off
				SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
								" module CV {" +
											"  ui     {} " +
											"  logic  {}" +
											"  domain {" +
											"		Customer {"+
											"			startDate Date,"+
											"			endDate   Date"+
											"		}"+
											""+	
											"	    Experience {"+
											"			startDate Date,"+
											"			endDate   Date,"+
											"			field 	  Blob"+
											"		} " +
											"  }" +
								"}"));
				//@formatter:on
		ctxt.setTemplateRootDir(new File(testTemplateDir, "entity-no-package"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
	}



	@Test(expected = ParseException.class)
	public void entityFieldArrayOfUnkown() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
									" module CV {" +
												"  ui     {} " +
												"  logic  {}" +
												"  domain {" +
												"		Customer {"+
												"			startDate Date,"+
												"			endDate   Date"+
												"		}"+
												""+	
												"	    Experience {"+
												"			startDate Date,"+
												"			endDate   Date,"+
												"			field 	  SomeParam[]"+
												"		} " +
												"  }" +
									"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
		generator.enhance(appModel);
	}


	@SuppressWarnings("rawtypes")
	@Test
	public void entityAutoDetectPackage() throws ParseException, GenerateException {
		//@formatter:off
				SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
								" module CV {" +
											"  ui     {} " +
											"  logic  {}" +
											"  domain {" +
											"		Customer {"+
											"			startDate Date,"+
											"			endDate   Date"+
											"		}"+
											""+	
											"	    Experience {"+
											"			startDate Date,"+
											"			endDate   Date,"+
											"			field 	  Blob"+
											"		} " +
											"  }" +
								"}"));
				//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setProperty(AUTO_DETECT_PACKAGE, "true");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity-no-package"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource module = root.getChild(0);
		assertEquals("CV", ((ModuleResource) module).getModuleName());

		Resource pkgFolderRoot = module.getChild(0);
		assertTrue(pkgFolderRoot instanceof FolderResource);
		Resource pkgFolder2 = pkgFolderRoot.getChild(0);
		assertTrue(pkgFolder2 instanceof FolderResource);
		Resource pkgFolder3 = pkgFolder2.getChild(0);
		assertTrue(pkgFolder3 instanceof FolderResource);
		Resource pkgFolder4 = pkgFolder3.getChild(0);
		assertTrue(pkgFolder4 instanceof FolderResource);

		assertEquals(2, pkgFolder4.getChildren().size());
		assertEquals(3, pkgFolder3.getChildren().size());

		JavaResource customer = (JavaResource) pkgFolder4.getChild(0);
		JavaResource experience = (JavaResource) pkgFolder4.getChild(1);

		assertEquals(2, customer.getFields().size());
		assertEquals(3, experience.getFields().size());

		assertEquals("ro.sft.example.model", customer.getPackage());
		assertEquals("ro.sft.example.model", experience.getPackage());
	}


	@Test
	public void entityDirectChildOfPackage() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
						" module CV {" +
									"  ui     {} " +
									"  logic  {}" +
									"  domain {" +
									"		Customer {"+
									"			startDate Date,"+
									"			endDate   Date"+
									"		}"+
									""+	
									"	    Experience {"+
									"			startDate Date,"+
									"			endDate   Date,"+
									"			field 	  Blob"+
									"		} " +
									"  }" +
						"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setProperty(AUTO_DETECT_PACKAGE, "true");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "entity-child-of-package"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		PackageResource pkg = (PackageResource) root.getChild(0).getChild(0);

		assertEquals("ro.sft.somepackage", pkg.getNamespace());

		assertTrue(pkg.getChild(1) instanceof FolderResource);
		assertTrue(pkg.getChild(2) instanceof JavaResource);
		assertTrue(pkg.getChild(3) instanceof JavaResource);
		assertTrue(pkg.getChild(4) instanceof ServiceResource);
		assertTrue(pkg.getChild(5) instanceof FileResource);
	}


	@Test
	public void serviceAndCrud() throws ParseException, GenerateException {
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
											"		service CV { " +
											"			crud CV { " +
											"				searcher WithFirstName {" +
											"					\"Select j from CV j where j.firstName = :firstName\"" +
											"				}" +
											"				searcher Certifications {"+ 
											"					\"Select cert from Certification cert where cert.name like :name\""+
											"				}" +
											"			}" +
											"			" +
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
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setProperty(AUTO_DETECT_PACKAGE, "true");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "entity-child-of-package"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
		generator.enhance(appModel);

		ProjectRoot project = generator.getProjectRoot();


	}


}
