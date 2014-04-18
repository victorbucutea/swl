package ro.swl.engine.generator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.javaee.enhancer.JaxRSTechnology;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;


public class JaxRsEnhancerTests extends GeneratorTest {


	@Override
	public List<Technology> getTechsUnderTest() {
		List<Technology> techs = new ArrayList<Technology>();
		techs.add(new InternalEnhancers());
		techs.add(new JaxRSTechnology());
		return techs;
	}


	@Test
	public void jaxRs_OneToOne_Unidirectional() throws CreateException, ParseException {
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
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);
		EntityResource experience = modelFolder.getChildCast(1);


		EntityField experience1 = customer.getField(2);
		assertNoAnnotationsPresent(experience1);

		EntityField experience2 = customer.getField(3);
		assertNoAnnotationsPresent(experience2);


		EntityField customer1 = experience.getField(2);
		assertNoAnnotationsPresent(customer1);

	}


	@Test
	public void jaxRs_OneToMany() throws CreateException, ParseException {
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
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);
		EntityResource experience = modelFolder.getChildCast(1);


		EntityField experience1 = customer.getField(2);
		assertJaxRsOwningAnotationWithoutSerializerMethod(customer, experience1);

		EntityField experience2 = customer.getField(3);
		assertJaxRsOwningAnotationWithoutSerializerMethod(customer, experience2);


		EntityField customer1 = experience.getField(2);
		// one-to-many should have json ignore and a serializer method
		assertJsonIgnoreAndSerializerMethod(experience, customer1);
		//assertJaxRsNonOwningAnotation(customer1);

		EntityField customer2 = experience.getField(3);
		assertJsonIgnoreAndSerializerMethod(experience, customer2);
		//assertJaxRsNonOwningAnotation(customer1);

	}


	@Test
	public void jaxRs_OneToOne_Bidir() throws CreateException, ParseException {
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
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);
		EntityResource experience = modelFolder.getChildCast(1);

		EntityField experience1 = customer.getField(2);
		assertJaxRsOwningAnotationWithoutSerializerMethod(customer, experience1);

		EntityField experience2 = customer.getField(3);
		assertJsonIgnoreAndSerializerMethod(customer, experience2);


		EntityField customer1 = experience.getField(2);
		assertJsonIgnoreAndSerializerMethod(experience, customer1);

		EntityField customer2 = experience.getField(3);
		assertJaxRsOwningAnotationWithoutSerializerMethod(experience, customer2);
	}


	@Test
	public void jaxRs_ManyToMany_Unidir() throws CreateException, ParseException {
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
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);
		ProjectRoot root = generator.getProjectRoot();

		Resource modelFolder = root.getChild(0).getChild(0).getChild(0);
		EntityResource customer = modelFolder.getChildCast(0);
		EntityResource experience = modelFolder.getChildCast(1);


		EntityField experience1 = customer.getField(2);
		assertJsonIgnoreAndSerializerMethod(customer, experience1);

		EntityField customer1 = experience.getField(2);
		assertJsonIgnoreAndSerializerMethod(experience, customer1);

	}


	@Test(expected = CreateException.class)
	public void jaxRs_ManyToMany_Bidir() throws CreateException, ParseException {
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
		skeleton.setSkeletonName("module-and-entity");
		ASTSwdlApp appModel = swl.SwdlApp();
		generator.create(appModel);
		generator.enhance(appModel);

	}


	private void assertNoAnnotationsPresent(EntityField field) {
		assertEquals(0, field.getAnnotations().size());
	}


	private void assertJaxRsNonOwningAnotation(EntityField field) {
		assertEquals(1, field.getAnnotations().size());
		Annotation managedRef = field.getAnnotations().get(0);
		assertEquals("JsonManagedReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonManagedReference", managedRef.getFqName());
	}


	private void assertJsonIgnoreAndSerializerMethod(EntityResource resource, EntityField field) {
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


	private void assertJaxRsOwningAnotationWithoutSerializerMethod(EntityResource resource, EntityField field) {
		assertEquals(1, field.getAnnotations().size());
		Annotation managedRef = field.getAnnotations().get(0);
		assertEquals("JsonBackReference", managedRef.getSimpleName());
		assertEquals("org.codehaus.jackson.annotate.JsonBackReference", managedRef.getFqName());

		Method serializerMethod = resource.getMethod("serialize" + field.getUpperCamelName());
		assertNull(serializerMethod);
	}



}
