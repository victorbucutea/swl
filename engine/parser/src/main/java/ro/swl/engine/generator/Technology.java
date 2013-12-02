package ro.swl.engine.generator;

import java.util.List;

import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class Technology {

	private GenerationContext ctxt;


	public Technology(GenerationContext ctxt) {
		this.ctxt = ctxt;
	}


	public void enhance(Resource res, ASTSwdlApp appModel) throws GenerateException {

		for (Enhancer<? extends Resource> enhancer : getEnhancers()) {

			if (enhancer.accepts(res)) {
				enhancer.enhanceInternal(appModel, res, ctxt);
			}

		}
	}


	public GenerationContext getCtxt() {
		return this.ctxt;
	}


	public abstract List<Enhancer<? extends Resource>> getEnhancers();

}
