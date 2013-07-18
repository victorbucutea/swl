package ro.sft.recruiter.ui.common;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RecruiterApplication extends com.vaadin.Application {

	private static final long serialVersionUID = -5270428336353489423L;

	private MainPresenter mainPresenter;

	@Override
	public void init() {
		initGuiceModules();
		initMainWindow();
	}

	private void initMainWindow() {
		mainPresenter.init();
	}

	private void initGuiceModules() {
		MainModule mainModule = new MainModule();
		mainModule.setApplication(this);
		Injector injector = Guice.createInjector(mainModule);
		mainPresenter = injector.getInstance(MainPresenter.class);
	}
}