package ro.swl.engine.generator.javaee.enhancer;

import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;


public class HibernateTechnology extends Technology {


	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		// return persistence xml enhancer, pom.xml enhancer
		return null;
	}

}
