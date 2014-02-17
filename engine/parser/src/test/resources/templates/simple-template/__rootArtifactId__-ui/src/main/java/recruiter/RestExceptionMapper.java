#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.recruiter;

import static javax.ws.rs.core.Response.serverError;
import static javax.ws.rs.core.Response.status;

import javax.security.auth.login.LoginException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception e) {
		e.printStackTrace();

		Response.ResponseBuilder response = null;

		/*
		 * Ideally we should only have a fixed set of exceptions (and
		 * subclasses) we must treat:
		 * 
		 * 1. javax.servlet.ServletException - for servlet container errors
		 * 2. javax.ejb.EJBException - for EJB container errors
		 * 3. javax.security.LoginException
		 */

		if (e instanceof LoginException) {
			response = status(Response.Status.UNAUTHORIZED);
		} else if (e.getCause() instanceof LoginException) {
			response = status(Response.Status.UNAUTHORIZED);
		} else {
			response = serverError();
		}

		return response.entity("").type(MediaType.APPLICATION_JSON).build();
	}
}