package ro.sft.recruiter.ui.interview;

import ro.sft.recruiter.ui.common.Presenter;

import com.google.inject.Inject;

public class InterviewPresenter implements Presenter {

	@Inject
	InterviewView interviewView;

	@Override
	public void init() {

		// execute init code... like fetching model data from the DB

		interviewView.init();

		// execute post init code
	}

	public void validateCV() {

	}
}
