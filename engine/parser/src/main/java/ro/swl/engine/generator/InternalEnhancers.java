package ro.swl.engine.generator;

import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.model.Resource;


public class InternalEnhancers extends Technology {

	@Override
	public List<? extends Enhancer<? extends Resource>> getEnhancers() {
		//return asList(new InternalFieldsEnhancer(), new InternalTypesEnhancer(), new RelationsEnhancer());
		List<Enhancer<? extends Resource>> list = new ArrayList<Enhancer<? extends Resource>>();
		list.add(new RelationsEnhancer());
		list.add(new InternalTypesEnhancer());
		list.add(new InternalFieldsEnhancer());
		return list;
	}
}
