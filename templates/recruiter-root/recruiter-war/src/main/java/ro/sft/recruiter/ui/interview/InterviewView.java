package ro.sft.recruiter.ui.interview;

import ro.sft.recruiter.job.model.Ad;
import ro.sft.recruiter.ui.common.View;

import com.google.inject.Inject;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class InterviewView implements View {

	@Inject
	private CustomLayout mainLayout;

	@Inject
	private Window window;

	private Form adForm;

	@Override
	public void init() {
		initMainScreen();
	}

	private void initMainScreen() {

		VerticalLayout vertical = new VerticalLayout();

		Ad ad = new Ad();
		BeanItem<Ad> adBean = new BeanItem<Ad>(ad); // item from

		adForm = new com.vaadin.ui.Form();
		adForm.setCaption("Ad form");
		adForm.setWriteThrough(false); // we want explicit 'apply'
		adForm.setInvalidCommitted(false); // no invalid values in datamodel
		adForm.setFormFieldFactory(DefaultFieldFactory.get());
		adForm.setItemDataSource(adBean);

		vertical.addComponent(adForm);
		vertical.addComponent(new Label("Some text"));
		// Add form to layout
		mainLayout.addComponent(vertical, "content");

		HorizontalLayout horizLayout = new HorizontalLayout();

		Button button = new Button("Save");
		button.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				window.showNotification("Saving form");
			}
		});
		horizLayout.addComponent(button);

		vertical.addComponent(horizLayout);

	}

	public void save() {
	}
}
