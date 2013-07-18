package ro.sft.recruiter.framework.action;

public class ActionResult {

	public enum Status {
		SUCCESS, INFO, WARN, ERROR
	}

	private Status status;

	private Message message;

	private Class<? extends Action<?>> action;

	public ActionResult(Class<? extends Action<?>> t, Status success) {
		status = success;
		this.action = t;
	}

	public Class<? extends Action<?>> getAction() {
		return action;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public static ActionResult succes(Class<? extends Action<?>> cls) {
		return new ActionResult(cls, Status.SUCCESS);
	}

}
