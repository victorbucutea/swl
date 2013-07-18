package ro.sft.recruiter.test.cv.dao;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.collections.CollectionUtils.exists;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;

import org.apache.commons.collections.Predicate;
import org.junit.Test;

import ro.sft.recruiter.base.dao.CrudDao;
import ro.sft.recruiter.base.dao.CrudDaoBean;
import ro.sft.recruiter.base.util.ReflectionUtil;
import ro.sft.recruiter.interview.model.CV;
import ro.sft.recruiter.interview.model.Certification;
import ro.sft.recruiter.interview.model.CompanyType;
import ro.sft.recruiter.interview.model.Experience;
import ro.sft.recruiter.test.base.DaoTest;
import ro.sft.recruiter.test.base.TestedObject;

public class CrudDaoTest extends DaoTest {

	@TestedObject
	private CrudDaoBean<CV> crudDao = new CrudDaoBean<CV>();

	@Override
	public void delete() {
		CV cv = createCV();

		crudDao.create(cv);

		crudDao.delete(cv);

		assertEquals(0, crudDao.findByNamedQuery(Certification.ALL).size());
		assertEquals(0, crudDao.findByNamedQuery(Experience.ALL).size());
	}

	@Override
	public void save() {

		CV cv = createCV();
		CV cv2 = createCV();

		crudDao.save(cv);
		crudDao.save(cv2);

		List<CV> cvs = crudDao.findByNamedQuery(CV.ALL);
		assertNotNull(cvs);
		assertEquals(2, cvs.size());

		CV cvToUpdate = cvs.get(1);
		cvToUpdate.getCertifications().iterator().next().setName("new name");
		crudDao.save(cvToUpdate);

		assertTrue(exists(crudDao.find(cvToUpdate.getId(), CV.class).getCertifications(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return "new name".equals(((Certification) object).getName());
			}
		}));
	}

	@Override
	public void find() {
		CV cv = createCV();
		crudDao.save(cv);
		assertEquals(cv, crudDao.find(cv.getId(), CV.class));
	}

	@Override
	public void findByNamedQuery() {
		crudDao.create(createCV());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstName", "Ghita");
		List<CV> foundCv = crudDao.findByNamedQuery(CV.FIRST_NAME, params);
		assertEquals(1, foundCv.size());
	}

	@Test
	public void eagerLoadChildrenOnSave() {
		CV cv = crudDao.create(createCV());
		assertChildrenAreEagerLoaded(cv);
	}

	@Test
	public void lazyLoadChildrenOnFind() {
		crudDao.create(createCV());
		em.getTransaction().commit();
		em.close();
		em = Persistence.createEntityManagerFactory("test").createEntityManager();
		em.getTransaction().begin();
		CrudDao<?> dao = ReflectionUtil.getValueOfFieldAnnotatedWith(TestedObject.class, this, CrudDao.class);
		String persistenceCtxField = ReflectionUtil.getNameOfFieldAnnotatedWith(PersistenceContext.class, dao);
		ReflectionUtil.injectIntoField(dao, persistenceCtxField, em);
		CV cvs = crudDao.find(1L, CV.class);
		assertChildrenAreLazyLoaded(cvs);
	}

	private void assertChildrenAreLazyLoaded(CV cv1) {
		PersistenceUnitUtil persistenceUnitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
		assertFalse(persistenceUnitUtil.isLoaded(cv1, "experiences"));
		assertFalse(persistenceUnitUtil.isLoaded(cv1, "certifications"));
		assertFalse(persistenceUnitUtil.isLoaded(cv1, "educations"));
	}

	private void assertChildrenAreEagerLoaded(CV cv) {
		PersistenceUnitUtil persistenceUnitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
		assertTrue(persistenceUnitUtil.isLoaded(cv, "experiences"));
		assertTrue(persistenceUnitUtil.isLoaded(cv, "certifications"));
		assertTrue(persistenceUnitUtil.isLoaded(cv, "educations"));
	}

	private CV createCV() {
		CV cv = new CV();
		cv.addCertification(createCertification("SCEA"));
		cv.addCertification(createCertification("SCJP"));
		cv.addCertification(createCertification("SCJD"));
		cv.addExperience(createExperience("Oracle"));
		cv.setFileName("X.doc");
		cv.setRawFile(new byte[] { 12, 31, 4, 22 });
		cv.setFirstName("Ghita");
		cv.setLastName("Popescu");
		return cv;
	}

	private Experience createExperience(String string) {
		Experience e = new Experience();

		e.setCompanyType(CompanyType.AEROSPACE);
		e.setEndDate(new Date());
		e.setStartDate(new Date());

		return e;
	}

	private Certification createCertification(String name) {
		Certification cert = new Certification();
		cert.setCreationDate(new Date());
		cert.setCreationUser("X");
		cert.setDateObtained(new Date());
		cert.setName(name);
		return cert;
	}

}
