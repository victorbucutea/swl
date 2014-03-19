package ro.swl.engine.generator.javaee.enhancer;

import static java.util.Arrays.asList;

import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;


public class EJBTechnology extends Technology {


	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Enhancer<? extends Resource>> getEnhancers() {
		return asList(new ServiceEJBEnhancer(), new SearcherEnhancer());
	}

}
