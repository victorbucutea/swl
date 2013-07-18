package ro.sft.recruiter.framework.action;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ExecuteActionInterceptor {

	private Injector injector;

	@AroundInvoke
	public Object interceptActionExecutions(InvocationContext context) throws Exception {
		Method method = context.getMethod();
		ExecuteAction actionsAnnotation = method.getAnnotation(ExecuteAction.class);

		if (actionsAnnotation == null) {
			context.proceed();
		}

		if (!(context.getParameters()[1] instanceof ExecuteActionLog)) {
			throw new IllegalArgumentException("Second parameter must be a ExecuteActionLog");
		}

		ExecuteActionLog log = (ExecuteActionLog) context.getParameters()[1];

		executeActions(actionsAnnotation, log);

		return context.proceed();

	}

	private void executeActions(ExecuteAction actionsAnnotation, ExecuteActionLog log) {
		// get instance
		// inject dependencies
	}

	@PostConstruct
	public void initGuice(InvocationContext context) throws Exception {
		context.proceed();
		injector = Guice.createInjector(new Module() {
			public void configure(Binder binder) {
				// TODO Auto-generated method stub

			}
		});

	}

}
