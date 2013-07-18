package ro.sft.recruiter.user;

import java.util.List;

import javax.ejb.EJB;

import ro.sft.recruiter.base.dao.CrudDao;
import ro.sft.recruiter.user.model.User;

public class UserServiceBean implements UserService {

	@EJB
	private CrudDao<User> userDao;

	@Override
	public List<User> getUsers() {
		return userDao.findByNamedQuery(User.ALL);
	}

}
