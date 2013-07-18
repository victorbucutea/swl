package ro.sft.recruiter.test.cv.dao;

import static com.google.common.collect.Iterables.any;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ro.sft.recruiter.base.util.PersistenceUtil.isinitialized;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceUnitUtil;

import org.junit.Ignore;
import org.junit.Test;

import ro.sft.recruiter.base.dao.CrudDaoBean;
import ro.sft.recruiter.interview.model.CV;
import ro.sft.recruiter.interview.model.Certification;
import ro.sft.recruiter.interview.model.CompanyType;
import ro.sft.recruiter.interview.model.Education;
import ro.sft.recruiter.interview.model.Experience;
import ro.sft.recruiter.interview.model.Project;
import ro.sft.recruiter.test.base.DaoTest;
import ro.sft.recruiter.test.base.TestedObject;

import com.google.common.base.Predicate;

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

		CV fetchedCv = crudDao.find(cvToUpdate.getId(), CV.class);
		Set<Certification> certifications = fetchedCv.getCertifications();

		assertTrue(any(certifications, new Predicate<Certification>() {
			@Override
			public boolean apply(Certification cert) {
				return "new name".equals(cert.getName());
			}
		}));
	}

	@Override
	public void find() {
		CV cv = createCV();
		CV savedCv = crudDao.save(cv);
		assertEquals(savedCv, crudDao.find(savedCv.getId(), CV.class));
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
	public void findByComplexNamedQuery() {
		crudDao.create(createCV());
		crudDao.create(createCV());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstName", "Ghita");
		List<CV> foundCv = crudDao.findByNamedQuery(CV.EXP_EDU_CER_FIRNAM, params);
		assertEquals(2, foundCv.size());
		for (CV cv : foundCv) {
			assertEquals(3, cv.getCertifications().size());
			assertEquals(1, cv.getExperiences().size());
			assertEquals(1, cv.getEducations().size());
		}
	}

	@Test
	public void auditListenerTest() {
		CV cv = createCV();

		crudDao.create(cv);

		crudDao.find(cv.getId(), CV.class);

		cv.setFileName("xxx.doc");

		crudDao.save(cv);
		em.flush();

		assertNotNull(cv.getCreationDate());
		assertNotNull(cv.getLastUpdateDate());
		assertNotNull(cv.getCreationUser());
	}

	@Test
	public void cascadeTest() {
		// persist a CV with educations, experiences, certifications
		CV cv = createCV();
		crudDao.create(cv);

		// fetch the CV, modify 1st experience and remove certifications.
		CV fetchedCV = crudDao.find(cv.getId(), CV.class);

		// modify an experience of the CV
		fetchedCV.getExperiences().iterator().next().setCompanyName("newCompany");

		// remove educations 
		fetchedCV.getEducations().clear();
		em.merge(cv);

		// expect that the experience is NOT modified ( merge did not cascade to Experiences ) 
		CV refetch = crudDao.find(cv.getId(), CV.class);
		String newCompany = refetch.getExperiences().iterator().next().getCompanyName();
		assertEquals("newCompany", newCompany);

		// expect educations is cleared;
		assertEquals(0, refetch.getEducations().size());
	}

	/**
	 * this test emulates behavior of reading an object serializing it to the
	 * UI, de-serializing it and passing it to the back end.
	 * 
	 * Expected that the relations which have not been loaded (or null in the
	 * serialized form ) will
	 * not trigger deletion of relationship in the DB.
	 */
	@Test
	public void retrieveWithNoChildren_doesNotDeleteChildrenOnMerge() {
		// persist a CV with educations, experiences, certifications
		CV cv = createCV();
		crudDao.create(cv);

		// fetch the CV and make sure experiences and educations have not been loaded;
		CV fetchedCV = crudDao.find(cv.getId(), CV.class);

		assertFalse(isinitialized(fetchedCV.getEducations()));
		assertFalse(isinitialized(fetchedCV.getExperiences()));

		em.getTransaction().commit();
		em.getTransaction().begin();

		// fetchedCV is detached 
		crudDao.save(fetchedCV);

		em.getTransaction().commit();
		em.getTransaction().begin();

		assertRelationsKept(cv.getId());

	}

	@Test
	@Ignore
	// hibernate is not able to cope with this. It will initialize all existing children when adding a new child
	public void addChildren_doesNotInitializeCollection() {
		// persist a CV with educations, experiences, certifications
		CV cv = createCV();
		crudDao.create(cv);

		CV fetchedCV = crudDao.find(cv.getId(), CV.class);

		assertFalse(isinitialized(fetchedCV.getEducations()));
		assertFalse(isinitialized(fetchedCV.getExperiences()));

		fetchedCV.addExperience(createExperience("newExp"));
		assertFalse(isinitialized(fetchedCV.getExperiences()));

		fetchedCV.addEducation(createEducation("newEdu"));
		assertFalse(isinitialized(fetchedCV.getEducations()));
	}

	@Test
	@Ignore
	// hibernate is not able to cope with this. it will eager load some children, and lazy load others. The behavior is not consistent
	public void eagerLoadChildrenOnSave() {
		CV cv = crudDao.create(createCV());
		assertChildrenAreEagerLoaded(cv);
	}

	@Test
	@Ignore
	// hibernate is not able to cope with this
	public void lazyLoadChildrenOnFind() {
		crudDao.create(createCV());
		CV cvs = crudDao.find(1L, CV.class);
		assertChildrenAreLazyLoaded(cvs);
	}

	private void assertRelationsKept(Long cvId) {
		//expect relations are still kept 
		CV refetchedCV = crudDao.find(cvId, CV.class);

		assertEquals(3, refetchedCV.getCertifications().size());
		assertEquals(1, refetchedCV.getEducations().size());
		assertEquals(1, refetchedCV.getExperiences().size());
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
		cv.addEducation(createEducation("some university"));
		cv.setFileName("X.doc");
		cv.setRawFile(new byte[] { 12, 31, 4, 22 });
		cv.setFirstName("Ghita");
		cv.setLastName("Popescu");
		return cv;
	}

	private Education createEducation(String string) {
		Education edu = new Education();
		edu.setHighSchool("some high school");
		edu.setUniversity(string);
		return edu;
	}

	private Experience createExperience(String string) {
		Experience e = new Experience();

		e.setCompanyType(CompanyType.AEROSPACE);
		e.setEndDate(new Date());
		e.setStartDate(new Date());

		e.addProject(createProject());
		e.addProject(createProject());
		e.addProject(createProject());

		return e;
	}

	private Project createProject() {
		Project p = new Project();
		p.setActivity("activity");
		p.setDescription("desc");
		p.setName("name");
		p.setRole("role");
		return p;
	}

	private Certification createCertification(String name) {
		Certification cert = new Certification();
		cert.setDateObtained(new Date());
		cert.setName(name);
		return cert;
	}

}
