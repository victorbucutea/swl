#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ${package}.${rootArtifactId}.base.model.Entity;

@Local
public interface CrudDao<T extends Entity> {

	public T create(T t);

	public T find(Long id, Class<T> clazz);

	public T save(T t);

	void delete(T t);

	List<T> findByNamedQuery(String name);

	List<T> findByNamedQuery(String name, Map<String, Object> parameters);

}
