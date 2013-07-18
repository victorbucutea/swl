package ro.sft.recruiter.auth;

import java.security.Principal;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.sft.recruiter.user.model.User;

/**
 * Authentication facade. It provides login/logout and also serializes user
 * properties.
 * 
 * Good place for:
 * 1. recover password functionality.
 * 2. Good place to protect against brute force attacks ( delays on multiple
 * login attempts, CAPTCHA challenge, account lock-out, etc.)
 * 
 * @author VictorBucutea
 * 
 */
@Path("/auth")
@ApplicationPath("/rest")
public class AuthenticationRestFacade {

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(User loginInfo, @Context HttpServletRequest request) throws LoginException, ServletException {

		// nasty work-around for Catalina AuthenticatorBase to be able to 
		// change/create the session cookie 
		request.getSession();
		// end nasty work-around

		request.login(loginInfo.getName(), loginInfo.getPassword());

		Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal == null) {
			throw new FailedLoginException("Login failed");
		}

		User u = new User();
		u.setName(userPrincipal.getName());
		return u;
	}

	@POST
	@Path("/logout")
	public void logout(@Context HttpServletRequest request) throws ServletException {
		request.logout();
	}

}
