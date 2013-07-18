package ro.sft.recruiter.test.base;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.sft.recruiter.base.dao.CrudDao;
import ro.sft.recruiter.base.util.ReflectionUtil;

public abstract class DaoTest {

	public EntityManager em;

	@Before
	public void setUp() throws Exception {
		setUpEm();
		injectEmIntoTestedDaos();
	}

	private void setUpEm() throws Exception {
		em = Persistence.createEntityManagerFactory("test").createEntityManager();
		em.getTransaction().begin();
	}

	private void injectEmIntoTestedDaos() throws Exception {
		CrudDao<?> dao = ReflectionUtil.getValueOfFieldAnnotatedWith(TestedObject.class, this, CrudDao.class);
		String persistenceCtxField = ReflectionUtil.getNameOfFieldAnnotatedWith(PersistenceContext.class, dao);
		ReflectionUtil.injectIntoField(dao, persistenceCtxField, em);
	}

	@After
	public void close() {
		em.getTransaction().commit();
	}

	@Test
	public abstract void find();

	@Test
	public abstract void delete();

	@Test
	public abstract void save();

	@Test
	public abstract void findByNamedQuery();

}
