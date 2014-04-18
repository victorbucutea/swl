package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.Annotation.AnnotationPropertyValue;
import ro.swl.engine.generator.javaee.enhancer.JPATechnology;
import ro.swl.engine.generator.javaee.exception.WrongRelatedFieldTypeException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class JpaEnhancerTests extends GeneratorTest {

	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> techs = new ArrayList<Technology>();
		techs.add(new InternalEnhancers());
		techs.add(new JPATechnology());
		return techs;
	}


	private void setUpResourceTree(SWL swl) throws ParseException, CreateException {
		ctxt.setDefaultPackage("ro.sft.somepackage");
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
	}



	@Test
	public void entityFieldOneToOne_bidirectional() throws ParseException, CreateException {

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
													"			experience Experience -> field"+		
													"		}"+
													"  }" +
										"}"));
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource experience = (EntityResource) modelFolder.getChild(0);
		EntityField expField = experience.getFields().get(2);

		assertDateFieldIsOk(experience.getFields().get(0));
		assertDateFieldIsOk(experience.getFields().get(1));



		assertEquals("field", expField.getName());

		// one to one owning relation
		assertOneToOneNonOwningAnnotations(expField);

		EntityResource customer = modelFolder.getChildCast(1);
		EntityField custField = customer.getFields().get(2);
		assertEquals("experience", custField.getName());

		// one to one 
		assertOneToOneOwningAnnotations(custField);
	}


	@Test
	public void entityFieldOneToOne() throws ParseException, CreateException {
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
													"			field 	  Customer -> experience," +
													" 			anInt     Integer," +
													"			aBlob     Blob"+
													"		} " +
													"  }" +
										"}"));
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField expField = customer.getFields().get(2);

		assertDateFieldIsOk(customer.getFields().get(0));
		assertDateFieldIsOk(customer.getFields().get(1));



		assertEquals("experience", expField.getName());

		// one to one owning relation
		assertOneToOneNonOwningAnnotations(expField);

		EntityResource experience = (EntityResource) modelFolder.getChild(1);
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());

		// one to one 
		assertOneToOneOwningAnnotations(custField);
	}


	@Test
	public void entityField_MultipleOwningOneToOne_unidirRelations() throws CreateException, ParseException {
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
															"			field 	  Customer," +
															"			field2    Customer," +
															"			field3    Customer"+
															"		} " +
															"  }" +
												"}"));
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField expField = customer.getFields().get(2);

		assertDateFieldIsOk(customer.getFields().get(0));
		assertDateFieldIsOk(customer.getFields().get(1));

		assertEquals("experience", expField.getName());
		assertOneToOneOwningAnnotations(expField);


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
	public void entityField_noRelatedFields() throws CreateException, ParseException {
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
																	"			field 	  Customer," +
																	"			field2    Set<Customer>," +
																	"			field3    Set<Customer> -> *"+
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTree(swl);
		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		// one to one owning relation
		EntityResource experience = (EntityResource) modelFolder.getChild(1);
		EntityField custField = experience.getFields().get(2);
		assertEquals("field", custField.getName());
		assertOneToOneOwningAnnotations(custField);

		custField = experience.getFields().get(3);
		assertEquals("field2", custField.getName());
		assertOneToManyOwningAnnotations(custField);

		custField = experience.getFields().get(4);
		assertEquals("field3", custField.getName());
		assertManyToManyOwningAnnotations(custField);
	}


	@Test
	public void entityField_MultipleOneToOneRelations_OwningSideSpecified() throws ParseException, CreateException {
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
															"			field 	  Customer -> experience1," +
															"			field2    Customer -> experience2," +
															"			field3    Customer -> experience3"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource cust = (EntityResource) modelFolder.getChild(0);
		EntityField exp1 = cust.getFields().get(2);
		assertEquals("experience1", exp1.getName());
		assertOneToOneNonOwningAnnotations(exp1);

		EntityField exp2 = cust.getFields().get(3);
		assertEquals("experience2", exp2.getName());
		assertOneToOneNonOwningAnnotations(exp2);

		EntityField exp3 = cust.getFields().get(4);
		assertEquals("experience3", exp3.getName());
		assertOneToOneNonOwningAnnotations(exp3);

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
	public void entityFieldManyToOneRelations() throws ParseException, CreateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience," +
															"			experience2 Order," +
															"			order Order"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer -> experience1," +
															"			field2    Order -> field2," +
															"			field3    Customer -> * "+
															"		} " +
															// field names are the same as the ones in Experience
															"	    Order {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Set<Customer> -> experience2," +
															"			field2    Experience," +
															"			field3    Customer -> *  "+
															"		} " +
															"  }" +
												"}"));
		
		//@formatter:on
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);

		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField exp1 = customer.getFields().get(2);
		assertOneToOneNonOwningAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertManyToOneOwningAnnotations(exp2);

		EntityResource exp = modelFolder.getChildCast(1);
		EntityField field = exp.getFields().get(2);
		assertOneToOneOwningAnnotations(field);

		EntityField field2 = exp.getFields().get(3);
		assertOneToOneOwningAnnotations(field2);

		EntityField field3 = exp.getFields().get(4);
		assertManyToOneOwningAnnotations(field3);


		EntityResource order = modelFolder.getChildCast(2);
		EntityField ordField = order.getFields().get(2);
		assertOneToManyNonOwningAnnotations(ordField);

		EntityField ordField2 = order.getFields().get(3);
		assertOneToOneNonOwningAnnotations(ordField2);

		EntityField ordField3 = order.getFields().get(4);
		assertManyToOneOwningAnnotations(ordField3);
	}


	@Test(expected = WrongRelatedFieldTypeException.class)
	public void entityFieldManyToOneRelations_wrongRelatedFieldType() throws ParseException, CreateException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience," +
															"			experience2 Order," +
															"			order Order"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Customer -> experience1," +
															"			field2    Order -> field2," +
															"			field3    Customer -> * "+
															"		} " +
															// field names are the same as the ones in Experience
															"	    Order {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			field 	  Set<Customer> -> experience2," +
															// this field is referenced by Experience.field2
															// it should be of type Experience
															"			field2    Customer," + 
															"			field3    Customer -> *  "+
															"		} " +
															"  }" +
												"}"));
		
		//@formatter:on
		setUpResourceTree(swl);
	}


	@Test
	public void entityFieldOneToMany() throws CreateException, ParseException {
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
																"			field 	  Set<Customer> -> experience1," +
																// fields below are unidirectional one-to-many
																"			field2    Set<Customer>," +
																"			field3    Set<Customer>"+
																"		} " +
																"  }" +
													"}"));
		//@formatter:on
		setUpResourceTree(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = (EntityResource) modelFolder.getChild(0);
		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertOneToOneOwningAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(4);
		assertOneToOneOwningAnnotations(exp3);

		EntityResource experience = modelFolder.getChildCast(1);
		EntityField custField = experience.getFields().get(2);
		assertOneToManyNonOwningAnnotations(custField);

		EntityField custField2 = experience.getFields().get(3);
		assertOneToManyOwningAnnotations(custField2);
		assertTrue(custField2.isUnidirectional());

		EntityField custField3 = experience.getFields().get(4);
		assertOneToManyOwningAnnotations(custField3);
		assertTrue(custField3.isUnidirectional());
	}


	@Test
	public void entityFieldManyToOneUnidirectional() throws CreateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience ->*,"+
															"			experience2 Experience ->*,"+	
															"			experience3 Experience ->*"+	
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date"+
															         // experience 1,2,3 is unidirectional - no corresponding one-to-many here
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		setUpResourceTree(swl);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);

		EntityField exp1 = customer.getFields().get(2);
		assertManyToOneOwningAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertManyToOneOwningAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(4);
		assertManyToOneOwningAnnotations(exp3);
	}


	@Test()
	public void entityFieldManyToMany() throws CreateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Set<Experience> -> *,"+
															"			experience2 Set<Experience>,"+	
															"			experience3 Set<Experience> -> customers1, "+
															"			experience4 Set<Experience> "+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customers1 Set<Customer>,"+
															"			customers2 Set<Customer> -> experience4"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		setUpResourceTree(swl);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);

		EntityField exp1 = customer.getFields().get(2);
		assertManyToManyOwningAnnotations(exp1);

		EntityField exp2 = customer.getFields().get(3);
		assertOneToManyOwningAnnotations(exp2);

		EntityField exp3 = customer.getFields().get(4);
		assertManyToManyOwningAnnotations(exp3);

		EntityField exp4 = customer.getFields().get(5);
		assertManyToManyNonOwningAnnotations(exp4);



		EntityResource experience = modelFolder.getChildCast(1);

		EntityField cus1 = experience.getFields().get(2);
		assertManyToManyNonOwningAnnotations(cus1);

		EntityField cus2 = experience.getFields().get(3);
		assertManyToManyOwningAnnotations(cus2);
	}


	@Test
	public void internalTypesRegistry() throws ParseException, CreateException {
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
							"		CompanyType { " +
							"			someProperty String"+		
							"		}" +
							"		Project { " +
							"			someProperty String"+		
							"		}" +
							" }" +
				"}"));
		//@formatter:on
		ctxt.setDefaultPackage("ro.sft.somepackage");
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);

		String fqName = ctxt.getFqNameForRegisteredType("SomeDifferentEntity");
		assertEquals("ro.sft.somepackage.model.SomeDifferentEntity", fqName);

		fqName = ctxt.getFqNameForRegisteredType("Certification");
		assertEquals("ro.sft.somepackage.model.Certification", fqName);

		fqName = ctxt.getFqNameForRegisteredType("Experience");
		assertEquals("ro.sft.somepackage.model.Experience", fqName);

		fqName = ctxt.getFqNameForRegisteredType("SomeEntity");
		assertEquals("ro.sft.somepackage.model.SomeEntity", fqName);

	}


	public void entityManyToOneInDifferentModules() {

	}


	public void entityManyToOneInDifferentPackages() {

	}


	public void entityManyToOneInDifferentPackageInSameModule() {

	}


	private void assertManyToOneAnnotations(EntityField custField) {
		List<Annotation> annotations = custField.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("ManyToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.ManyToOne", onetoOne.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoOne.getAttributes();
		assertEquals(0, attributes.size());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationPropertyValue name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(custField.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

		assertEquals(2, annotations.size());
	}


	private void assertManyToManyNonOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation manyToMany = annotations.get(0);
		assertEquals("ManyToMany", manyToMany.getSimpleName());
		assertEquals("javax.persistence.ManyToMany", manyToMany.getFqName());
		Collection<AnnotationPropertyValue> attributes = manyToMany.getAttributes();
		assertEquals(2, attributes.size());
		AnnotationPropertyValue cascade = manyToMany.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getTypeValues().get(0).getFqName());

		assertEquals(1, annotations.size());
	}


	private void assertManyToManyOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation manyToMany = annotations.get(0);
		assertEquals("ManyToMany", manyToMany.getSimpleName());
		assertEquals("javax.persistence.ManyToMany", manyToMany.getFqName());
		Collection<AnnotationPropertyValue> attributes = manyToMany.getAttributes();
		assertEquals(0, attributes.size());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationPropertyValue name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(field.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

		assertEquals(2, annotations.size());
	}


	private void assertOneToManyOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation onetoMany = annotations.get(0);
		assertEquals("OneToMany", onetoMany.getSimpleName());
		assertEquals("javax.persistence.OneToMany", onetoMany.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoMany.getAttributes();

		assertEquals(2, attributes.size());
		assertEquals(2, annotations.size());

		AnnotationPropertyValue cascade = onetoMany.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getTypeValues().get(0).getFqName());
		AnnotationPropertyValue orphanRemove = onetoMany.getAttribute("orphanRemoval");
		assertEquals("true", orphanRemove.getValueLiterals().get(0));

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationPropertyValue name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(field.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

	}


	private void assertManyToOneOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("ManyToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.ManyToOne", onetoOne.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoOne.getAttributes();
		assertEquals(0, attributes.size());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationPropertyValue name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(field.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));
		assertEquals(2, annotations.size());
	}


	private void assertOneToManyNonOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("OneToMany", onetoOne.getSimpleName());
		assertEquals("javax.persistence.OneToMany", onetoOne.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoOne.getAttributes();
		assertEquals(2, attributes.size());
		AnnotationPropertyValue cascade = onetoOne.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getTypeValues().get(0).getFqName());
		assertEquals(1, annotations.size());
	}


	private void assertOneToOneOwningAnnotations(EntityField field) {
		List<Annotation> annotations = field.getAnnotations();
		Annotation onetoOne = annotations.get(0);
		assertEquals("OneToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.OneToOne", onetoOne.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoOne.getAttributes();
		assertEquals(1, attributes.size());
		AnnotationPropertyValue cascade = onetoOne.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getTypeValues().get(0).getFqName());

		Annotation joinColumn = annotations.get(1);
		assertEquals("JoinColumn", joinColumn.getSimpleName());
		assertEquals("javax.persistence.JoinColumn", joinColumn.getFqName());
		AnnotationPropertyValue name = joinColumn.getAttribute("name");
		assertNotNull(name.getValueLiterals().get(0));
		assertEquals(field.getUpperUnderscoreName() + "_ID", name.getValueLiterals().get(0));

		assertEquals(2, annotations.size());
	}


	private void assertOneToOneNonOwningAnnotations(EntityField custField) {
		List<Annotation> relation = custField.getAnnotations();
		assertEquals(1, relation.size());
		Annotation onetoOne = relation.get(0);
		assertEquals("OneToOne", onetoOne.getSimpleName());
		assertEquals("javax.persistence.OneToOne", onetoOne.getFqName());
		Collection<AnnotationPropertyValue> attributes = onetoOne.getAttributes();
		// no cascade
		assertEquals(1, attributes.size());
		AnnotationPropertyValue cascade = onetoOne.getAttribute("cascade");
		assertNotNull(cascade);
		assertEquals("javax.persistence.CascadeType.ALL", cascade.getTypeValues().get(0).getFqName());

	}


	private void assertDateFieldIsOk(EntityField entityField) {

		List<Annotation> annotations = entityField.getAnnotations();

		Annotation column = annotations.get(0);
		assertEquals("Column", column.getSimpleName());
		assertEquals("javax.persistence.Column", column.getFqName());
		AnnotationPropertyValue columnName = column.getAttribute("name");
		assertNotNull(columnName.getValueLiterals().get(0));
		assertEquals(entityField.getUpperUnderscoreName(), columnName.getValueLiterals().get(0));

		Annotation temporal = annotations.get(1);
		assertEquals("Temporal", temporal.getSimpleName());
		assertEquals("javax.persistence.Temporal", temporal.getFqName());

		AnnotationPropertyValue temporalValue = temporal.getAttribute("value");
		assertNotNull(temporalValue.getTypeValues().get(0));
		assertEquals("javax.persistence.TemporalType.TIMESTAMP", temporalValue.getTypeValues().get(0).getFqName());
	}



}
