package ro.swl.engine.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.parser.ASTSwdlApp;


public abstract class Enhancer<T extends Resource> {


	@SuppressWarnings("unchecked")
	public void enhanceInternal(ASTSwdlApp appModel, Resource r) throws CreateException {
		if (!getGenericClass().isAssignableFrom(r.getClass())) {
			throw new CreateException("Internal error while enhancing. Type of resource ('" + r.getClass()
					+ "') is not accepted by enhancer. Current enhancer only accepts " + getGenericClass());
		}

		enhance(appModel, (T) r);
	}


	public abstract void enhance(ASTSwdlApp appModel, T r) throws CreateException;


	public boolean accepts(Resource res) {
		Class<?> acceptedCls = getGenericClass();
		if (acceptedCls == null)
			return false;

		if (acceptedCls.isAssignableFrom(res.getClass())) {
			return true;
		}

		return false;
	}


	private Class<?> getGenericClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type typeArgument = genericSuperclass.getActualTypeArguments()[0];
		if (typeArgument instanceof Class) {
			return (Class<?>) typeArgument;
		} else if (typeArgument instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) typeArgument).getRawType();
		} else {
			return null;
		}
	}
}
