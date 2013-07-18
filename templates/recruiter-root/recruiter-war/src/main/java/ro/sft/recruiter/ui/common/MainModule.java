package ro.sft.recruiter.ui.common;

import ro.sft.recruiter.cv.CvService;
import ro.sft.recruiter.cv.CvServiceBean;
import ro.sft.recruiter.ui.interview.InterviewPresenter;
import ro.sft.recruiter.ui.interview.InterviewView;
import ro.sft.recruiter.ui.jobs.JobsPresenter;
import ro.sft.recruiter.ui.jobs.JobsView;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vaadin.Application;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Window;

public class MainModule extends AbstractModule {

	private Application app;

	@Override
	protected void configure() {

		bind(MainView.class);
		bind(Application.class).toInstance(app);
		bind(Window.class).toInstance(new Window("eRecruiter"));
		bind(Presenter.class).to(MainPresenter.class);
		bind(CustomLayout.class).toInstance(new CustomLayout("mainLayout"));

		bind(JobsView.class);
		bind(JobsPresenter.class);

		bind(InterviewView.class);
		bind(InterviewPresenter.class);

	}

	@Provides
	public CvService createCVService() {
		// lookup code to return a CV service
		return new CvServiceBean();
	}

	public void setApplication(Application app) {
		this.app = app;
	}
}
