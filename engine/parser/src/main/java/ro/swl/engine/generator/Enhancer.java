package ro.swl.engine.generator;

import java.lang.reflect.ParameterizedType;

import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class Enhancer<T extends Resource> {


	@SuppressWarnings("unchecked")
	public void enhanceInternal(ASTSwdlApp appModel, Resource r, GenerationContext ctxt) throws GenerateException {
		if (!r.getClass().isAssignableFrom(getGenericClass())) {
			throw new GenerateException("Internal error while enhancing. Type of resource ('" + r.getClass()
					+ "') is not accepted by enhancer. Current enhancer only accepts " + getGenericClass());
		}

		enhance(appModel, (T) r, ctxt);
	}


	public abstract void enhance(ASTSwdlApp appModel, T r, GenerationContext ctxt) throws GenerateException;


	public boolean accepts(Resource res) {
		Class<?> acceptedCls = getGenericClass();
		if (acceptedCls == null)
			return false;

		if (res.getClass().isAssignableFrom(acceptedCls)) {
			return true;
		}

		return false;
	}


	private Class<?> getGenericClass() {
		Class<?> acceptedCls = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		return acceptedCls;
	}

}
