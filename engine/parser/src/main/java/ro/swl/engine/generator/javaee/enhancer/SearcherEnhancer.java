package ro.swl.engine.generator.javaee.enhancer;

import com.google.common.base.CaseFormat;
import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.*;

import java.util.List;


public class SearcherEnhancer extends Enhancer<EntityResource> {

	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource r) throws CreateException {

		String currentModule = r.getModuleName();

		ASTModule module = appModel.findModule(currentModule);
		ASTLogic logic = module.getLogic();
		List<ASTService> services = logic.getServices();

		for (ASTService service : services) {
			List<ASTCrud> cruds = service.getChildNodesOfType(ASTCrud.class, true);
			String entityName = r.getName();

			for (ASTCrud crud : cruds) {
				if (crud.getEntity().equals(entityName)) {
					addSearchers(crud, r);
				}
			}
		}

	}


	private void addSearchers(ASTCrud crud, EntityResource r) throws CreateException {
		Annotation namedQueries = new Annotation("javax.persistence.NamedQueries");
		String entityName = r.getName();

		addFindAllNamedQuery(namedQueries, r);

		for (ASTSearcher searcher : crud.getSearchers()) {
			Annotation namedQuery = new Annotation("javax.persistence.NamedQuery");
			namedQueries.addPropertyAnnotation("value", namedQuery);
			String namedQueryName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, searcher.getName());
			namedQuery.addProperty("name", entityName + "." + namedQueryName);
			namedQuery.addPropertyLiteral("query", searcher.getQuery());


			EntityField namedQueryFinal = new EntityField("String", namedQueryName, "");
			namedQueryFinal.setInitializingExpression("\"" + searcher.getName() + "\"");
			r.addStaticFinalProperty(namedQueryFinal);
		}

		r.addAnnotation(namedQueries);
	}


	private void addFindAllNamedQuery(Annotation namedQueries, EntityResource r) throws CreateException {
		EntityField findAllNamedQ = new EntityField("String", "ALL", "");
		findAllNamedQ.setInitializingExpression("\"all\"");
		r.addStaticFinalProperty(findAllNamedQ);

		Annotation namedQuery = new Annotation("javax.persistence.NamedQuery");
		namedQuery.addProperty("name", r.getName() + ".ALL");
		namedQuery.addPropertyLiteral("query", "Select j from " + r.getName());
		namedQueries.addPropertyAnnotation("value", namedQuery);
	}
}
