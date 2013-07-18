package ro.sft.recruiter.ui.common;

import com.google.inject.Inject;
import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

public class MainView implements View {

	@Inject
	Application application;

	@Inject
	Window mainWindow;

	@Inject
	CustomLayout mainLayout;

	@Inject
	MainPresenter mainPresenter;

	@Override
	public void init() {
		initMainLayout();
	}

	private void initMainLayout() {
		application.setMainWindow(mainWindow);
		application.setTheme("runo");
		mainWindow.setContent(mainLayout);

		initNavigationMenu();
	}

	private void initNavigationMenu() {
		VerticalLayout navigationMenu = new VerticalLayout();
		mainLayout.addComponent(navigationMenu, "menu");
		drawJobsMenu(navigationMenu);
		navigationMenu.addComponent(new Label(" "));
		drawInterviewMenu(navigationMenu);

	}

	@SuppressWarnings("serial")
	private void drawInterviewMenu(VerticalLayout navigationMenu) {
		Button interview = new Button("Interview");
		interview.setStyleName(Runo.BUTTON_LINK);
		interview.setDescription("Interview");
		interview.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.showNotification("Opening Interview menu");
				mainPresenter.openInterviewMenu();
			}
		});
		navigationMenu.addComponent(interview);
	}

	@SuppressWarnings("serial")
	private void drawJobsMenu(VerticalLayout navigationMenu) {
		Button jobs = new Button("Jobs");
		jobs.setStyleName(Runo.BUTTON_LINK);
		jobs.setDescription("Jobs");
		jobs.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.showNotification("Opening jobs menu");
				mainPresenter.openJobsMenu();
			}
		});
		navigationMenu.addComponent(jobs);
	}
}
