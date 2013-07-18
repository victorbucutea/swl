package ro.sft.recruiter.framework.action;

import java.util.Set;


public interface FindAction<T> {

	public T find(Long id, Agregator<T> agregator);

	public Set<T> findAll(Agregator<T> agregator);

}
