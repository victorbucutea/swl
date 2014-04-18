#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.dao;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Remove;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ${package}.${rootArtifactId}.base.model.Entity;

/**
 * Provides pagination for an entity. One would have never guessed had I not
 * written this javadoc.
 * 
 * @author VictorBucutea
 * 
 * @param <T>
 */
// @Stateful
public class PaginatorDao<T extends Entity> implements Iterator<List<T>> {

	private int pageNo = 0;
	private int pageSize = 30;
	private boolean next = true;

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean hasNext() {
		return next;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> next() {
		Query q = em.createNamedQuery("all");
		q.setFirstResult(getFirst());
		q.setMaxResults(pageSize);
		List<T> entityList = q.getResultList();
		return entityList;
	}

	private int getFirst() {
		return pageNo * pageSize;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove. Do not call!");
	}

	@Remove
	public void close() {
		em.clear();
		em.close();
	}

}
