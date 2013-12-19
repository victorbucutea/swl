package ro.swl.engine.generator.javaee.enhancer;

import java.util.List;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.model.Resource;


public class ResteasyTechnology extends Technology {

	public ResteasyTechnology(GenerationContext ctxt) {
		super(ctxt);
	}


	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		return null;
	}

}