package ro.swl.engine.generator.javaee.enhancer;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.util.List;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.java.model.Field;
import ro.swl.engine.generator.java.model.Method;
import ro.swl.engine.generator.java.model.Statement;
import ro.swl.engine.generator.java.model.Type;
import ro.swl.engine.generator.javaee.exception.CrudEntityNotFoundException;
import ro.swl.engine.generator.javaee.model.ServiceResource;
import ro.swl.engine.parser.ASTAction;
import ro.swl.engine.parser.ASTActionParam;
import ro.swl.engine.parser.ASTCrud;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ASTSwdlApp;

import com.google.common.base.CaseFormat;


public class ServiceEJBEnhancer extends Enhancer<ServiceResource> {

	@Override
	public void enhance(ASTSwdlApp appModel, ServiceResource r) throws CreateException {
		String currentModule = r.getModuleName();

		ASTModule module = appModel.findModule(currentModule);
		ASTService service = module.findService(r.getName());

		addEjbAnnotations(service, r);
		addServiceMethods(service, r);
		addCrudMethods(service, r);
	}


	private void addEjbAnnotations(ASTService service, ServiceResource r) throws CreateException {
		r.addAnnotation("javax.ejb.Stateless");
	}


	private void addCrudMethods(ASTService service, ServiceResource r) throws CreateException {

		List<ASTCrud> cruds = service.getChildNodesOfType(ASTCrud.class, true);

		for (ASTCrud crud : cruds) {
			addCrudDaoInjection(crud, r);
			addCrudDaoMethods(crud, r);
		}

	}


	private void addCrudDaoMethods(ASTCrud crud, ServiceResource r) throws CreateException {
		String entityName = crud.getEntity();
		String entityFqName = getGlobalCtxt().getFqNameForRegisteredType(entityName);
		if (entityFqName == null) {
			throw new CrudEntityNotFoundException(entityName, r.getName());
		}
		addSaveMethod(entityName, entityFqName, r);
		addGetAllMethod(entityName, entityFqName, r);
		addFindMethod(entityName, entityFqName, r);
		addSearchMethod(entityName, entityFqName, r);
	}


	private void addFindMethod(String entityName, String entityFqName, ServiceResource r) throws CreateException {
		Method find = new Method("find" + entityName);
		Type type = new Type(entityFqName);
		find.setReturnType(type);
		find.addParameter(new Method.Parameter("id", new Type("Long")));

		Statement stmt = new Statement("return " + lowerCamel(entityName) + "Dao.find(id, " + entityName + ".class)");
		find.getBody().add(stmt);
		r.addMethod(find);
	}


	private void addSearchMethod(String entityName, String entityFqName, ServiceResource r) throws CreateException {
		String pkg = getGlobalCtxt().getDefaultPackage();
		Method search = new Method("search" + entityName);
		Type type = new Type("java.util.List<" + entityFqName + ">");
		search.setReturnType(type);
		search.addParameter(new Method.Parameter("searcher", new Type(pkg + ".base.dao.SearchInfo")));
		String daoname = lowerCamel(entityName) + "Dao";
		String expr = "return " + daoname + ".findByNamedQuery(searcher.getSearcherId(), searcher.getParamMap())";
		search.getBody().add(new Statement(expr));
		r.addMethod(search);
	}


	private void addGetAllMethod(String entityName, String entityFqName, ServiceResource r) throws CreateException {
		Method getall = new Method("getAll" + entityName + "s");
		Type type = new Type("java.util.List<" + entityFqName + ">");
		getall.setReturnType(type);

		Statement stmt = new Statement("return " + lowerCamel(entityName) + "Dao.findByNamedQuery(" + entityName
				+ ".ALL)");
		getall.getBody().add(stmt);
		r.addMethod(getall);
	}


	private void addSaveMethod(String entityName, String entityFqName, ServiceResource r) throws CreateException {
		Method save = new Method("save" + entityName);
		Type type = new Type(entityFqName);
		save.setReturnType(type);
		Method.Parameter param = new Method.Parameter(entityName.toLowerCase(), type);
		save.addParameter(param);

		Statement stmt = new Statement(entityName + " managed" + entityName + " = find" + entityName + "("
				+ entityName.toLowerCase() + ".getId())", "");
		Statement stmt2 = new Statement("managed" + entityName + ".merge(" + entityName.toLowerCase() + ")");
		Statement stmt3 = new Statement("return " + lowerCamel(entityName) + "Dao.save(managed" + entityName + ")");

		save.getBody().add(stmt);
		save.getBody().add(stmt2);
		save.getBody().add(stmt3);
		r.addMethod(save);
	}


	private void addCrudDaoInjection(ASTCrud crud, ServiceResource r) throws CreateException {
		String pkg = getGlobalCtxt().getDefaultPackage() + ".base.dao";
		String name = lowerCamel(crud.getEntity()) + "Dao";
		Field f = new Field(name, "CrudDao<" + crud.getEntity() + ">", pkg);
		f.setHasGetter(false);
		f.setHasSetter(false);
		f.addAnotation("javax.ejb.EJB");
		r.addProperty(f);
	}


	private String lowerCamel(String name) {
		return UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
	}


	private void addServiceMethods(ASTService service, ServiceResource r) throws CreateException {
		for (ASTAction action : service.getActions()) {
			Method m = new Method(action.getImage());

			m.setReturnType(new Type(action.getReturnType()));
			addParameters(action.getActionParams(), m);
			r.addMethod(m);
		}

	}


	private void addParameters(List<ASTActionParam> actionParams, Method m) throws CreateException {
		for (ASTActionParam param : actionParams) {
			String fqParamName = getGlobalCtxt().getFqNameForRegisteredType(param.getType());
			Method.Parameter mParam = new Method.Parameter(param.getName(), new Type(fqParamName));
			m.addParameter(mParam);
		}
	}
}
