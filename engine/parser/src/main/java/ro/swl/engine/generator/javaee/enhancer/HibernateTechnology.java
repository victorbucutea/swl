package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;

import java.util.Arrays;
import java.util.List;


public class HibernateTechnology extends Technology {


	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		return Arrays.asList(new HibernatePomXmlEnhancer(), new HibernatePersistenceXmlEnhancer());
	}

}
