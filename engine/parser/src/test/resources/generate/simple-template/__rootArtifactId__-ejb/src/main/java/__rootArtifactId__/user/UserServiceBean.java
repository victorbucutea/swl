#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.user;

import java.util.List;

import javax.ejb.EJB;

import ${package}.${rootArtifactId}.base.dao.CrudDao;
import ${package}.${rootArtifactId}.user.model.User;

public class UserServiceBean implements UserService {

	@EJB
	private CrudDao<User> userDao;

	@Override
	public List<User> getUsers() {
		return userDao.findByNamedQuery(User.ALL);
	}

}
