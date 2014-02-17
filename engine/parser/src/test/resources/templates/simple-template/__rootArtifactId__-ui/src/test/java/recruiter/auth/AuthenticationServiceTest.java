#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.recruiter.auth;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.Assert.fail;
import static ${package}.recruiter.base.util.ReflectionUtil.injectIntoField;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ${package}.recruiter.RestExceptionMapper;
import ${package}.recruiter.base.dao.CrudDaoBean;
import ${package}.recruiter.test.base.DaoTest;
import ${package}.recruiter.test.base.TestedObject;
import ${package}.recruiter.user.UserService;
import ${package}.recruiter.user.UserServiceBean;
import ${package}.recruiter.user.model.Group;
import ${package}.recruiter.user.model.Role;
import ${package}.recruiter.user.model.User;

@Ignore
public class AuthenticationServiceTest extends DaoTest {

	private Dispatcher dispatcher;

	@TestedObject
	private CrudDaoBean<User> userDao = new CrudDaoBean<User>();

	private UserService userService = new UserServiceBean();

	private AuthenticationRestFacade authenticationService = new AuthenticationRestFacade();

	@Before
	public void initDispatcher() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		dispatcher.getRegistry().addSingletonResource(authenticationService);
		dispatcher.getProviderFactory().addExceptionMapper(RestExceptionMapper.class);

	}

	@Before
	public void setUpService() {
		injectIntoField(userService, "dao", userDao);
		injectIntoField(authenticationService, "cvService", userService);
	}

	@Test
	public void exceptionMapper_servletExceptionMapped() throws URISyntaxException {

		userDao.create(createUser());

		String reqStr = "auth/login";
		MockHttpResponse response = invokePostUri(reqStr, "{${symbol_escape}"name${symbol_escape}":${symbol_escape}"asd${symbol_escape}",${symbol_escape}"password${symbol_escape}":${symbol_escape}"asd${symbol_escape}"}", false);

		// servlet exception should be mapped into an 
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}

	@Test
	public void loginTest_success() {
		fail();
	}

	@Test
	public void loginTest_loginFailed() {
		fail();
	}

	@Test
	public void addGroupToUser() {
		fail();
	}

	@Test
	public void removeGroupFromUser() {
		fail();
	}

	@Test
	public void addRoleToGroup() {
		fail();
	}

	@Test
	public void removeRoleFromGroup() {
		fail();
	}

	@Override
	@Test
	public void find() {
		// find user
		fail();
	}

	@Override
	@Test
	public void delete() {
		// delete user with children
		fail();
	}

	@Override
	@Test
	public void save() {
		fail();
	}

	@Override
	@Test
	public void findByNamedQuery() {
		// find with searcher
		fail();
	}

	private MockHttpResponse invokePostUri(String reqUri, String payload, boolean expectStatusOk)
			throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post(reqUri);
		request.setInputStream(toInputStream(payload));
		request.contentType(MediaType.APPLICATION_JSON);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		if (expectStatusOk) {
			assertResponseOK(response);
		}
		return response;
	}

	private MockHttpResponse invokeGetUri(String reqUri) throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get(reqUri);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		assertResponseOK(response);
		return response;
	}

	private void assertResponseOK(MockHttpResponse response) {
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	private User createUser() {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");
		User user = createUser("user1", role1, role2);
		return user;
	}

	private User createUser(String name, Role... roles) {
		User user = new User();
		user.setName(name);

		user.addGroup(createGroup(roles));
		return user;
	}

	private Role createRole(String name) {
		Role role = new Role();
		role.setName(name);
		return role;
	}

	private Group createGroup(Role... roles) {
		Group g = new Group();
		g.setName("group1");

		g.addRoles(roles);

		return g;
	}

}
