package ro.swl.engine.generator.javaee.enhancer;

import ro.swl.engine.generator.Enhancer;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.javaee.model.PomXml;
import ro.swl.engine.generator.model.Resource;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


public class ResteasyTechnology extends Technology {


	@Override
	public List<Enhancer<? extends Resource>> getEnhancers() {
        List<Enhancer<? extends Resource>> list = new ArrayList<Enhancer<? extends Resource>>();
        list.add(new ResteasyPomXmlEnhancer());
		return list;
	}

}
