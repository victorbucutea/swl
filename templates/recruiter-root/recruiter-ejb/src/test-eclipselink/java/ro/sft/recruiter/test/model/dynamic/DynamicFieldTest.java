package ro.sft.recruiter.test.model.dynamic;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import ro.sft.recruiter.base.dao.CrudDaoBean;
import ro.sft.recruiter.job.model.Ad;
import ro.sft.recruiter.test.base.DaoTest;
import ro.sft.recruiter.test.base.TestedObject;

@Ignore
public class DynamicFieldTest extends DaoTest {

	@TestedObject
	private CrudDaoBean<Ad> crudDao = new CrudDaoBean<Ad>();

	@Test
	public void saveAndGetByteArrayProperty() {
		Ad ad = new Ad();

		// add byte[]
		// ad.addDynamicBinaryProperty("someProp", "value".getBytes());
		// retrieve byte[]
		// byte[] dynamicStringPropertyValue =
		// ad.getDynamicBinaryPropertyValue("someProp");

		// assertNotNull(dynamicStringPropertyValue);

	}

	@Test
	public void persistStringProperty() {
		Ad ad = new Ad();

		// ad.addDynamicStringProperty("someProp", "value");
		crudDao.create(ad);
		Long id = ad.getId();
		em.getTransaction().commit();

		em.getTransaction().begin();
		Ad retrievedAd = crudDao.find(id, Ad.class);
		assertNotNull(retrievedAd);
		// String dynamicStringPropertyValue =
		// retrievedAd.getDynamicStringPropertyValue("someProp");
		// assertNotNull(dynamicStringPropertyValue);
		// assertEquals("value", dynamicStringPropertyValue);
	}

	@Test
	public void persistByteArrayProperty() {
		Ad ad = new Ad();

		// ad.addDynamicBinaryProperty("someProp", "value".getBytes());
		crudDao.create(ad);
		Long id = ad.getId();
		em.getTransaction().commit();

		em.getTransaction().begin();
		Ad retrievedAd = crudDao.find(id, Ad.class);
		assertNotNull(retrievedAd);
		// byte[] dynamicStringPropertyValue =
		// retrievedAd.getDynamicBinaryPropertyValue("someProp");
		// assertNotNull(dynamicStringPropertyValue);
		// assertArrayEquals("value".getBytes(), dynamicStringPropertyValue);

	}

	@Override
	@Test
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	@Test
	public void findByNamedQuery() {
		// TODO Auto-generated method stub

	}

	@Override
	@Test
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	@Test
	public void find() {
		// TODO Auto-generated method stub

	}
}
