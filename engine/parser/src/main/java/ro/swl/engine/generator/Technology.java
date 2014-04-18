package ro.swl.engine.generator;

import java.util.List;

import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class Technology {



	public void enhance(Resource res, ASTSwdlApp appModel) throws CreateException {

		for (Enhancer<? extends Resource> enhancer : getEnhancers()) {

			if (enhancer.accepts(res)) {
				enhancer.enhanceInternal(appModel, res);
			}

		}

	}


	public abstract List<? extends Enhancer<? extends Resource>> getEnhancers();

}
