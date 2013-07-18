package ro.sft.recruiter.ui.jobs;

import ro.sft.recruiter.ui.common.View;

import com.google.inject.Inject;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class JobsView implements View {

	@Inject
	private CustomLayout mainLayout;

	@Override
	public void init() {
		initMainScreen();
	}

	private void initMainScreen() {

		VerticalLayout jobsContentLayout = new VerticalLayout();

		initTitle(jobsContentLayout);
		initTable(jobsContentLayout);

		// Add title and table to main layout
		mainLayout.addComponent(jobsContentLayout, "content");

	}

	private void initTitle(VerticalLayout jobsContentLayout) {
		Label label = new Label("This is the Jobs content");
		jobsContentLayout.addComponent(label);
	}

	private void initTable(VerticalLayout jobsContentLayout) {
		final Table table = new Table("My Table");
		table.addContainerProperty("Name", String.class, null);
		table.addContainerProperty("Description", TextArea.class, null);
		table.addContainerProperty("Delete", CheckBox.class, null);

		// Insert this data
		String people[][] = { { "Galileo", "Liked to go around the Sun" }, { "Monnier", "Liked star charts" },
				{ "VÀisÀlÀ", "Liked optics" }, { "Oterma", "Liked comets" },
				{ "Valtaoja", "Likes cosmology and still lives unlike the others above" }, };

		// Insert the data and the additional component column
		for (int i = 0; i < people.length; i++) {
			TextArea area = new TextArea(null, people[i][1]);
			area.setRows(2);

			// Add an item with two components
			Object obj[] = { people[i][0], area, new CheckBox() };
			table.addItem(obj, new Integer(i));
		}
		table.setPageLength(table.size());
		jobsContentLayout.addComponent(table);
	}
}
