package ro.swl.engine.generator.javaee.enhancer;

import static java.util.Arrays.asList;

import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;


public class JaxRSTechnology extends Technology {

	public JaxRSTechnology(GenerationContext ctxt) {
		super(ctxt);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		return asList(new JaxRSPomXmlEnhancer(), new JaxRSEntityEnhancer());
	}

}
