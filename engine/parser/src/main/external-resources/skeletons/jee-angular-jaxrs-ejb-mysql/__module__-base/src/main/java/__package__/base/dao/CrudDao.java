#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.base.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ${basePackage}.base.model.EntityBase;

@Local
public interface CrudDao<T extends EntityBase> {

	public T create(T t);

	public T find(Long id, Class<T> clazz);

	public T save(T t);

	void delete(T t);

	List<T> findByNamedQuery(String name);

	List<T> findByNamedQuery(String name, Map<String, Object> parameters);

}
