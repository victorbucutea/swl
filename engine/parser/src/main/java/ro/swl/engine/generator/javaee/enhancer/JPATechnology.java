package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;

import java.util.List;

import static java.util.Arrays.asList;


public class JPATechnology extends Technology {



	@SuppressWarnings("unchecked")
	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		return asList(new JPAEntityEnhancer(), new JPAPomXmlEnhancer(), new JPAHelperMethodEnhancer());
	}
}
