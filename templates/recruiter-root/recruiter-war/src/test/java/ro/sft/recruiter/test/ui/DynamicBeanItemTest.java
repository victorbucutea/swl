package ro.sft.recruiter.test.ui;

import org.junit.Ignore;
import org.junit.Test;

import ro.sft.recruiter.job.model.Ad;

@Ignore
public class DynamicBeanItemTest {

	@Test
	public void dynamicPropsRecordedInBean() {
		Ad ad = new Ad();

		// // ad.addDynamicStringProperty("prop1", "value");
		// DynamicBeanItem<Ad> adBean = new DynamicBeanItem<Ad>(ad);
		//
		// assertNotNull(adBean.getItemProperty("prop1"));
		// assertEquals("value", adBean.getItemProperty("prop1"));
	}

}
