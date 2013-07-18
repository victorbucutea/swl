package ro.sft.recruiter.ui.jobs;

import ro.sft.recruiter.ui.common.Presenter;

import com.google.inject.Inject;

public class JobsPresenter implements Presenter {

	@Inject
	JobsView jobsView;

	@Override
	public void init() {

		// execute init code... like fetching model data from the DB

		jobsView.init();

		// execute post init code
	}
}
