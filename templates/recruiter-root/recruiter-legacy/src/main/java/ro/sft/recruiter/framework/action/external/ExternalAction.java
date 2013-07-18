package ro.sft.recruiter.framework.action.external;

import ro.sft.recruiter.framework.action.Action;

public abstract class ExternalAction<T> implements Action<T> {

	private Spec specs;

	public ExternalAction(Spec conf) {
		this.specs = conf;
	}

}
