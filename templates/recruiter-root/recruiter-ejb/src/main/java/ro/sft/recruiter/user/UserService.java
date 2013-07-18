package ro.sft.recruiter.user;

import java.util.List;

import javax.ejb.Local;

import ro.sft.recruiter.user.model.User;

@Local
public interface UserService {

	public List<User> getUsers();

}
