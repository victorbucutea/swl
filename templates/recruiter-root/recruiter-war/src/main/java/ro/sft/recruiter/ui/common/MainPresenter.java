package ro.sft.recruiter.ui.common;

import ro.sft.recruiter.ui.interview.InterviewPresenter;
import ro.sft.recruiter.ui.jobs.JobsPresenter;

import com.google.inject.Inject;

public class MainPresenter implements Presenter {

	@Inject
	private MainView view;

	@Inject
	private JobsPresenter jobsPresenter;

	@Inject
	private InterviewPresenter interviewPresenter;

	@Override
	public void init() {
		view.init();
	}

	public void openJobsMenu() {
		jobsPresenter.init();
	}

	public void openInterviewMenu() {
		interviewPresenter.init();
	}
}
