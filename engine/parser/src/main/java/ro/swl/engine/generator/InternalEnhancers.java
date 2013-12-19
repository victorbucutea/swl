package ro.swl.engine.generator;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.model.Resource;


public class InternalEnhancers extends Technology {

	public InternalEnhancers(GenerationContext ctxt) {
		super(ctxt);
	}


	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
		List<Enhancer<? extends Resource>> list = new ArrayList<Enhancer<? extends Resource>>();
		list.add(new RelationsEnhancer());
		return list;
	}

}
