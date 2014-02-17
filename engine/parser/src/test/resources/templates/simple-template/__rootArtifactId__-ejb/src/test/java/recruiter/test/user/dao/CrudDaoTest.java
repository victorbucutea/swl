#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.test.user.dao;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ${package}.${rootArtifactId}.base.dao.CrudDaoBean;
import ${package}.${rootArtifactId}.test.base.DaoTest;
import ${package}.${rootArtifactId}.test.base.TestedObject;
import ${package}.${rootArtifactId}.user.model.Group;
import ${package}.${rootArtifactId}.user.model.Role;
import ${package}.${rootArtifactId}.user.model.User;

public class CrudDaoTest extends DaoTest {

	@TestedObject
	private CrudDaoBean<User> crudDao = new CrudDaoBean<User>();

	@Override
	@Test
	public void find() {
		Role role1 = createRole("role1");
		User user = createUser("user1", role1);
		crudDao.create(user);

		User fetchedUser = crudDao.find(user.getId(), User.class);

		assertNotNull(fetchedUser);
		assertEquals("role1", fetchedUser.getGroups().iterator().next().getRoles().iterator().next().getName());
	}

	@Override
	@Test
	public void delete() {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");
		User user = createUser("user1", role1, role2);
		crudDao.create(user);

		crudDao.delete(user);

		User fetchedUser = crudDao.find(user.getId(), User.class);
		assertNull(fetchedUser);
	}

	@Override
	@Test
	public void save() {
		Role role1 = createRole("role1");
		Role role2 = createRole("role2");
		Role role3 = createRole("role2");
		User user = createUser("user1", role1, role2, role3);

		User user2 = createUser("user2", role2, role3);

		crudDao.create(user);
		crudDao.create(user2);
	}

	@Override
	@Test
	public void findByNamedQuery() {
		// TODO Auto-generated method stub

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
