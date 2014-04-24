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
import ro.swl.engine.generator.java.model.Method.Parameter;
import ro.swl.engine.generator.javaee.enhancer.JaxRsTechnology;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.ExternalInterfaceResource;
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
		techs.add(new JaxRsTechnology());
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
	// many-to-many bidir is not yet supported by jax rs serializer
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


	@Test
	public void jaxRsFacade() throws CreateException, ParseException {
		//@formatter:off
		SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
												" module CV {" +
															"  ui     {} " +
															"  logic  {" +
															"		external rest interface CVRest {"+
															"			exchange model { // define interchange model \n" +
															"				ExternalCustomer {"+
															"					startDate Date,"+
															"					endDate   Date," +
															"					experience2 Set<Experience>"+
															"				}" +
															"			}"+
															"			service SomeOtherService {}  // expose all operations in SomeOtherService \n"+
															
															"			Set<Experience> someAction(ExternalCustomer someCust, Long someId) {"+
															"			}" +

															"			Set<CV> getAllCvWithSomeProperty(String someProp) {"+
															"			}"+
																	"}" +

																	"service SomeOtherService {" +
																	"	crud Customer {}" +
																	"}" +
															"  }" +
																	
															"  domain {" +
															"		Customer {"+
															"			startDate Date,"+
															"			endDate   Date," +
															"			experience2 Set<Experience>"+
															"		}"+
															"	    Experience {"+
															"			startDate Date,"+
															"			endDate   Date,"+
															"			customer3 Set<Customer> "+
															"		} " +
															"  }" +
												"}"));
		//@formatter:on
		skeleton.setSkeletonName("module-entity-and-interface");
		ASTSwdlApp appModel = swl.SwdlApp();
		appModel.dump("");
		generator.create(appModel);
		generator.enhance(appModel);

		ProjectRoot root = generator.getProjectRoot();

		assertRestFacadeCreated(root);
	}


	private void assertRestFacadeCreated(ProjectRoot root) {
		Resource externalFolder = root.getChild(0).getChild(0).getChild(0);
		ExternalInterfaceResource restService = externalFolder.getChildCast(0);

		List<Annotation> annotations = restService.getAnnotations();
		Annotation path = annotations.get(0);
		assertEquals("@Path(\"/cvrest\")", path.toJavaRepresentation());
		Annotation application = annotations.get(1);
		assertEquals("@ApplicationPath(\"/rest\")", application.toJavaRepresentation());
		assertEquals("CVRest", restService.getName());

		assertCvServiceOperationsCreated(restService);
		assertAdditionalOperationsCreated(restService);

	}


	private void assertAdditionalOperationsCreated(ExternalInterfaceResource restService) {
		Method someAction = restService.getMethod("someAction");
		assertNotNull(someAction);
		assertEquals("Set<Experience>", someAction.getReturnType().toString());
		Parameter param1 = someAction.getParameters().get(0);
		assertEquals("ExternalCustomer", param1.getType().getSimpleClassName());
		Parameter param2 = someAction.getParameters().get(1);
		Annotation post = someAction.getAnnotations().get(0);
		assertEquals("@POST", post.toJavaRepresentation());
		Annotation path = someAction.getAnnotations().get(1);
		assertEquals("@Path(\"/someaction\")", path.toJavaRepresentation());
		Annotation consumes = someAction.getAnnotations().get(2);
		assertEquals("@Consumes(MediaType.APPLICATION_JSON)", consumes.toJavaRepresentation());
		Annotation produces = someAction.getAnnotations().get(3);
		assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces.toJavaRepresentation());


		assertEquals("Long", param2.getType().getSimpleClassName());
		Method getAllCv = restService.getMethod("getAllCvWithSomeProperty");
		assertNotNull(getAllCv);
		assertEquals("Set<CV>", getAllCv.getReturnType().toString());

		Annotation post1 = getAllCv.getAnnotations().get(0);
		assertEquals("@POST", post1.toJavaRepresentation());
		Annotation path1 = getAllCv.getAnnotations().get(1);
		assertEquals("@Path(\"/getallcvwithsomeproperty\")", path1.toJavaRepresentation());
		Annotation consumes1 = getAllCv.getAnnotations().get(2);
		assertEquals("@Consumes(MediaType.APPLICATION_JSON)", consumes1.toJavaRepresentation());
		Annotation produces2 = getAllCv.getAnnotations().get(3);
		assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces2.toJavaRepresentation());


	}


	private void assertCvServiceOperationsCreated(ExternalInterfaceResource customer) {


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
