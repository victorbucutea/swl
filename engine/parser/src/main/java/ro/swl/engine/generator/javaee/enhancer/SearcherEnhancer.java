package ro.swl.engine.generator.javaee.enhancer;

import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.javaee.model.EntityField;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.ASTCrud;
import ro.swl.engine.parser.ASTLogic;
import ro.swl.engine.parser.ASTModule;
import ro.swl.engine.parser.ASTSearcher;
import ro.swl.engine.parser.ASTService;
import ro.swl.engine.parser.ASTSwdlApp;

import com.google.common.base.CaseFormat;


public class SearcherEnhancer extends Enhancer<EntityResource> {

	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource r) throws GenerateException {

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


	private void addSearchers(ASTCrud crud, EntityResource r) throws GenerateException {
		Annotation namedQueries = new Annotation("javax.persistence.NamedQueries");
		String entityName = r.getName();

		List<ASTSearcher> searchers = crud.getSearchers();

		for (ASTSearcher searcher : searchers) {
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
}
