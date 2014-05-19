package ro.swl.engine.generator;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import ro.swl.engine.GeneratorTest;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Method.Parameter;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.javaee.enhancer.JaxRsTechnology;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.generator.javaee.model.ExternalInterfaceResource;
import ro.swl.engine.generator.model.ProjectRoot;
import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class JaxRsEnhancerTests extends GeneratorTest {


    @Override
    public List<Technology> getTechsUnderTest() {
        List<Technology> techs = new ArrayList<Technology>();
        techs.add(new InternalEnhancers());
        techs.add(new JaxRsTechnology());
        return techs;
    }


    @Test
    public void jaxRs_OneToMany() throws CreateException, ParseException {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {}" +
                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience1 Experience -> customer1," +
                "			experience2 Experience " +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer1 Set<Customer>," +
                "			customer2 Set<Customer> -> experience2" +
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
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience1 Experience -> customer1," +
                "			experience2 Experience" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer1 Customer," +
                "			customer2 Customer -> experience2" +
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
    public void jaxRs_OneToOne_Unidirectional() throws CreateException, ParseException {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {}" +
                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience1 Experience," +
                "			experience2 Experience" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer1 Customer" +
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
    public void jaxRs_ManyToMany_Unidir() throws CreateException, ParseException {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {}" +
                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience2 Set<Experience>" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer3 Set<Customer> -> *" +
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
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience2 Set<Experience>" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer3 Set<Customer> -> experience2" +
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
    public void jaxRs_noServiceActions() throws Exception {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {" +
                "		external rest interface CVRest {" +
                "			service ServiceWithNoMethods {} " +

                "			Set<Experience> someAction(ExternalCustomer someCust, Long someId) {}" +

                "			Set<CV> getAllCvWithSomeProperty(String someProp) {}" +
                "       }" +
                "       service ServiceWithNoMethods{} " +
                "  }" +
                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience2 Set<Experience>" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer3 Set<Customer> " +
                "		} " +
                "  }" +
                "}"));

        skeleton.setSkeletonName("module-entity-and-interface");
        ASTSwdlApp appModel = swl.SwdlApp();
        appModel.dump("");
        generator.create(appModel);
        generator.enhance(appModel);

        ProjectRoot root = generator.getProjectRoot();

        Resource externalFolder = root.getChild(0).getChild(0).getChild(0);
        ExternalInterfaceResource restService = externalFolder.getChildCast(0);
        assertExternalInterfaceOperationsCreated(restService);
    }


    @Test
    public void jaxRs_noExternalInterfaceActions_and_noServiceActions() throws Exception {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {" +
                "		external rest interface CVRest {" +
                "			service ServiceWithMethodsAndCrud {}" +
                "       }" +
                "       service ServiceWithMethodsAndCrud {}" +
                "  }" +
                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience2 Set<Experience>" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer3 Set<Customer> " +
                "		} " +
                "  }" +
                "}"));

        skeleton.setSkeletonName("module-entity-and-interface");
        ASTSwdlApp appModel = swl.SwdlApp();
        generator.create(appModel);
        generator.enhance(appModel);

        ProjectRoot root = generator.getProjectRoot();

        Resource externalFolder = root.getChild(0).getChild(0).getChild(0);
        ExternalInterfaceResource restService = externalFolder.getChildCast(0);

        assertEquals(0, restService.getMethods().size());
        List<Annotation> annotations = restService.getAnnotations();
        Annotation path = annotations.get(0);
        assertEquals("@Path(\"/cvrest\")", path.toJavaRepresentation());
        Annotation application = annotations.get(1);
        assertEquals("@ApplicationPath(\"/rest\")", application.toJavaRepresentation());
        assertEquals("CVRest", restService.getName());

        assertServiceInjected(restService);

    }

    private void assertServiceInjected(ExternalInterfaceResource restService) {
        Field field = restService.getFields().get(0);
        assertEquals("serviceWithMethodsAndCrud", field.getName());
        assertEquals("ServiceWithMethodsAndCrud", field.getType().toJavaRepresentation());
    }

    @Test
    public void jaxRs_generateServiceActions() throws CreateException, ParseException, WriteException {
        //@formatter:off
        SWL swl = new SWL(createInputStream(" name  'module' \n\t\n" +
                " module CV {" +
                "  ui     {} " +
                "  logic  {" +
                "		external rest interface CVRest {" +
                "			exchange model { // define interchange model \n" +
                "				ExternalCustomer {" +
                "					startDate Date," +
                "					endDate   Date," +
                "					experience2 Set<Experience>" +
                "				}" +
                "			}" +
                // expose all operations in SomeOtherService
                "			service SomeOtherService {}  " +

                "			Set<Experience> someAction(ExternalCustomer someCust, Long someId) {}" +

                "			Set<CV> getAllCvWithSomeProperty(String someProp) {}" +
                "}" +

                "       service SomeOtherService {" +
                "	        crud Customer {}" +

                "           Set<Customer> getCustomers(String someId) {}" +
                "           void aVoidMethod() {}" +
                "           void aVoidMethodWithArguments(Customer argument) {}" +
                "       }" +
                "  }" +

                "  domain {" +
                "		Customer {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			experience2 Set<Experience>" +
                "		}" +
                "	    Experience {" +
                "			startDate Date," +
                "			endDate   Date," +
                "			customer3 Set<Customer> " +
                "		} " +
                "  }" +
                "}"));
        //@formatter:on
        skeleton.setSkeletonName("module-entity-and-interface");
        ASTSwdlApp appModel = swl.SwdlApp();
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

        assertExternalInterfaceOperationsCreated(restService);
        assertCvServiceOperationsCreated(restService);
        assertImportsCreated(restService);
    }

    private void assertImportsCreated(ExternalInterfaceResource restService) {
        List<String> imports = Arrays.asList("java.util.List", "javax.ejb.EJB", "javax.ws.rs.ApplicationPath", "javax.ws.rs.Consumes",
                "javax.ws.rs.GET","javax.ws.rs.POST", "javax.ws.rs.Path", "javax.ws.rs.PathParam", "javax.ws.rs.Produces",
                "javax.ws.rs.core.Application","javax.ws.rs.core.Context","javax.ws.rs.core.MediaType", "javax.ws.rs.core.UriInfo",
                "ro.sft.recruiter.base.dao.UriSearchInfo","ro.sft.recruiter.model.Customer","ro.sft.recruiter.SomeOtherService");

        Set<String> actualImports = restService.getImports();

        System.out.println(CollectionUtils.subtract(actualImports, imports));
        System.out.println(CollectionUtils.subtract(imports, actualImports));
        assertEquals(imports.size(), actualImports.size());
        assertTrue(actualImports.containsAll(imports));

    }


    private void assertExternalInterfaceOperationsCreated(ExternalInterfaceResource restService) {
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
        assertServiceActionsCreated(customer);
        assertCrudActionDelegationsCreated(customer);
    }

    private void assertServiceActionsCreated(ExternalInterfaceResource customer) {
        List<Method> methods = customer.getMethods();
        Method getCustomers = customer.getMethod("getCustomers");

        // Set<CV> getAllCvWithSomeProperty(String someProp) {}
        assertNotNull(getCustomers);
        Annotation method = getCustomers.getAnnotations().get(0);
        assertEquals("@POST", method.toJavaRepresentation());
        Annotation path1 = getCustomers.getAnnotations().get(1);
        assertEquals("@Path(\"/getcustomers\")", path1.toJavaRepresentation());
        Annotation consumes1 = getCustomers.getAnnotations().get(2);
        assertEquals("@Consumes(MediaType.APPLICATION_JSON)", consumes1.toJavaRepresentation());
        Annotation produces1 = getCustomers.getAnnotations().get(3);
        assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces1.toJavaRepresentation());


        //void aVoidMethod() {}
        Method aVoidMethod = customer.getMethod("aVoidMethod");
        assertNotNull(aVoidMethod);
        Annotation method2 = aVoidMethod.getAnnotations().get(0);
        assertEquals("@GET", method2.toJavaRepresentation());
        Annotation path2 = aVoidMethod.getAnnotations().get(1);
        assertEquals("@Path(\"/avoidmethod\")", path2.toJavaRepresentation());
        // just the two annotations should be present
        assertEquals(2, aVoidMethod.getAnnotations().size());

        //void aVoidMethodWithArguments(Customer argument) {}
        Method aVoidMethodWithArguments = customer.getMethod("aVoidMethodWithArguments");
        assertNotNull(aVoidMethodWithArguments);
        Annotation method3 = aVoidMethodWithArguments.getAnnotations().get(0);
        assertEquals("@POST", method3.toJavaRepresentation());
        Annotation path3 = aVoidMethodWithArguments.getAnnotations().get(1);
        assertEquals("@Path(\"/avoidmethodwitharguments\")", path3.toJavaRepresentation());
        Annotation consumes3 = aVoidMethodWithArguments.getAnnotations().get(2);
        assertEquals("@Consumes(MediaType.APPLICATION_JSON)", consumes3.toJavaRepresentation());
        // just the three annotations should be present
        assertEquals(3, aVoidMethodWithArguments.getAnnotations().size());
    }

    private void assertCrudActionDelegationsCreated(ExternalInterfaceResource customer) {
        assertGetAllMethodCreated(customer);
        assertFindMethodCreated(customer);
        assertSaveMethodCreated(customer);
        assertSearchMethodCreated(customer);
    }

    private void assertSearchMethodCreated(ExternalInterfaceResource customer) {
//        @GET
//        @Path("search_{searcherId}")
//        @Produces(MediaType.APPLICATION_JSON)
//        public List<CV> search(@PathParam("searcherId") String searcherId, @Context UriInfo uriInfo) {
//            return cvService.search(new UriSearchInfo(searcherId, uriInfo));
//        }
        Method search = customer.getMethod("search");
        assertNotNull(search);
        Annotation method3 = search.getAnnotations().get(0);
        assertEquals("@GET", method3.toJavaRepresentation());
        Annotation path3 = search.getAnnotations().get(1);
        assertEquals("@Path(\"search_{searcherId}\")", path3.toJavaRepresentation());
        Annotation produces1 = search.getAnnotations().get(2);
        assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces1.toJavaRepresentation());

        Parameter parameter = search.getParameters().get(0);
        assertEquals("@PathParam(\"searcherId\")", parameter.getAnnotations().get(0).toJavaRepresentation());
        assertEquals("String", parameter.getType().toJavaRepresentation());
        assertEquals("searcherId", parameter.getName());

        Parameter uriInfo = search.getParameters().get(1);
        assertEquals("@Context", uriInfo.getAnnotations().get(0).toJavaRepresentation());
        assertEquals("UriInfo", uriInfo.getType().toJavaRepresentation());
        assertEquals("uriInfo", uriInfo.getName());

        Statement statement = search.getBody().get(0);
        assertEquals("return someOtherService.search(new UriSearchInfo(searcherId, uriInfo));", statement.toJavaRepresentation());

    }

    private void assertSaveMethodCreated(ExternalInterfaceResource customer) {
//        @POST
//        @Consumes(MediaType.APPLICATION_JSON)
//        @Produces(MediaType.APPLICATION_JSON)
//        public CV saveCv(CV cv) {
//            return cvService.saveCV(cv);
//        }
        Method saveCv = customer.getMethod("saveCustomer");
        assertNotNull(saveCv);
        Annotation method3 = saveCv.getAnnotations().get(0);
        assertEquals("@POST", method3.toJavaRepresentation());

        Annotation produces1 = saveCv.getAnnotations().get(1);
        assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces1.toJavaRepresentation());
        Annotation consumes1 = saveCv.getAnnotations().get(2);
        assertEquals("@Consumes(MediaType.APPLICATION_JSON)", consumes1.toJavaRepresentation());

        assertEquals("Customer", saveCv.getReturnType().toJavaRepresentation());
        assertEquals("Customer", saveCv.getParameters().get(0).getType().toJavaRepresentation());

        Statement returnStmt = saveCv.getBody().get(0);
        assertEquals("return someOtherService.saveCustomer(customer);", returnStmt.toJavaRepresentation());
    }

    private void assertFindMethodCreated(ExternalInterfaceResource customer) {

//        @GET
//        @Path("{id}")
//        @Produces(MediaType.APPLICATION_JSON)
//        public CV find(@PathParam("id") Long id) {
//            return cvService.find(id);
//        }

        Method find = customer.getMethod("find");
        assertNotNull(find);
        Annotation method = find.getAnnotations().get(0);
        assertEquals("@GET", method.toJavaRepresentation());

        Annotation path = find.getAnnotations().get(1);
        assertEquals("@Path(\"{id}\")", path.toJavaRepresentation());

        Annotation produces1 = find.getAnnotations().get(2);
        assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces1.toJavaRepresentation());

        assertEquals("Customer", find.getReturnType().toString());
        Statement returnStmt = find.getBody().get(0);
        assertEquals("return someOtherService.find(id);", returnStmt.toJavaRepresentation());

        Parameter parameter = find.getParameters().get(0);
        assertEquals("@PathParam(\"id\")", parameter.getAnnotations().get(0).toJavaRepresentation());
        assertEquals(1, parameter.getAnnotations().size());
        assertEquals("id", parameter.getName());
        assertEquals(3, find.getAnnotations().size());
    }

    private void assertGetAllMethodCreated(ExternalInterfaceResource customer) {
        // getAll method
        //@GET
        // @Produces(MediaType.APPLICATION_JSON)
        // public List<CV> getAll() {
        //      return cvService.getAll();
        // }
        Method getAll = customer.getMethod("getAll");
        assertNotNull(getAll);
        Annotation method = getAll.getAnnotations().get(0);
        assertEquals("@GET", method.toJavaRepresentation());
        Annotation produces1 = getAll.getAnnotations().get(1);
        assertEquals("@Produces(MediaType.APPLICATION_JSON)", produces1.toJavaRepresentation());
        assertEquals(2, getAll.getAnnotations().size());

        assertEquals("List<Customer>", getAll.getReturnType().toString());
        Statement returnStmt = getAll.getBody().get(0);
        assertEquals("return someOtherService.getAll();", returnStmt.toJavaRepresentation());
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
