package ro.sft.recruiter.framework.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteAction {

	public Class<? extends Action<?>>[] value();

	public AlternateAction[] alternateActions();
}
