package ro.sft.recruiter.framework.action;

public interface Action<T> {

	public ActionResult execute(T entity);

}
