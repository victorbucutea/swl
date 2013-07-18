package ro.sft.recruiter.base.security;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class AuthorisationInterceptor {

	@AroundInvoke
	public Object checkAuthorisation(InvocationContext ic) throws Exception {
		return ic.proceed();
	}
}
