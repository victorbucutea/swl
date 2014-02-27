package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerateException;
import ro.swl.engine.generator.java.model.Annotation;
import ro.swl.engine.generator.javaee.model.EntityResource;
import ro.swl.engine.parser.ASTSwdlApp;


public class SearcherEnhancer extends Enhancer<EntityResource> {

	@Override
	public void enhance(ASTSwdlApp appModel, EntityResource r) throws GenerateException {

		Annotation namedQueries = new Annotation("javax.persistence.NamedQueries");
		
		Annotation namedQuery = new Annotation("javax.persistence.NamedQuery");
		
		appModel.getModules();
		namedQueries.ad
	}
}
