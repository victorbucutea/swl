package ro.sft.recruiter.framework.action.external;

import ro.sft.recruiter.framework.action.ActionResult;

public class SendEmailAction<T> extends ExternalAction<T> {

	public SendEmailAction(Spec spec) {
		super(spec);
	}

	public ActionResult execute(T entity) {
		return null;
	}
}
