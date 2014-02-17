package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static ro.swl.engine.generator.GenerationContext.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.java.model.AbstractField;
import ro.swl.engine.generator.java.model.JavaResource;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.javaee.exception.DuplicateDeclaredRelation;
import ro.swl.engine.generator.javaee.exception.RelatedEntityNotFoundException;
import ro.swl.engine.generator.javaee.exception.RelatedFieldNotFoundException;
import ro.swl.engine.generator.javaee.exception.WrongRelatedFieldTypeException;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class RelationsEnhancerTest extends GeneratorTest {


	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> techs = new ArrayList<Technology>();
		techs.add(new InternalEnhancers(ctxt));
		return techs;
	}


	private void setUpResourceTreeAndEnhance(SWL swl) throws ParseException, GenerateException {
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ctxt.setTemplateRootDir(new File(testTemplateDir, "module-and-entity"));
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel);
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
									"			startDt Date,"+
									"			blob    Blob,"+
									"			field2  List<Customer>,"+
									"			field   Set<Customer>,"+
									"			field3  Integer,"+
									"			field4  int," +
									"			field5  Long,"+
									"			field6  long,"+
									"			field7  Double,"+
									"			field8  double," +
									"			field9  String"+
									"		} " +
									"  }" +
						"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);

		Resource module = generator.getProjectRoot().getChild(0);
		Resource modelFolder = module.getChild(0).getChild(0);

		assertEquals(2, modelFolder.getChildren().size());

		JavaResource<Type, AbstractField<Type>> experience = modelFolder.getChildCast(1);

		assertEquals(11, experience.getFields().size());

		assertNull(experience.getFields().get(9).getType().getImport());
		assertEquals("double", experience.getFields().get(9).getType().getFqName());

		assertNull(experience.getFields().get(7).getType().getImport());
		assertEquals("long", experience.getFields().get(7).getType().getFqName());

		assertNull(experience.getFields().get(5).getType().getImport());
		assertEquals("int", experience.getFields().get(5).getType().getFqName());

		Type set = experience.getFields().get(3).getType();
		assertEquals("java.util.Set", set.getImport());
		assertEquals("Customer", set.getParameter());

		Type list = experience.getFields().get(2).getType();
		assertEquals("java.util.List", list.getImport());
		assertEquals("Customer", list.getParameter());


		assertEquals("java.util.Date", experience.getFields().get(0).getType().getImport());
		assertEquals("java.util.Date", experience.getFields().get(0).getType().getFqName());
		assertEquals("Date", experience.getFields().get(0).getType().getSimpleClassName());

		assertNull(experience.getFields().get(1).getType().getImport());
		assertEquals("byte[]", experience.getFields().get(1).getType().getFqName());
		assertEquals("byte[]", experience.getFields().get(1).getType().getSimpleClassName());

	}


	@Test
	public void entityFieldXToOne_unidirectional() throws GenerateException, ParseException {
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
																	"			field 	  Customer -> *," +
																	"			field2    Customer"+
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);

		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = modelFolder.getChildCast(1);


		// one to one owning relation
		assertFieldOwningManyToOne(experience, 2, "field");
		assertFieldUnidirectional(experience, 2);
		assertFieldOwningOneToOne(experience, 3, "field2");
		assertFieldUnidirectional(experience, 3);

	}


	@Test
	public void entityFieldOneToOne_owningSideSpecified() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+	
																	"			experience1 Experience -> field,"+
																	"			experience2 Experience -> field2,"+	
																	"			experience3 Experience -> field3"+
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
		setUpResourceTreeAndEnhance(swl);


		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = modelFolder.getChildCast(1);

		assertFieldNonOwningOneToOne(experience, 2, "field");
		assertFieldNonOwningOneToOne(experience, 3, "field2");
		assertFieldNonOwningOneToOne(experience, 4, "field3");


		EntityResource customer = modelFolder.getChildCast(0);
		assertFieldOwningOneToOne(customer, 2, "experience1");
		assertFieldOwningOneToOne(customer, 3, "experience2");
		assertFieldOwningOneToOne(customer, 4, "experience3");

	}


	@Test
	public void entityFieldOneToOne_owningSideSpecified_reverse() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+	
																	"			experience1 Experience,"+
																	"			experience2 Experience,"+	
																	"			experience3 Experience," +
																	"			someField Date," +
																	"			someOtherField int," +
																	"			integer Integer," +
																	"			longField Long," +
																	"			img Blob" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer -> experience1," +
																	"			field2    Customer -> experience2," +
																	"			field3    Customer -> experience3," +
																	"			someStr   String"+
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);


		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = modelFolder.getChildCast(1);

		assertFieldOwningOneToOne(experience, 2, "field");
		assertFieldOwningOneToOne(experience, 3, "field2");
		assertFieldOwningOneToOne(experience, 4, "field3");


		EntityResource customer = modelFolder.getChildCast(0);
		assertFieldNonOwningOneToOne(customer, 2, "experience1");
		assertFieldNonOwningOneToOne(customer, 3, "experience2");
		assertFieldNonOwningOneToOne(customer, 4, "experience3");

	}


	@Test
	public void entityField_MultipleXToOneRelationTypes() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Experience -> field,"+
																	"			experience2 Experience -> field2,"+	
																	"			experience3 Experience -> *"+
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Set<Customer>," +
																	"			field2    Customer," +
																	"			field3    Customer"+
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);


		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = modelFolder.getChildCast(1);

		// one to one owning relation
		assertFieldNonOwningOneToMany(experience, 2, "field");
		assertFieldNonUnidirectional(experience, 2);
		assertFieldNonOwningOneToOne(experience, 3, "field2");
		assertFieldNonUnidirectional(experience, 3);
		assertFieldOwningOneToOne(experience, 4, "field3");
		assertFieldUnidirectional(experience, 4);


		EntityResource customer = modelFolder.getChildCast(0);
		assertFieldOwningManyToOne(customer, 2, "experience1");
		assertFieldNonUnidirectional(customer, 2);
		assertFieldOwningOneToOne(customer, 3, "experience2");
		assertFieldNonUnidirectional(customer, 3);
		assertFieldOwningManyToOne(customer, 4, "experience3");
		assertFieldUnidirectional(customer, 4);

	}


	@Test
	public void entityField_MultipleXToManyRelationTypes() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<Experience> -> field,"+
																	"			experience2 Set<Experience> -> field2,"+	
																	"			experience3 Experience -> *,"+
																	"			experience4 Set<Experience> -> *"+			
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Set<Customer>," +
																	"			field2    Customer," +
																	"			field3    Set<Customer>"+
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);


		ProjectRoot root = generator.getProjectRoot();
		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource experience = modelFolder.getChildCast(1);

		// one to one owning relation
		assertFieldNonOwningManyToMany(experience, 2, "field");
		assertFieldOwningManyToOne(experience, 3, "field2");
		assertFieldNonUnidirectional(experience, 3);
		// the only case in which a one-to-many is the owning side is when it is unidirectional
		assertFieldOwningOneToMany(experience, 4, "field3");
		assertFieldUnidirectional(experience, 4);


		EntityResource customer = modelFolder.getChildCast(0);
		assertFieldOwningManyToMany(customer, 2, "experience1");
		assertFieldNonUnidirectional(experience, 2);
		assertFieldNonOwningOneToMany(customer, 3, "experience2");
		assertFieldNonUnidirectional(experience, 3);
		assertFieldOwningManyToOne(customer, 4, "experience3");
		assertFieldUnidirectional(customer, 4);
		assertFieldOwningManyToMany(customer, 5, "experience4");
		assertFieldUnidirectional(customer, 5);

	}


	@Test(expected = RelatedFieldNotFoundException.class)
	public void entityFieldOneToOne_inexistingRelation() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Experience -> inexistingField" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = RelatedFieldNotFoundException.class)
	public void entityFieldOneToOne_inexistingRelation_ManyToMany() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<Experience> -> inexistingField" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = RelatedEntityNotFoundException.class)
	public void entityFieldOneToOne_inexistingRelationEntity() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 ExperienceXXX -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);


	}


	@Test(expected = RelatedEntityNotFoundException.class)
	public void entityFieldOneToOne_inexistingRelationEntity_ManyToMany() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<ExperienceXXX> -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);

	}


	@Test(expected = WrongRelatedFieldTypeException.class)
	public void entityFieldOneToOne_wrongRelationFieldType() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<Experience> -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	// Customer.experience1 references field , but field is not of type Customer
																	"			field 	  NotACustomerType" +
																	"		}" +
																	"		NotACustomerType {" +
																	"			field int" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = DuplicateDeclaredRelation.class)
	public void entityFieldOneToOne_duplicateMapping() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Experience -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer -> experience1" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = DuplicateDeclaredRelation.class)
	public void entityFieldOneToOne_duplicateMapping_2() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Experience -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Customer -> *" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = DuplicateDeclaredRelation.class)
	public void entityFieldOneToOne_duplicateMapping_3() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<Experience> -> field" +
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			field 	  Set<Customer> -> *" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
	}


	@Test(expected = DuplicateDeclaredRelation.class)
	public void entityFieldOneToOne_duplicateManyToMany() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
														" module CV {" +
																	"  ui     {} " +
																	"  logic  {}" +
																	"  domain {" +
																	"		Customer {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			experience1 Set<Experience> -> cus1"+
																	"		}"+
																	"	    Experience {"+
																	"			startDate Date,"+
																	"			endDate   Date,"+
																	"			cus1 	  Set<Customer> -> experience1" +
																	"		} " +
																	"  }" +
														"}"));
		//@formatter:on
		setUpResourceTreeAndEnhance(swl);
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
		setUpResourceTreeAndEnhance(swl);
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
		setUpResourceTreeAndEnhance(swl);
	}



	private void assertFieldOwningOneToOne(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isOneToOne());
		assertTrue(field.isOwningInRelation());

	}


	private void assertFieldNonOwningOneToOne(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isOneToOne());
		assertFalse(field.isOwningInRelation());
	}


	private void assertFieldNonOwningManyToMany(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isManyToMany());
		assertFalse(field.isOwningInRelation());
	}


	private void assertFieldOwningManyToMany(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isManyToMany());
		assertTrue(field.isOwningInRelation());
	}


	private void assertFieldUnidirectional(EntityResource entity, int i) {
		assertTrue(entity.getField(i).isUnidirectional());
	}


	private void assertFieldNonUnidirectional(EntityResource entity, int i) {
		assertFalse(entity.getField(i).isUnidirectional());
	}



	private void assertFieldOwningManyToOne(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isManyToOne());
		assertTrue(field.isOwningInRelation());
	}


	private void assertFieldOwningOneToMany(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isOneToMany());
		assertTrue(field.isOwningInRelation());
	}


	private void assertFieldNonOwningOneToMany(EntityResource entity, int i, String string) {
		EntityField field = entity.getField(i);
		assertEquals(string, field.getName());
		assertTrue(field.isOneToMany());
		assertFalse(field.isOwningInRelation());
	}

}
