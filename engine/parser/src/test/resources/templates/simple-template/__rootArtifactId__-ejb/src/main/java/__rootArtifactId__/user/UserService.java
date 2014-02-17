#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.user;

import java.util.List;

import javax.ejb.Local;

import ${package}.${rootArtifactId}.user.model.User;

@Local
public interface UserService {

	public List<User> getUsers();

}
