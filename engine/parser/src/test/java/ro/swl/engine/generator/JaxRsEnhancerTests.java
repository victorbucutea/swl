package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ro.swl.engine.generator.GenerationContext.PACKAGE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.javaee.enhancer.JaxRSTechnology;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.EntityType;
import ro.swl.engine.generator.model.Annotation;
import ro.swl.engine.generator.model.Field;
import ro.swl.engine.generator.model.Method;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class JaxRsEnhancerTests extends GeneratorTest {


	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> techs = new ArrayList<Technology>();
		techs.add(new InternalEnhancers(ctxt));
		techs.add(new JaxRSTechnology(ctxt));
		return techs;
	}


	@Test
	public void jaxRs_OneToOne_Unidirectional() throws GenerateException, ParseException {
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
															"			experience2 Experience"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer1 Customer"+
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
		assertNoAnnotationsPresent(experience1);

		EntityField experience2 = customer.getField(3);
		assertNoAnnotationsPresent(experience2);


		EntityField customer1 = experience.getField(2);
		assertNoAnnotationsPresent(customer1);

	}


	@Test
	public void jaxRs_OneToMany() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience -> customer1,"+
															"			experience2 Experience "+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer1 Set<Customer>,"+
															"			customer2 Set<Customer> -> experience2"+
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
		assertJaxRsOwningAnotation(experience1);

		EntityField experience2 = customer.getField(3);
		assertJaxRsOwningAnotation(experience2);


		Field<EntityType> customer1 = experience.getField(2);
		// one-to-many should have json ignore and a serializer method
		assertJsonIgnoreAndSerializerMethod(experience, customer1);
		//assertJaxRsNonOwningAnotation(customer1);

		Field<EntityType> customer2 = experience.getField(3);
		assertJsonIgnoreAndSerializerMethod(experience, customer2);
		//assertJaxRsNonOwningAnotation(customer1);

	}


	@Test
	public void jaxRs_OneToOne_Bidir() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience1 Experience -> customer1,"+
															"			experience2 Experience"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer1 Customer,"+
															"			customer2 Customer -> experience2"+
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
		assertJaxRsOwningAnotation(experience1);

		Field<EntityType> experience2 = customer.getField(3);
		assertJaxRsNonOwningAnotation(experience2);


		EntityField customer1 = experience.getField(2);
		assertJaxRsNonOwningAnotation(customer1);

		EntityField customer2 = experience.getField(3);
		assertJaxRsOwningAnotation(customer2);
	}


	@Test
	public void jaxRs_ManyToMany_Unidir() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience2 Set<Experience>"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer3 Set<Customer> -> *"+
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
		assertJsonIgnoreAndSerializerMethod(customer, experience1);

		EntityField customer1 = experience.getField(2);
		assertJsonIgnoreAndSerializerMethod(experience, customer1);

	}


	@Test(expected = GenerateException.class)
	public void jaxRs_ManyToMany_Bidir() throws GenerateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {}" +
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience2 Set<Experience>"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer3 Set<Customer> -> experience2"+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		ctxt.setProperty(PACKAGE, "ro.sft.somepackage");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.generate(appModel, new File(testTemplateDir, "module-and-entity"));
		generator.enhance(appModel);

	}


	private void assertNoAnnotationsPresent(Field<EntityType> field) {
		assertEquals(0, field.getAnnotations().size());
	}


	private void assertJaxRsNonOwningAnotation(Field<EntityType> field) {
		assertEquals(1, field.getAnnotations().size());
		Annotation managedRef = field.getAnnotations().get(0);
		assertEquals("JsonManagedReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonManagedReference", managedRef.getFqName());
	}


	private void assertJsonIgnoreAndSerializerMethod(EntityResource resource, Field<EntityType> field) {
		assertEquals(1, field.getAnnotations().size());

		Annotation managedRef = field.getAnnotations().get(0);
		assertEquals("org.codehaus.jackson.annotate.JsonIgnore", managedRef.getFqName());


		Method serializerMethod = resource.getMethod("serialize" + field.getUpperCamelName());
		assertNotNull(serializerMethod);

		List<Annotation> mAnnotations = serializerMethod.getAnnotations();
		assertEquals(2, mAnnotations.size());
		Annotation jsonProp = mAnnotations.get(0);
		assertEquals("org.codehaus.jackson.annotate.JsonProperty", jsonProp.getFqName());

		assertEquals("org.codehaus.jackson.annotate.JsonManagedReference", mAnnotations.get(1).getFqName());
	}


	private void assertJaxRsOwningAnotation(EntityField field) {
		assertEquals(1, field.getAnnotations().size());
		Annotation managedRef = field.getAnnotations().get(0);
		assertEquals("JsonBackReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonBackReference", managedRef.getFqName());
	}



}
