package ro.swl.engine.generator.javaee.enhancer;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.util.List;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.javaee.exception.InvalidServiceReferenceException;
import ro.swl.engine.generator.javaee.model.ExternalInterfaceResource;
import ro.swl.engine.parser.ASTAction;
import ro.swl.engine.parser.ASTActionParam;
import ro.swl.engine.parser.ASTExternalInterface;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ASTServiceActions;
import ro.swl.engine.parser.ASTSwdlApp;


public class JaxRsFacadeEnhancer extends Enhancer<ExternalInterfaceResource> {

    @Override
    public void enhance(ASTSwdlApp appModel, ExternalInterfaceResource r) throws CreateException {
        addJaxRsPathAnnotation(r);
        addServiceActionsDelegators(appModel, r);
        addCustomActions(r);
    }


    private void addCustomActions(ExternalInterfaceResource r) throws CreateException {
        ASTExternalInterface extIf = r.getModel();

        for (ASTAction action : extIf.getCustomActions()) {
            addActionMethod(null, r, action);
        }
    }


    private void addServiceActionsDelegators(ASTSwdlApp appModel, ExternalInterfaceResource r) throws CreateException {
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

        Field f = new Field(serviceName, serviceFqName, "");
        f.setHasGetter(false);
        f.setHasGetter(false);

        // TODO create a 'Technology connector' of some kind
        // e.g. when both JaxRs & EJB are present, the fields need to be injected via an @EJB, after EJB enhancer ran.
        // e.g. when both JaxRs & Spring are present, fields need to be injected via an Spring annotation

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
            // @GET
            // @Path("search_{searcherId}")
            Annotation post = new Annotation("javax.ws.rs.POST");
            Annotation path = new Annotation("javax.ws.rs.Path");
            path.addPropertyLiteral("value", "/" + action.getImage().toLowerCase());
            Annotation consumes = new Annotation("javax.ws.rs.Consumes");
            consumes.addProperty("value", "MediaType.APPLICATION_JSON");
            m.addAnnotation(post);
            m.addAnnotation(path);
            m.addAnnotation(consumes);
        }

        if (!action.isVoidReturn()) {
            Annotation produces = new Annotation("javax.ws.rs.Produces");
            produces.addProperty("value", "MediaType.APPLICATION_JSON");
            m.addAnnotation(produces);
        }

    }


    private void addDelegationCall(ASTService service, ASTAction action, Method m) {
        List<Statement> body = m.getBody();
        StringBuilder params = new StringBuilder("(");

        for (ASTActionParam param : action.getActionParams()) {
            params.append(param.getTypeAsString());
            params.append(" ");
            params.append(param.getName());
            params.append(",");
        }
        params.deleteCharAt(params.length());

        if (action.isVoidReturn()) {
            body.add(new Statement(service.getImage() + "." + action.getImage() + params + ")"));
        } else {
            body.add(new Statement("return " + service.getImage() + "." + action.getImage() + params + "()"));
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

    }
}
