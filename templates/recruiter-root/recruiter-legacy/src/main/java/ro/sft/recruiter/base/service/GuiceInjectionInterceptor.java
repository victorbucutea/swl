package ro.sft.recruiter.base.service;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceInjectionInterceptor {

	private Injector injector;

	@PostConstruct
	public void startupGuice(InvocationContext ctx) throws Exception {
		ctx.proceed();
		injector = Guice.createInjector(/* new AppConfigModule() */);
		injector.injectMembers(ctx.getTarget());
	}
}
