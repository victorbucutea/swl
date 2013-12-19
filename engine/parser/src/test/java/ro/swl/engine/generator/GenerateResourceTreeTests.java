package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static ro.swl.engine.generator.GenerationContext.AUTO_DETECT_PACKAGE;
import static ro.swl.engine.generator.GenerationContext.PACKAGE;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ro.swl.engine.AbstractTest;
import ro.swl.engine.generator.javaee.exception.CardinalityUnkownException;
import ro.swl.engine.generator.javaee.exception.DuplicateEntityException;
import ro.swl.engine.generator.javaee.exception.DuplicateFieldNameException;
import ro.swl.engine.generator.javaee.exception.InvalidPackageException;
import ro.swl.engine.generator.javaee.exception.NoModuleException;
import ro.swl.engine.generator.javaee.exception.RelatedEntityNotFoundException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.generator.javaee.model.ModuleResource;
import ro.swl.engine.generator.javaee.model.PackageResource;
import ro.swl.engine.generator.javaee.model.PersistenceXml;
import ro.swl.engine.generator.javaee.model.ServiceBeanResource;
import ro.swl.engine.generator.javaee.model.ServiceResource;
import ro.swl.engine.generator.model.Annotation;
import ro.swl.engine.generator.model.Annotation.AnnotationProperty;
import ro.swl.engine.generator.model.FileResource;
import ro.swl.engine.generator.model.FolderResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class GenerateResourceTreeTests extends AbstractTest {

	private ProjectGenerator generator;
	private GenerationContext ctxt;
	private File testTemplateDir;


	@Before
	public void setUp() throws Exception {
		ctxt = new GenerationContext();
		generator = new ProjectGenerator(ctxt);
		testTemplateDir = new File(getClass().getClassLoader().getResource("generate/").toURI());
	}


	@Test
	public void simpleResourceTreeGeneration() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();
		File testDir = new File(testTemplateDir, "simple-template");

		generator.generate(appModel, testDir);

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
		assertEquals("src", ejbSource.getTemplateFile().getName());
		assertEquals("test", projects.get(2).getChildren().get(1).getTemplateFile().getName());
		assertEquals("pom.xml", projects.get(2).getChildren().get(2).getTemplateFile().getName());
		assertEquals("test.xml", projects.get(2).getChildren().get(3).getTemplateFile().getName());

		Resource rootArtifactId = ejbSource.getChildren().get(0).getChildren().get(0).getChildren().get(0);

		Resource moduleTemplate = rootArtifactId.getChildren().get(0);
		assertTrue(moduleTemplate instanceof ModuleResource);

		Resource moduleTemplate2 = rootArtifactId.getChildren().get(1);
		assertTrue(moduleTemplate2 instanceof ModuleResource);


		Resource resources = ejbSource.getChildren().get(0).getChildren().get(1);
		Resource metaInf = resources.getChildren().get(0);
		assertEquals("META-INF", metaInf.getTemplateFile().getName());
		assertEquals(2, metaInf.getChildren().size());

		assertEquals("beans.xml", metaInf.getChildren().get(0).getTemplateFile().getName());

		Resource persistencexml = metaInf.getChildren().get(1);
		assertTrue(persistencexml instanceof PersistenceXml);
		assertEquals("persistence.xml", persistencexml.getTemplateFile().getName());
	}


	@Test
	public void moduleWithNoEntitiesInModel() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();

		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));

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

		generator.generate(swl.SwdlApp(), new File(testTemplateDir, "entity-no-module"));
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

		generator.generate(swl.SwdlApp(), new File(testTemplateDir, "module-no-entity"));

		ProjectRoot root = generator.getProjectRoot();

		ModuleResource module = (ModuleResource) root.getChild(0).getChild(0);
		assertEquals("CV", module.getModuleName());

		Resource file = module.getChild(0).getChild(0);

		assertFalse(file instanceof EntityResource);
	}


	@Test
	public void entityInTemplateButNotInModel() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n module SS {} ";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();

		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));

		ProjectRoot root = generator.getProjectRoot();
		assertTrue(root.getChild(0) instanceof ModuleResource);

	}


	@Test(expected = NoModuleException.class)
	public void moduleInModelButNotInTemplate() throws GenerateException, ParseException {
		String string = " name \"x\" \n\t\n  module CV { logic{} } module some_other { ui{}}";
		SWL swl = new SWL(createInputStream(string));
		ASTSwdlApp appModel = swl.SwdlApp();

		generator.generate(appModel, new File(testTemplateDir, "entity-no-module"));

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
		generator.generate(new ASTSwdlApp(0), new File(testTemplateDir, "module"));
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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
	}


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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));

		ProjectRoot projectRoot = generator.getProjectRoot();
		List<Resource> children = projectRoot.getChildren();

		Resource module1 = children.get(0);
		assertEquals("CV", ((ModuleResource) module1).getModuleName());

		Resource packageFolder = module1.getChild(0);
		assertTrue(packageFolder instanceof PackageResource);
		assertEquals(3, packageFolder.getChildren().size());

		assertTrue(packageFolder.getChild(1) instanceof ServiceResource);
		assertTrue(packageFolder.getChild(2) instanceof ServiceBeanResource);

		Resource modelFolder = packageFolder.getChild(0);
		assertTrue(modelFolder instanceof FolderResource);

		Resource entity1 = modelFolder.getChildren().get(0);
		assertTrue(entity1 instanceof EntityResource);
		EntityResource ent1 = (EntityResource) entity1;
		assertEquals("SomeEntity", ent1.getName());
		assertEquals(2, ent1.getFields().size());

		Resource entity2 = modelFolder.getChildren().get(1);
		assertTrue(entity2 instanceof EntityResource);
		EntityResource ent2 = (EntityResource) entity2;
		assertEquals(6, ent2.getFields().size());



		Resource module2 = children.get(1);

		Resource packageFolder2 = module2.getChild(0);
		assertTrue(packageFolder2 instanceof PackageResource);
		assertEquals(3, packageFolder2.getChildren().size());

		modelFolder = packageFolder2.getChildren().get(0);
		assertTrue(modelFolder instanceof FolderResource);

		Resource entity21 = modelFolder.getChildren().get(0);
		assertTrue(entity21 instanceof EntityResource);
		EntityResource ent21 = (EntityResource) entity21;
		assertEquals("SomeDifferentEntity", ent21.getName());
		assertEquals(2, ent21.getFields().size());

		Resource entity22 = modelFolder.getChildren().get(1);
		assertTrue(entity22 instanceof EntityResource);
		EntityResource ent22 = (EntityResource) entity22;
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

		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "entity-no-package"));
	}


	@Test(expected = RelatedEntityNotFoundException.class)
	public void entityFieldOfUknownType() throws ParseException, GenerateException {
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
									"			field 	  InexistingType"+
									"		} " +
									"  }" +
						"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

	}



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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity-no-package"));
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

		EntityResource customer = (EntityResource) pkgFolder4.getChild(0);
		EntityResource experience = (EntityResource) pkgFolder4.getChild(1);

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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "entity-child-of-package"));
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		PackageResource pkg = (PackageResource) root.getChild(0).getChild(0);

		assertEquals("ro.sft.somepackage", pkg.getNamespace());

		assertTrue(pkg.getChild(0) instanceof FolderResource);
		assertTrue(pkg.getChild(1) instanceof EntityResource);
		assertTrue(pkg.getChild(2) instanceof EntityResource);
		assertTrue(pkg.getChild(3) instanceof ServiceResource);
		assertTrue(pkg.getChild(4) instanceof ServiceBeanResource);
	}



	@Test(expected = RelatedEntityNotFoundException.class)
	public void entityFieldIsCollectionOfUnkownParameter() throws ParseException, GenerateException {
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
											"			field 	  Set<SomeParam>"+
											"		} " +
											"  }" +
								"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
	}


	@Test
	public void entityFieldInternalTypes() throws ParseException, GenerateException {
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
									"	    Experience {"+
									"			startDate Date,"+
									"			blob   Blob,"+
									"			field2  List<Customer>,"+
									"			field  Set<Customer>,"+
									"			field3  Integer,"+
									"			field4  int,"+
									"			field5  Long,"+
									"			field6  long,"+
									"			field7  Double,"+
									"			field8  double"+
									"		} " +
									"  }" +
						"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		Resource module = generator.getProjectRoot().getChild(0);
		Resource modelFolder = module.getChild(0).getChild(0);

		assertEquals(2, modelFolder.getChildren().size());

		EntityResource experience = (EntityResource) modelFolder.getChild(1);

		assertEquals(10, experience.getFields().size());

		assertNull(experience.getFields().get(9).getType().getImport());
		assertEquals("double", experience.getFields().get(9).getType().getFqName());

		assertNull(experience.getFields().get(7).getType().getImport());
		assertEquals("long", experience.getFields().get(7).getType().getFqName());

		assertNull(experience.getFields().get(5).getType().getImport());
		assertEquals("int", experience.getFields().get(5).getType().getFqName());

		EntityType set = experience.getFields().get(3).getType();
		assertEquals("java.util.Set", set.getImport());
		assertEquals("Customer", set.getParameter());

		EntityType list = experience.getFields().get(2).getType();
		assertEquals("java.util.List", list.getImport());
		assertEquals("Customer", list.getParameter());


		assertEquals("java.util.Date", experience.getFields().get(0).getType().getImport());
		assertEquals("java.util.Date", experience.getFields().get(0).getType().getFqName());
		assertEquals("Date", experience.getFields().get(0).getType().getSimpleClassName());

		assertNull(experience.getFields().get(1).getType().getImport());
		assertEquals("byte[]", experience.getFields().get(1).getType().getFqName());
		assertEquals("byte[]", experience.getFields().get(1).getType().getSimpleClassName());

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
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
	}


	@Test
	public void entityFieldOneToOneOwningSideSpecified() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
										" module CV {" +
													"  ui     {} " +
													"  logic  {}" +
													"  domain {" +
													"	    Experience {"+
													"			startDate Date,"+
													"			endDate   Date,"+
													"			field 	  Customer"+
													"		} " +
													"		Customer {"+
													"			startDate Date,"+
													"			endDate   Date,"+
													"			experience Experience*"+		
													"		}"+
													"  }" +
										"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource experience = (EntityResource) modelFolder.getChild(0);
		EntityField expField = experience.getFields().get(2);

		assertDateFieldIsOk(experience.getFields().get(0));
		assertDateFieldIsOk(experience.getFields().get(1));



		assertEquals("field", expField.getName());

		// one to one owning relation
		assertOneToOneNonOwningAnnotations(expField);

		EntityResource customer = (EntityResource) modelFolder.getChild(1);
		EntityField custField = customer.getFields().get(2);
		assertEquals("experience", custField.getName());

		// one to one 
		assertOneToOneOwningAnnotations(custField);
	}


	@Test
	public void entityFieldOneToOne() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
										" module CV {" +
													"  ui     {} " +
													"  logic  {}" +
													"  domain {" +
													"		Customer {"+
													"			startDate Date,"+
													"			endDate   Date,"+
													"			experience Experience"+		
													"		}"+
													"	    Experience {"+
													"			startDate Date,"+
													"			endDate   Date,"+
													"			field 	  Customer"+
													"		} " +
													"  }" +
										"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField expField = customer.getFields().get(2);

		assertDateFieldIsOk(customer.getFields().get(0));
		assertDateFieldIsOk(customer.getFields().get(1));



		assertEquals("experience", expField.getName());

		// one to one owning relation
		assertOneToOneOwningAnnotations(expField);

		EntityResource experience = (EntityResource) modelFolder.getChild(1);
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());

		// one to one 
		assertOneToOneNonOwningAnnotations(custField);
	}


	@Test
	public void entityDuplicateOneToOneRelation() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			experience Experience"+		
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer*," +
															"			field2    Customer*," +
															"			field3    Customer*"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField expField = customer.getFields().get(2);

		assertDateFieldIsOk(customer.getFields().get(0));
		assertDateFieldIsOk(customer.getFields().get(1));

		assertEquals("experience", expField.getName());
		assertOneToOneNonOwningAnnotations(expField);


		// one to one owning relation
		EntityResource experience = (EntityResource) modelFolder.getChild(1);
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToOneOwningAnnotations(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertEquals("field2", custField2.getName());
		assertOneToOneOwningAnnotations(custField2);

		EntityField custField3 = experience.getFields().get(4);
		assertEquals("field3", custField3.getName());
		assertOneToOneOwningAnnotations(custField3);

	}


	@Test
	public void entityFieldOneToOneUnidirectional() throws ParseException, GenerateException {
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
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer*," +
															"			field2    Customer*," +
															"			field3    Customer*"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = (EntityResource) modelFolder.getChild(1);



		// one to one owning relation
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToOneOwningAnnotations(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertEquals("field2", custField2.getName());
		assertOneToOneOwningAnnotations(custField2);

		EntityField custField3 = experience.getFields().get(4);
		assertEquals("field3", custField3.getName());
		assertOneToOneOwningAnnotations(custField3);


	}


	@Test(expected = CardinalityUnkownException.class)
	public void entityOneToOneNoOwningSide() throws GenerateException, ParseException {
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
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer*," +
																	"			field2    Customer*," +
																	"			field3    Customer"+// no owning side
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
	}


	@Test
	public void entityFieldOneToOneMultipleRelations_OwningSideSpecified() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+	
															"			experience3 Experience"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer*," +
															"			field2    Customer*," +
															"			field3    Customer*"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = (EntityResource) modelFolder.getChild(1);


		// one to one owning relation
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToOneOwningAnnotations(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertEquals("field2", custField2.getName());
		assertOneToOneOwningAnnotations(custField2);

		EntityField custField3 = experience.getFields().get(4);
		assertEquals("field3", custField3.getName());
		assertOneToOneOwningAnnotations(custField3);
	}


	@Test
	public void entityFieldOneToOneMultipleRelations_() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience" +
															"			order Order"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer * -> experience1," +
															"			field2    Customer," +
															"			field3    Customer * ->  "+
															"		} " +
															"	    Order {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer * -> experience1," +
															"			field2    Customer," +
															"			field3    Customer * ->  "+
															"		} " +
															"  }" +
												"}"));
	}


	@Test
	public void entityFieldOneToOneMultipleRelations_OwningSideNotSpecified() throws ParseException, GenerateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+	
															"			experience3 Experience"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer," +
															"			field2    Customer," +
															"			field3    Customer"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = (EntityResource) modelFolder.getChild(1);


		// one to one owning relation
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToOneNonOwningAnnotations(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertEquals("field2", custField2.getName());
		assertOneToOneNonOwningAnnotations(custField2);

		EntityField custField3 = experience.getFields().get(4);
		assertEquals("field3", custField3.getName());
		assertOneToOneNonOwningAnnotations(custField3);


		EntityResource customer = (EntityResource) modelFolder.getChild(0);


		// one to one owning relation
		EntityField expField1 = customer.getFields().get(2);
		assertEquals("experience1", expField1.getName());
		assertOneToOneOwningAnnotations(expField1);

		EntityField expField2 = customer.getFields().get(3);
		assertEquals("experience2", expField2.getName());
		assertOneToOneOwningAnnotations(expField2);

		EntityField expField3 = customer.getFields().get(4);
		assertEquals("experience3", expField3.getName());
		assertOneToOneOwningAnnotations(expField3);
	}


	@Test
	public void entityFieldManyToOne() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
													" module CV {" +
																"  ui     {} " +
																"  logic  {}" +
																"  domain {" +
																"		Customer {"+
																"			startDate Date,"+
																"			endDate   Date," +
																"			experience1 Experience,"+
																"			experience2 Experience,"+	
																"			experience3 Experience"+	
																"		}"+
																"	    Experience {"+
																"			startDate Date,"+
																"			endDate   Date,"+
																"			field 	  Set<Customer>," +
																"			field2    Set<Customer>*," +
																"			field3    Set<Customer>"+
																"		} " +
																"  }" +
													"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		// one to many side of relation
		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp3);

		EntityResource experience = (EntityResource) modelFolder.getChild(1);
		// many to one owning relation
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToManyAnnotationsOk(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertEquals("field2", custField2.getName());
		assertOneToManyAnnotationsOk(custField);

		EntityField custField3 = experience.getFields().get(4);
		assertEquals("field3", custField3.getName());
		assertOneToManyAnnotationsOk(custField3);
	}


	/**
	 * This is something that should be supported in some time. It cannot be
	 * achieved by simple declarations,
	 * because if the other side is not declared, we can't know what the
	 * relation is a one-to-one or a one-to-many
	 * 
	 */
	@Test(expected = CardinalityUnkownException.class)
	public void entityFieldManyToOneUnidirectional() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+	
															"			experience3 Experience"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date"+
															         // experience3 is unidirectional - no corresponding many-to-one
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
	}


	@Test(expected = CardinalityUnkownException.class)
	public void entityFieldManyToOneUnidirectionalForSomeFields() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+	
															"			experience3 Experience"+// no owning side
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customers1 Set<Customer>,"+
															"			customers2 Set<Customer>"+
															         // experience3 is unidirectional - no corresponding many-to-one
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

	}


	@Test
	public void entityFieldManyToOneUnidirectionalForSomeFields_ButOneToOneOwningSideSpecified()
			throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+
															// unidirectional owning side oneToOne
															"			experience3 Experience*"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customers1 Set<Customer>,"+
															"			customers2 Set<Customer>"+
															         // experience3 is unidirectional - no corresponding many-to-one
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		// one to many side of relation
		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertManyToOneAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(4);
		assertOneToOneOwningAnnotations(exp3);

		EntityResource experience = (EntityResource) modelFolder.getChild(1);

		EntityField cust1 = experience.getFields().get(2);
		assertOneToManyAnnotationsOk(cust1);

		EntityField cust2 = experience.getFields().get(2);
		assertOneToManyAnnotationsOk(cust2);
	}


	@Test
	public void entityFieldManyToOneUnidirectionalForSomeFields_ButManyToOneOwningSideSpecified()
			throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience,"+
															// unidirectional one-to-many
															"			experience3 Experience &"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customers1 Set<Customer>,"+
															"			customers2 Set<Customer>"+
															         // experience3 is unidirectional - no corresponding many-to-one
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		// one to many side of relation
		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertManyToOneAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(4);
		assertManyToOneAnnotations(exp3);

		EntityResource experience = (EntityResource) modelFolder.getChild(1);

		EntityField cust1 = experience.getFields().get(2);
		assertOneToManyAnnotationsOk(cust1);

		EntityField cust2 = experience.getFields().get(2);
		assertOneToManyAnnotationsOk(cust2);

	}


	@Test
	public void jaxRs_OneToOne() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience,"+
															"			experience2 Experience*"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer1 Customer,"+
															"			customer2 Customer"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityResource experience = (EntityResource) modelFolder.getChild(1);


		EntityField experience1 = customer.getField(2);
		assertJaxRsOneToOneOwningAnotation(experience1);

		EntityField experience2 = customer.getField(3);
		assertJaxRsOneToOneNonOwningAnotation(experience2);


		EntityField customer1 = experience.getField(2);
		assertJaxRsOneToOneNonOwningAnotation(customer1);

		EntityField customer2 = experience.getField(3);
		assertJaxRsOneToOneOwningAnotation(customer2);

	}


	private void assertJaxRsOneToOneOwningAnotation(EntityField field) {
		Annotation managedRef = field.getAnnotations().get(1);
		assertEquals("JsonManagedReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonManagedReference", managedRef.getFqName());
	}


	private void assertJaxRsOneToOneNonOwningAnotation(EntityField field) {
		Annotation managedRef = field.getAnnotations().get(1);
		assertEquals("JsonBackReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonBackReference", managedRef.getFqName());
	}


	@Test
	public void entityFieldManyToOneMultipleRelations() throws GenerateException, ParseException {
		//@formatter:off
				SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
															" module CV {" +
																		"  ui     {} " +
																		"  logic  {}" +
																		"  domain {" +
																		"		Customer {"+
																		"			startDate Date,"+
																		"			endDate   Date," +
																		"			experience1 Experience,"+
																		"			experience2 Experience,"+	
																		"			experience3 Experience"+	
																		"		}"+
																		"	    Experience {"+
																		"			startDate Date,"+
																		"			endDate   Date,"+
																		"			field 	  Set<Customer>," +
																		"			field2    Set<Customer>*," +
																		"			field3    Set<Customer>"+
																		"		} " +
																		"  }" +
															"}"));
				//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		// one to many side of relation
		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp1);

	}


	public void entityManyToOneInDifferentModules() {

	}


	public void entityManyToOneInDifferentPackages() {

	}


	public void entityManyToOneInDifferentPackageInSameModule() {

	}


	public void entityFieldOneToMany() {

	}


	public void entityFieldOneToManyUnidirectional() {

	}


	public void entityFieldOneToManyMultipleRelations() {

	}


	public void entityFieldManyToMany() {

	}


	public void entityFieldManyToManyUnidirectional() {

	}


	private void assertManyToOneAnnotations(EntityField custField) {
		List<Annotation> annotations = custField.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("ManyToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.ManyToOne", onetoOne.getFqName());
		Collection<AnnotationProperty> attributes = onetoOne.getAttributes();
		assertEquals(0, attributes.size());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationProperty name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(custField.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

		assertEquals(2, annotations.size());

	}


	private void assertOneToManyAnnotationsOk(EntityField exp) {
		List<Annotation> annotations = exp.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("OneToMany", onetoOne.getSimpleName());
		assertEquals("javax.persistence.OneToMany", onetoOne.getFqName());
		Collection<AnnotationProperty> attributes = onetoOne.getAttributes();
		assertEquals(2, attributes.size());
		AnnotationProperty cascade = onetoOne.getAttribute("cascade");
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getValues().get(0).getFqName());
		AnnotationProperty orphanRemove = onetoOne.getAttribute("orphanRemoval");
		assertEquals("true", orphanRemove.getValueLiterals().get(0));

		assertEquals(1, annotations.size());

	}



	private void assertOneToOneOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("OneToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.OneToOne", onetoOne.getFqName());
		Collection<AnnotationProperty> attributes = onetoOne.getAttributes();
		assertEquals(1, attributes.size());
		AnnotationProperty cascade = onetoOne.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getValues().get(0).getFqName());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationProperty name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(field.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

		assertEquals(2, annotations.size());
	}


	private void assertOneToOneNonOwningAnnotations(EntityField custField) {
		List<Annotation> relation = custField.getAnnotations();
		assertEquals(1, relation.size());
		Annotation onetoOne2 = relation.get(0);
		assertEquals("OneToOne", onetoOne2.getSimpleName());
		assertEquals("javax.persistence.OneToOne", onetoOne2.getFqName());
		// no cascade
		assertEquals(0, onetoOne2.getAttributes().size());
	}


	private void assertDateFieldIsOk(EntityField entityField) {

		List<Annotation> annotations = entityField.getAnnotations();

		Annotation column = annotations.get(0);
		assertEquals("Column", column.getSimpleName());
		assertEquals("javax.persistence.Column", column.getFqName());
		AnnotationProperty columnName = column.getAttribute("name");
		assertNotNull(columnName.getValueLiterals().get(0));
		assertEquals(entityField.getUpperUnderscoreName(), columnName.getValueLiterals().get(0));

		Annotation temporal = annotations.get(1);
		assertEquals("Temporal", temporal.getSimpleName());
		assertEquals("javax.persistence.Temporal", temporal.getFqName());

		AnnotationProperty temporalValue = temporal.getAttribute("value");
		assertNotNull(temporalValue.getValues().get(0));
		assertEquals("javax.persistence.TemporalType.TIMESTAMP", temporalValue.getValues().get(0).getFqName());
	}
}
