package ro.swl.engine.generator.javaee.enhancer;

import com.google.common.base.CaseFormat;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.java.model.*;
import ro.swl.engine.generator.javaee.exception.InvalidServiceReferenceException;
import ro.swl.engine.generator.javaee.model.ExternalInterfaceResource;
import ro.swl.engine.parser.*;

import java.util.List;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;


public class JaxRsFacadeEnhancer extends Enhancer<ExternalInterfaceResource> {

    @Override
    public void enhance(ASTSwdlApp appModel, ExternalInterfaceResource r) throws CreateException {
        addJaxRsPathAnnotation(r);
        addServiceCustomActions(appModel, r);
        addServiceCrudActions(appModel, r);
        addExternalInterfaceCustomActions(r);

    }

    private void addServiceCrudActions(ASTSwdlApp appModel, ExternalInterfaceResource r) throws CreateException {
        ASTExternalInterface extIf = r.getModel();

        for (ASTServiceActions serviceActions : extIf.getChildServiceActions()) {
            String referencedName = serviceActions.getReferencedServiceName();
            ASTService service = getReferencedService(r.getModuleName(), referencedName, appModel);

            if (service == null) {
                throw new InvalidServiceReferenceException(r.getName(), referencedName);
            }

            addCrudActionsDelegators(service, r);
        }
    }

    private void addCrudActionsDelegators(ASTService service, ExternalInterfaceResource r) throws CreateException {
        for (ASTCrud crud : service.getCruds()) {
            addGetAllMethod(r, service, crud);
            addFindMethod(r, service, crud);
            addSearchMethod(r, service,crud);
            addSaveMethod(r, service, crud);
        }

    }

    private void addSaveMethod(ExternalInterfaceResource r, ASTService service, ASTCrud crud) throws CreateException {
//        @POST
//        @Consumes(MediaType.APPLICATION_JSON)
//        @Produces(MediaType.APPLICATION_JSON)
//        public CV saveCv(CV cv) {
//            return cvService.saveCV(cv);
//        }

        String entity = crud.getEntity();

        Method m = new Method("save"+ entity);
        m.addAnnotation("javax.ws.rs.POST");
        Annotation ann = new Annotation("javax.ws.rs.Produces");
        ann.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
        m.addAnnotation(ann);

        Annotation consumes = new Annotation("javax.ws.rs.Consumes");
        consumes.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
        m.addAnnotation(consumes);

        m.setReturnType(new Type(entity));

        Method.Parameter param = new Method.Parameter(entity.toLowerCase(),entity);
        m.addParameter(param);

        String serviceName = service.getImage();
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
        Statement stmt = new Statement("return " + serviceName + ".save"+entity+"("+entity.toLowerCase()+")");
        m.getBody().add(stmt);
        r.addMethod(m);
    }

    private void addSearchMethod(ExternalInterfaceResource r, ASTService service, ASTCrud crud) throws CreateException {
//        @GET
//        @Path("search_{searcherId}")
//        @Produces(MediaType.APPLICATION_JSON)
//        public List<CV> search(@PathParam("searcherId") String searcherId, @Context UriInfo uriInfo) {
//            return cvService.search(new UriSearchInfo(searcherId, uriInfo));
//        }
        Method m = new Method("search");
        m.addAnnotation("javax.ws.rs.GET");
        Annotation path = new Annotation("javax.ws.rs.Path");
        path.addPropertyLiteral("value", "search_{searcherId}");
        m.addAnnotation(path);

        Annotation ann = new Annotation("javax.ws.rs.Produces");
        ann.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
        m.addAnnotation(ann);

        m.setReturnType(new Type("java.util.List<" + crud.getEntity() + ">"));

        Method.Parameter param1 = new Method.Parameter("searcherId", "java.lang.String");
        Annotation pathParam = new Annotation("javax.ws.rs.PathParam");
        pathParam.addPropertyLiteral("value","searcherId");
        param1.addAnnotation(pathParam);
        m.addParameter(param1);

        Method.Parameter uriInfo = new Method.Parameter("uriInfo", "javax.ws.rs.core.UriInfo");
        Annotation context = new Annotation("javax.ws.rs.core.Context");
        uriInfo.addAnnotation(context);
        m.addParameter(uriInfo);


        String serviceName = service.getImage();
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
        String defaultPackage = getGlobalCtxt().getDefaultPackage();

        r.addImport(defaultPackage + ".base.dao.UriSearchInfo");
        Statement stmt = new Statement("return "+serviceName+ ".search(new UriSearchInfo(searcherId, uriInfo))");
        m.getBody().add(stmt);
        r.addMethod(m);
    }

    private void addFindMethod(ExternalInterfaceResource r, ASTService service, ASTCrud crud) throws CreateException {
        Method m = new Method("find");
        m.addAnnotation("javax.ws.rs.GET");
        Annotation path = new Annotation("javax.ws.rs.Path");
        path.addPropertyLiteral("value", "{id}");
        m.addAnnotation(path);

        Annotation ann = new Annotation("javax.ws.rs.Produces");
        ann.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
        m.addAnnotation(ann);

        m.setReturnType(new Type(crud.getEntity()));

        Method.Parameter param1 = new Method.Parameter("id", "java.lang.String");
        Annotation pathParam = new Annotation("javax.ws.rs.PathParam");
        pathParam.addPropertyLiteral("value","id");
        param1.addAnnotation(pathParam);
        m.addParameter(param1);

        String serviceName = service.getImage();
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
        Statement stmt = new Statement("return " + serviceName + ".find(id)");
        m.getBody().add(stmt);
        r.addMethod(m);
    }

    private void addGetAllMethod(ExternalInterfaceResource r, ASTService service, ASTCrud crud) throws CreateException {
        Method m = new Method("getAll");
        m.addAnnotation("javax.ws.rs.GET");

        Annotation ann = new Annotation("javax.ws.rs.Produces");
        ann.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
        m.addAnnotation(ann);

        m.setReturnType(new Type("java.util.List<" + crud.getEntity() + ">"));

        String serviceName = service.getImage();
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);
        Statement stmt = new Statement("return " + serviceName + ".getAll()");
        m.getBody().add(stmt);
        r.addMethod(m);
    }


    private void addExternalInterfaceCustomActions(ExternalInterfaceResource r) throws CreateException {
        ASTExternalInterface extIf = r.getModel();

        for (ASTAction action : extIf.getCustomActions()) {
            addActionMethod(null, r, action);
        }
    }


    private void addServiceCustomActions(ASTSwdlApp appModel, ExternalInterfaceResource r) throws CreateException {
        ASTExternalInterface extIf = r.getModel();

        for (ASTServiceActions serviceActions : extIf.getChildServiceActions()) {
            String referencedName = serviceActions.getReferencedServiceName();
            ASTService service = getReferencedService(r.getModuleName(), referencedName, appModel);

            if (service == null) {
                throw new InvalidServiceReferenceException(r.getName(), referencedName);
            }

            injectService(service, r);
            addServiceDelegators(service, r);
        }

    }


    private void injectService(ASTService service, ExternalInterfaceResource r) throws CreateException {
        String serviceName = service.getImage();
        String serviceFqName = getGlobalCtxt().getFqNameForRegisteredType(serviceName);
        serviceFqName = serviceFqName != null ? serviceFqName : serviceName;
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);

        Field f = new Field(serviceName, serviceFqName, "");
        f.setHasSetter(false);
        f.setHasGetter(false);

        // TODO create a 'Technology connector' of some kind
        // e.g. when both JaxRs & EJB are present, the fields need to be injected via an @EJB, after EJB enhancer ran.
        // e.g. when both JaxRs & Spring are present, fields need to be injected via an Spring annotation
        // also the FQ name of the service needs to be registered in the global context.
        // The rest facade has no way of knowing where the __service__ is

        f.addAnotation("javax.ejb.EJB");
        r.addField(f);
    }


    private void addServiceDelegators(ASTService service, ExternalInterfaceResource r) throws CreateException {

        for (ASTAction action : service.getActions()) {
            addActionMethod(service, r, action);
        }

    }


    private void addActionMethod(ASTService service, ExternalInterfaceResource r, ASTAction action)
            throws CreateException {
        Method m = new Method(action.getImage());
        addJaxRsAnnotations(action, m);
        addReturnType(action, m);
        addParameters(action.getActionParams(), m);
        if (service != null)
            addDelegationCall(service, action, m);
        r.addMethod(m);
    }


    private void addJaxRsAnnotations(ASTAction action, Method m) throws CreateException {
        boolean hasParams = !action.getActionParams().isEmpty();
        // some small conventions have to be be in place
        if (action.isVoidReturn() && !hasParams) {
            // void methods with no params are invoked with a GET
            // @GET
            // @Path("search_{searcherId}")
            Annotation get = new Annotation("javax.ws.rs.GET");
            Annotation path = new Annotation("javax.ws.rs.Path");
            path.addPropertyLiteral("value", "/" + action.getImage().toLowerCase());
            m.addAnnotation(get);
            m.addAnnotation(path);

        }

        if (hasParams) {
            // methods with params are invoked with a POST
            Annotation post = new Annotation("javax.ws.rs.POST");
            Annotation path = new Annotation("javax.ws.rs.Path");
            path.addPropertyLiteral("value", "/" + action.getImage().toLowerCase());
            Annotation consumes = new Annotation("javax.ws.rs.Consumes");
            consumes.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
            m.addAnnotation(post);
            m.addAnnotation(path);
            m.addAnnotation(consumes);
        }

        if (!action.isVoidReturn()) {
            Annotation produces = new Annotation("javax.ws.rs.Produces");
            produces.addProperty("value", "javax.ws.rs.core.MediaType.APPLICATION_JSON");
            m.addAnnotation(produces);
        }

    }


    private void addDelegationCall(ASTService service, ASTAction action, Method m) {
        List<Statement> body = m.getBody();
        StringBuilder params = new StringBuilder("(");
        String serviceName = service.getImage();
        serviceName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceName);

        int noOfParams = action.getActionParams().size();
        if ( noOfParams > 0) {
            for (ASTActionParam param : action.getActionParams()) {
                params.append(" ");
                params.append(param.getName());
                params.append(",");
            }
            params.deleteCharAt(params.length() - 1);
        }

        if (action.isVoidReturn()) {
            body.add(new Statement(service.getImage() + "." + action.getImage() + params + ")"));
        } else {
            body.add(new Statement("return " + serviceName + "." + action.getImage() + params +")"));
        }
    }


    private void addReturnType(ASTAction action, Method m) throws CreateException {
        if (action.isVoidReturn()) {
            m.setReturnType(Type.VOID);
        } else {
            String returnTypeAsStr = action.getReturnTypeAsString();
            m.setReturnType(new Type(returnTypeAsStr));
        }
    }


    private void addParameters(List<ASTActionParam> actionParams, Method m) throws CreateException {
        for (ASTActionParam param : actionParams) {
            String fqParamName = getGlobalCtxt().getFqNameForRegisteredType(param.getTypeAsString());
            fqParamName = fqParamName != null ? fqParamName : param.getTypeAsString();
            Method.Parameter mParam = new Method.Parameter(param.getName(), new Type(fqParamName));
            m.addParameter(mParam);
        }
    }


    private ASTService getReferencedService(String moduleName, String name, ASTSwdlApp appModel) {
        ASTModule currentModule = getCurrentModule(moduleName, appModel);
        for (ASTService service : currentModule.getLogic().getServices()) {
            if (service.getImage().equals(name)) {
                return service;
            }
        }
        return null;
    }


    private ASTModule getCurrentModule(String moduleName, ASTSwdlApp appModel) {
        for (ASTModule module : appModel.getModules()) {
            if (module.getName().equals(moduleName)) {
                return module;
            }
        }
        return null;
    }


    private void addJaxRsPathAnnotation(ExternalInterfaceResource r) throws CreateException {
        String intfName = r.getLowerCaseName();
        Annotation path = new Annotation("javax.ws.rs.Path");
        path.addPropertyLiteral("value", "/" + intfName);
        r.addAnnotation(path);

        Annotation appPath = new Annotation("javax.ws.rs.ApplicationPath");
        appPath.addPropertyLiteral("value", "/rest");
        r.addAnnotation(appPath);

        r.setSuperClass("javax.ws.rs.core.Application");

    }
}
