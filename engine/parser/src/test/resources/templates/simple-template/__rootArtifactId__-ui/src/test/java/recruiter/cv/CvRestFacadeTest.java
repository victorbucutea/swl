#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.recruiter.cv;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static ${package}.recruiter.base.util.PersistenceUtil.isinitialized;
import static ${package}.recruiter.base.util.ReflectionUtil.injectIntoField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Before;
import org.junit.Test;

import ${package}.recruiter.base.dao.CrudDaoBean;
import ${package}.recruiter.base.util.ReflectionUtil;
import ${package}.recruiter.interview.model.CV;
import ${package}.recruiter.interview.model.CVStatus;
import ${package}.recruiter.interview.model.Certification;
import ${package}.recruiter.interview.model.CompanyType;
import ${package}.recruiter.interview.model.Details;
import ${package}.recruiter.interview.model.Education;
import ${package}.recruiter.interview.model.Experience;
import ${package}.recruiter.interview.model.Project;
import ${package}.recruiter.interview.model.ProjectDetails;
import ${package}.recruiter.test.base.DaoTest;
import ${package}.recruiter.test.base.TestedObject;

import com.google.common.collect.Ordering;

public class CvRestFacadeTest extends DaoTest {

	private Dispatcher dispatcher;

	@TestedObject
	private CrudDaoBean<CV> cvDao = new CrudDaoBean<CV>();

	private CvService cvService = new CvServiceBean();

	private CvRestFacade cvRestFacade = new CvRestFacade();

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void initDispatcher() {
		dispatcher = MockDispatcherFactory.createDispatcher();
		dispatcher.getRegistry().addSingletonResource(cvRestFacade);
	}

	@Before
	public void setUpService() {
		ReflectionUtil.injectIntoField(cvService, "dao", cvDao);
		ReflectionUtil.injectIntoField(cvRestFacade, "cvService", cvService);
	}

	@Test
	public void getAllCvs() throws IOException, URISyntaxException {
		cvDao.save(createCV("Ghita"));
		cvDao.save(createCV("Ghita"));
		String reqUri = "cvs/";
		MockHttpResponse response = invokeGetUri(reqUri);
		assertFirstTwoNames("Ghita", response);
	}

	@Test
	public void getCv() throws IOException, URISyntaxException {
		cvDao.create(createCV("Ghita"));
		String reqUri = "cvs/1";
		MockHttpResponse response = invokeGetUri(reqUri);
		assertName("Ghita", response);
	}

	@Test
	public void findWithSearcher() throws IOException, URISyntaxException {
		cvDao.create(createCV("Ghita"));
		cvDao.create(createCV("Ghita"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("Dorel"));
		String reqStr = "cvs/search_" + CV.FIRST_NAME + "?firstName=Ghita";
		MockHttpResponse response = invokeGetUri(reqStr);
		assertFirstTwoNames("Ghita", response);

		String reqUri = "cvs/search_" + CV.FIRST_NAME + "?firstName=George";
		response = invokeGetUri(reqUri);
		assertFirstTwoNames("George", response);

	}

	@Test
	public void findWithSearcher_eagerLoadChildren() throws IOException, URISyntaxException {
		cvDao.create(createCV("Grigore"));
		cvDao.create(createCV("Ghita"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("Dorel"));

		String reqStr = "cvs/search_" + CV.EXP_EDU_CER_FIRNAM + "?firstName=George";
		MockHttpResponse response = invokeGetUri(reqStr);
		assertFirstTwoNames("George", response);
		assertEducationsSerialized(response);
		assertCertificationsSerialized(response);
		assertExperiencesSerialized(response);

	}

	@Test
	public void serialize_eagerLoaded_skip_lazyLoaded() throws IOException, URISyntaxException {
		cvDao.create(createCV("Grigore"));
		cvDao.create(createCV("Ghita"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("George"));
		cvDao.create(createCV("Dorel"));

		String reqStr = "cvs/search_" + CV.EXP_CERT_FIRNAM + "?firstName=George";
		MockHttpResponse response = invokeGetUri(reqStr);
		assertFirstTwoNames("George", response);
		assertEducationsNotSerialized(response);
		assertCertificationsSerialized(response);
		assertExperiencesSerialized(response);
	}

	@Test
	public void leftJoinProperlySerialized() throws URISyntaxException, IOException {
		CV grigore = createCV("Grigore");
		grigore.getCertifications().clear();
		cvDao.create(grigore);

		CV ghita = createCV("Ghita");
		ghita.getExperiences().clear();
		cvDao.create(ghita);

		cvDao.create(createCV("George"));

		String reqStr = "cvs/search_" + CV.EXP_EDU_CER_FIRNAM + "?firstName=Grigore";
		MockHttpResponse response = invokeGetUri(reqStr);
		assertEducationsSerialized(response);
		assertExperiencesSerialized(response);
		assertCertificationsNotSerialized(response);

		reqStr = "cvs/search_" + CV.EXP_EDU_CER_FIRNAM + "?firstName=Ghita";
		response = invokeGetUri(reqStr);
		assertExperiencesNotSerialized(response);
		assertCertificationsSerialized(response);
		assertEducationsSerialized(response);

		reqStr = "cvs/search_" + CV.EXP_EDU_CER_FIRNAM + "?firstName=George";
		response = invokeGetUri(reqStr);
		assertExperiencesSerialized(response);
		assertCertificationsSerialized(response);
		assertEducationsSerialized(response);

	}

	@Test
	public void serializeEnum() throws URISyntaxException, IOException, ScriptException {
		CV grigore = createCV("Grigore");
		CV deserializedCv = serializeAndDeserialize(grigore);

		assertEquals(CVStatus.UNREAD, deserializedCv.getStatus());
		assertEquals(CVStatus.OUTDATED, deserializedCv.getPreviousStatus());
		assertEnumValuesSerialized(new ObjectMapper().writeValueAsString(grigore));
	}

	@Test
	public void serializeNullEnum() throws URISyntaxException, IOException, ScriptException {
		CV grigore = createCV("Grigore");
		grigore.setStatus(null);
		assertEnumValuesSerialized(new ObjectMapper().writeValueAsString(grigore));
	}

	@Test
	public void serializeDateAndCalendar() throws IOException {
		CV grigore = createCV("Grigore");
		Date expectedStartDate = grigore.getExperiences().iterator().next().getStartDate();
		Date expectedEndDate = grigore.getExperiences().iterator().next().getEndDate();
		Calendar expected = GregorianCalendar.getInstance();
		grigore.setCal(expected);

		CV desCv = serializeAndDeserialize(grigore);
		Experience next = desCv.getExperiences().iterator().next();
		assertTrue(expected.equals(desCv.getCal()));
		assertTrue(expectedStartDate.equals(next.getStartDate()));
		assertTrue(expectedEndDate.equals(next.getEndDate()));
	}

	@Test
	public void fetchedEntityWithNoLazyLoad_DoesNotCascadeChildDeletion() throws URISyntaxException, IOException {
		CV grigore = createCV("Ion");
		cvDao.create(grigore);

		String reqStr = "cvs/" + grigore.getId();
		MockHttpResponse getResponse = invokeGetUri(reqStr);

		// persist the same cv graph, with lazy loaded entities not initialized 
		// particularly educations and experiences should not be loaded (they will be null in the json string )
		reqStr = "cvs/";
		invokePostUri(reqStr, getResponse.getContentAsString());

		assertAllRelationsAreKept();

	}

	@Test
	public void fetchedEntityWithLazyLoadAndEagerLoad_cascadeChildDeletion() throws URISyntaxException {

		// delete all initialized entities 

		// check non-initialized entities not deleted 
	}

	@Test
	public void mappingBackIntoManagedObject_orphanRemoval() throws URISyntaxException, IOException, ScriptException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with all children loaded
		CV deserializedCv = serializeAndDeserialize(cv);

		// remove experiences in order to have orphan Projects 
		for (Experience exp : deserializedCv.getExperiences()) {
			injectIntoField(exp, "deleted", Boolean.TRUE);
		}

		// merge the managed entity with the deserialized one
		cv.merge(deserializedCv);

		// merge deserializedCv into context
		cvDao.save(cv);

		assertOrphanProjectsAreRemoved();
	}

	private void assertOrphanProjectsAreRemoved() {

		Query query = em.createNamedQuery(Project.ALL);

		List<Project> resultList = query.getResultList();

		assertEquals(0, resultList.size());
	}

	@Test
	public void mappingBackIntoManagedObject_keepsChildrenRelations() throws URISyntaxException, IOException,
			ScriptException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		// merge the managed entity with the deserialized one
		cv.merge(deserializedCv);

		// merge deserializedCv into context
		cvDao.save(cv);

		assertAllRelationsAreKept();
	}

	@Test
	public void mappingBackIntoManagedObject_updatesAllFields() throws URISyntaxException, IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);
		updateFields(deserializedCv);

		// merge the managed entity with the deserialized one
		cv.merge(deserializedCv);

		// merge deserializedCv into context
		cvDao.save(cv);

		assertAllFieldsUpdated(cv.getId());
	}

	@Test
	public void mappingBackIntoManagedObject_updatesChildrenValues() throws IOException, ParseException {

		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with experiences and certifications loaded
		CV deserializedCv = serializeAndDeserialize(cv);
		updateChildren(deserializedCv);

		// merge the managed entity with the de-serialized one and save the merged entity
		cvRestFacade.saveCv(deserializedCv);

		assertAllChildrenUpdated(cv.getId());
	}

	@Test
	public void mappingBackIntoManagedObject_mappingDoesNotTriggerLazyLoadForNonUpdatedFields() throws IOException,
			ParseException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		// load all children except educations
		cv.getCertifications().size();
		cv.getExperiences().size();
		cv.getExperiences().iterator().next().getProjects().size();

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		// this will not update certifications because there shouldn't be any in the serialized form
		updateChildren(deserializedCv);

		// merge the managed entity with the de-serialized one and save the de-serialized entity
		cvRestFacade.saveCv(deserializedCv);

		assertTrue(isinitialized(cv.getCertifications()));
		assertTrue(isinitialized(cv.getExperiences()));
		assertTrue(isinitialized(cv.getExperiences().iterator().next().getProjects()));
		assertFalse(isinitialized(cv.getEducations()));
	}

	@Test
	public void mappingBackIntoManagedObject_addsNewRelations() throws IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		deserializedCv.addCertification(createCertification("addedCertification"));
		deserializedCv.addExperience(createExperience("addedExperience"));
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationPresent(fetchedCv, "addedCertification");
		assertExperiencePresent(fetchedCv, "addedExperience");
		assertEducationPresent(fetchedCv, "addedEducation");

	}

	@Test
	public void mappingBackIntoManagedObject_removeOld_addNewRelations() throws IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		CV deserializedCv = serializeAndDeserialize(cv);

		Certification firstCert = deserializedCv.getCertifications().iterator().next();
		injectIntoField(firstCert, "deleted", Boolean.TRUE);
		deserializedCv.addCertification(createCertification("addedCertification"));

		Experience firstExp = deserializedCv.getExperiences().iterator().next();
		injectIntoField(firstExp, "deleted", Boolean.TRUE);
		deserializedCv.addExperience(createExperience("addedExperience"));

		Education firstEducation = deserializedCv.getEducations().iterator().next();
		injectIntoField(firstEducation, "deleted", Boolean.TRUE);
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationPresent(fetchedCv, "addedCertification");
		assertCertificationNotPresent(fetchedCv, firstCert.getName());
		assertEquals(3, fetchedCv.getCertifications().size());

		assertExperiencePresent(fetchedCv, "addedExperience");
		assertExperienceNotPresent(fetchedCv, firstExp.getCompanyName());
		assertEquals(1, fetchedCv.getExperiences().size());

		assertEducationPresent(fetchedCv, "addedEducation");
		assertEducationNotPresent(fetchedCv, firstEducation.getUniversity());
		assertEquals(1, fetchedCv.getEducations().size());

	}

	@Test
	public void mappingBackIntoManagedObject_removeOld_addNewRelations_inUnitializedCollections() throws IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		// some collections are not loaded

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		Certification firstCert = deserializedCv.getCertifications().iterator().next();
		injectIntoField(firstCert, "deleted", Boolean.TRUE);
		deserializedCv.addCertification(createCertification("addedCertification"));
		deserializedCv.addExperience(createExperience("addedExperience"));
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationPresent(fetchedCv, "addedCertification");
		assertCertificationNotPresent(fetchedCv, firstCert.getName());
		assertEquals(3, fetchedCv.getCertifications().size());
		assertExperiencePresent(fetchedCv, "addedExperience");

		Set<Experience> experiences = fetchedCv.getExperiences();
		assertEquals(2, experiences.size());
		for (Experience exp : experiences) {
			assertEquals(3, exp.getProjects().size());
			for (Project p : exp.getProjects()) {
				assertNotNull(p.getDetails());
				assertEquals("detail1", p.getDetails().getProjectDetail1());
			}
		}
		assertEducationPresent(fetchedCv, "addedEducation");
		assertEducationPresent(fetchedCv, "some university");

	}

	@Test
	public void mappingBackIntoManagedObject_addsNewRelations_inUnitializedCollections() throws IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		// some collections are not loaded

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		deserializedCv.addCertification(createCertification("addedCertification"));
		deserializedCv.addExperience(createExperience("addedExperience"));
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationPresent(fetchedCv, "addedCertification");
		assertEquals(4, fetchedCv.getCertifications().size());
		assertExperiencePresent(fetchedCv, "addedExperience");
		assertEquals(2, fetchedCv.getExperiences().size());
		assertEducationPresent(fetchedCv, "addedEducation");
		assertEquals(2, fetchedCv.getEducations().size());

	}

	@Test
	public void mappingBackIntoManagedObject_deleteOneToOne() throws URISyntaxException, IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		// some collections are not loaded

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		deserializedCv.setDetails(null);

		deserializedCv.addCertification(createCertification("addedCertification"));
		deserializedCv.addExperience(createExperience("addedExperience"));
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationPresent(fetchedCv, "addedCertification");
		assertExperiencePresent(fetchedCv, "addedExperience");
		assertEducationPresent(fetchedCv, "addedEducation");
		assertDetailsNotPresent(fetchedCv);
	}

	@Test
	public void mappingBackIntoManagedObject_deleteOneToMany() throws URISyntaxException, IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		// some collections are not loaded

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		// delete one to many is done by removing all child entities
		Set<Certification> certifications = deserializedCv.getCertifications();
		for (Certification cert : certifications) {
			injectIntoField(cert, "deleted", Boolean.TRUE);
		}

		deserializedCv.addExperience(createExperience("addedExperience"));
		deserializedCv.addEducation(createEducation("addedEducation"));

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationNotPresent(fetchedCv, "addedCertification");
		assertEquals(0, fetchedCv.getCertifications().size());
		assertExperiencePresent(fetchedCv, "addedExperience");
		assertEquals(2, fetchedCv.getExperiences().size());
		assertEducationPresent(fetchedCv, "addedEducation");
		assertEquals(2, fetchedCv.getEducations().size());
	}

	@Test
	public void mappingBackIntoManagedObject_deleteOneToMany_updateChildren() throws URISyntaxException, IOException,
			ParseException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		// delete one to many is done by removing all child entities
		Set<Certification> certifications = deserializedCv.getCertifications();
		for (Certification cert : certifications) {
			injectIntoField(cert, "deleted", Boolean.TRUE);
		}
		updateChildren(deserializedCv);

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertCertificationNotPresent(fetchedCv, "addedCertification");
		assertEquals(0, fetchedCv.getCertifications().size());
		assertExperiencePresent(fetchedCv, "newCompany");
		assertEquals(1, fetchedCv.getExperiences().size());
		assertEducationPresent(fetchedCv, "newUniversity");
		assertEquals(1, fetchedCv.getEducations().size());
	}

	@Test
	public void mappingBackIntoManagedObject_deleteChildOneToMany() throws URISyntaxException, IOException {
		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		Set<Project> ${parentArtifactId}s = deserializedCv.getExperiences().iterator().next().getProjects();
		for (Project p : ${parentArtifactId}s) {
			injectIntoField(p, "deleted", Boolean.TRUE);
		}

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertEquals(3, fetchedCv.getCertifications().size());
		assertEquals(1, fetchedCv.getExperiences().size());
		assertEquals(0, fetchedCv.getExperiences().iterator().next().getProjects().size());
		assertEquals(1, fetchedCv.getEducations().size());

	}

	@Test
	public void mappingBackIntoManagedObject_deleteChildOneToOne() throws URISyntaxException, IOException {

		// persist a CV with educations, experiences, certifications
		CV cv = cvDao.create(createCV("Ion"));
		loadAllChildren(cv);

		// deserializedCv is a detached object with experiences and certifications null
		CV deserializedCv = serializeAndDeserialize(cv);

		deserializedCv.addExperience(createExperience("newExp"));
		Set<Experience> rawExperiences = deserializedCv.getExperiences();
		for (Experience exp : rawExperiences) {
			for (Project p : exp.getProjects()) {
				p.setDetails(null);
			}
		}

		cvRestFacade.saveCv(deserializedCv);

		CV fetchedCv = cvDao.find(cv.getId(), CV.class);
		assertEquals(3, fetchedCv.getCertifications().size());
		Set<Experience> experiences = fetchedCv.getExperiences();
		assertEquals(2, experiences.size());
		for (Experience exp : experiences) {
			assertEquals(3, exp.getProjects().size());
			for (Project p : exp.getProjects()) {
				assertNull(p.getDetails());
			}
		}
		assertEquals(1, fetchedCv.getEducations().size());
	}

	@Override
	@Test
	public void delete() {

	}

	@Override
	@Test
	public void save() {

	}

	private void loadAllChildren(CV cv) {
		cv.getCertifications().size();
		cv.getExperiences().size();
		cv.getExperiences().iterator().next().getProjects().size();
		cv.getEducations().size();
	}

	private void assertDetailsNotPresent(CV cv) {
		assertNull(cv.getDetails());
	}

	private void assertEducationNotPresent(CV cv, String string) {
		assertFalse(educationWithNameExists(string, cv));
	}

	private void assertEducationPresent(CV cv, String string) {
		assertTrue(educationWithNameExists(string, cv));
	}

	private boolean educationWithNameExists(String string, CV cv) {
		for (Education edu : cv.getEducations()) {
			if (string.equals(edu.getUniversity())) {
				return true;
			}
		}
		return false;
	}

	private void assertExperienceNotPresent(CV cv, String string) {
		assertFalse(expWithCompanyNameExists(string, cv));
	}

	private void assertExperiencePresent(CV cv, String string) {
		assertTrue(expWithCompanyNameExists(string, cv));
	}

	private boolean expWithCompanyNameExists(String string, CV cv) {
		for (Experience cert : cv.getExperiences()) {
			if (string.equals(cert.getCompanyName())) {
				return true;
			}
		}
		return false;
	}

	private void assertCertificationNotPresent(CV cv, String name) {
		assertFalse(certWithNameExists(name, cv));
	}

	private void assertCertificationPresent(CV cv, String name) {
		assertTrue(certWithNameExists(name, cv));
	}

	private boolean certWithNameExists(String name, CV cv) {
		for (Certification cert : cv.getCertifications()) {
			if (name.equals(cert.getName())) {
				return true;
			}
		}
		return false;
	}

	private void assertAllChildrenUpdated(Long id) throws ParseException {
		CV cv = cvDao.find(id, CV.class);
		Set<Certification> certifications = cv.getCertifications();

		for (Certification cert : certifications) {
			assertEquals(parseDate("15-12-2012", "dd-MM-yyyy"), cert.getDateObtained());
		}

		Set<Education> educations = cv.getEducations();
		for (Education edu : educations) {
			assertEquals("newHighSchool", edu.getHighSchool());
			assertEquals(parseDate("15-12-2012", "dd-MM-yyyy"), edu.getEndDate());
		}

		Collection<Experience> experiences = cv.getExperiences();
		for (Experience exp : experiences) {
			Set<Project> prjs = exp.getProjects();
			for (Project prj : prjs) {
				assertEquals("newActivity", prj.getActivity());
				assertEquals("newName", prj.getName());
			}

			assertEquals("newCompany", exp.getCompanyName());
			assertEquals(parseDate("15-12-2012", "dd-MM-yyyy"), exp.getEndDate());
		}
	}

	private Date parseDate(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}

	private void updateChildren(CV deserializedCv) throws ParseException {
		Set<Certification> certifications = deserializedCv.getCertifications();
		if (certifications != null) {
			for (Certification cert : certifications) {
				cert.setDateObtained(parseDate("15-12-2012", "dd-MM-yyyy"));
			}
		}

		Set<Education> educations = deserializedCv.getEducations();
		if (educations != null) {
			for (Education edu : educations) {
				edu.setHighSchool("newHighSchool");
				edu.setUniversity("newUniversity");
				edu.setEndDate(parseDate("15-12-2012", "dd-MM-yyyy"));
			}
		}

		Collection<Experience> experiences = deserializedCv.getExperiences();
		if (experiences != null) {
			for (Experience exp : experiences) {
				Set<Project> prjs = exp.getProjects();
				for (Project prj : prjs) {
					prj.setActivity("newActivity");
					prj.setName("newName");
				}

				exp.setCompanyName("newCompany");
				exp.setEndDate(parseDate("15-12-2012", "dd-MM-yyyy"));
			}
		}
	}

	private void assertAllFieldsUpdated(Long id) {
		CV cv = cvDao.find(id, CV.class);
		assertEquals("UPDATED", cv.getCreationUser());
		assertEquals("updated.doc", cv.getFileName());
		assertArrayEquals(new byte[] { 12, 24, 43, 23 }, cv.getRawFile());
		assertEquals(CVStatus.OUTDATED, cv.getStatus());
		assertNull(cv.getPreviousStatus());
		assertNotNull(cv.getCal());
		assertArrayEquals(cv.getRawChars(), new Character[] { 'a', 'b' });
		assertArrayEquals(cv.getRawInts(), new int[] { 'a', 'b' });
		assertArrayEquals(cv.getRawString(), new String[] { "a", "b" });
		Details details = cv.getDetails();
		assertEquals("updated1", details.getDescription());
		assertEquals("updated2", details.getDescription2());
	}

	private void updateFields(CV deserializedCv) {
		deserializedCv.setCreationUser("UPDATED");
		deserializedCv.setFileName("updated.doc");
		deserializedCv.setRawFile(new byte[] { 12, 24, 43, 23 });
		deserializedCv.setPreviousStatus(null);
		deserializedCv.setCal(Calendar.getInstance());
		deserializedCv.setRawChars(new Character[] { 'a', 'b' });
		deserializedCv.setRawInts(new int[] { 'a', 'b' });
		deserializedCv.setRawString(new String[] { "a", "b" });
		Details details = deserializedCv.getDetails();
		details.setDescription("updated1");
		details.setDescription2("updated2");
		deserializedCv.setDetails(details);
		deserializedCv.setStatus(CVStatus.OUTDATED);
	}

	private void assertAllRelationsAreKept() throws URISyntaxException, IOException {
		// search again for this id, and expect relations were kept ( educations and experiences were not removed )
		String reqStr = "cvs/search_" + CV.EXP_EDU_CER_FIRNAM + "?firstName=Ion";
		MockHttpResponse getResponse = invokeGetUri(reqStr);

		// assert cv has 3 certifications 
		assertCertificationsSerialized(getResponse);

		// assert cv has 1 experience
		assertExperiencesSerialized(getResponse);

		// assert cv has 1 education
		assertEducationsSerialized(getResponse);
	}

	private CV serializeAndDeserialize(CV cv) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		mapper.writeValue(buffer, cv);
		System.out.println(" Serialized :" + new String(buffer.toByteArray()));
		CV deserializedCv = mapper.readValue(buffer.toByteArray(), CV.class);
		return deserializedCv;
	}

	private void assertEnumValuesSerialized(String serializedCv) throws ScriptException {
		String script = " var obj = " + serializedCv + " ;";
		script += "var enumValues = obj.statuses;";
		script += "var val1 = enumValues[0];";
		script += "var val2 = enumValues[1];";
		script += "var val3 = enumValues[2];";

		script += "var enumValues = obj.previousStatuses;";
		script += "var pval1 = enumValues[0];";
		script += "var pval2 = enumValues[1];";
		script += "var pval3 = enumValues[2];";
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.eval(script);
		assertEquals(CVStatus.READ.name(), engine.get("val1"));
		assertEquals(CVStatus.UNREAD.name(), engine.get("val2"));
		assertEquals(CVStatus.OUTDATED.name(), engine.get("val3"));

		assertEquals(CVStatus.READ.name(), engine.get("pval1"));
		assertEquals(CVStatus.UNREAD.name(), engine.get("pval2"));
		assertEquals(CVStatus.OUTDATED.name(), engine.get("pval3"));
	}

	private MockHttpResponse invokeGetUri(String reqUri) throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.get(reqUri);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		assertResponseOK(response);
		System.out.println("response :" + response.getContentAsString());
		return response;
	}

	private MockHttpResponse invokePostUri(String reqUri, String payload) throws URISyntaxException {
		MockHttpRequest request = MockHttpRequest.post(reqUri);
		request.setInputStream(toInputStream(payload));
		request.contentType(MediaType.APPLICATION_JSON);
		MockHttpResponse response = new MockHttpResponse();
		dispatcher.invoke(request, response);
		assertResponseOK(response);
		return response;
	}

	private void assertName(String name, MockHttpResponse response) throws IOException {
		CV deserialized = mapper.readValue(response.getContentAsString(), CV.class);
		assertEquals(name, deserialized.getFirstName());
	}

	private void assertFirstTwoNames(String name, MockHttpResponse response) throws IOException {

		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);
		assertEquals(name, deserialized[0].getFirstName());
		assertEquals(name, deserialized[1].getFirstName());
	}

	private void assertExperiencesNotSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);
		assertEquals(0, deserialized[0].getExperiences().size());
	}

	private void assertExperiencesSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);

		Experience firstExp = deserialized[0].getExperiences().iterator().next();
		assertEquals("Oracle", firstExp.getCompanyName());
		Set<Project> ${parentArtifactId}s = firstExp.getProjects();
		assertEquals("name", ${parentArtifactId}s.iterator().next().getName());
	}

	private void assertCertificationsNotSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);
		assertEquals(0, deserialized[0].getCertifications().size());
	}

	private void assertCertificationsSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);

		CV cv1 = deserialized[0];

		Ordering<Certification> ordering = new Ordering<Certification>() {
			@Override
			public int compare(Certification o1, Certification o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
		List<Certification> sortedCopy = ordering.sortedCopy(cv1.getCertifications());

		assertEquals("SCEA", sortedCopy.get(0).getName());
		assertEquals("SCJD", sortedCopy.get(1).getName());
		assertEquals("SCJP", sortedCopy.get(2).getName());
	}

	private void assertEducationsNotSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);
		assertEquals(0, deserialized[0].getEducations().size());
	}

	private void assertEducationsSerialized(MockHttpResponse response) throws IOException {
		CV[] deserialized = mapper.readValue(response.getContentAsString(), CV[].class);
		Education firstEdu = deserialized[0].getEducations().iterator().next();
		assertEquals("some university", firstEdu.getUniversity());
	}

	private void assertResponseOK(MockHttpResponse response) {
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}

	private CV createCV(String string) {
		CV cv = new CV();
		cv.addCertification(createCertification("SCEA"));
		cv.addCertification(createCertification("SCJP"));
		cv.addCertification(createCertification("SCJD"));
		cv.addExperience(createExperience("Oracle"));
		cv.addEducation(createEducation("some university"));
		cv.setDetails(createDetails());
		cv.setFileName("X.doc");
		cv.setRawFile(new byte[] { 12, 31, 4, 22 });
		cv.setFirstName(string);
		cv.setLastName("Popescu");
		cv.setStatus(CVStatus.UNREAD);
		cv.setPreviousStatus(CVStatus.OUTDATED);
		return cv;
	}

	private Details createDetails() {
		Details details = new Details();
		details.setDescription("x");
		details.setDescription2("desc2");
		return details;
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
		e.setCompanyName(string);
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

		ProjectDetails details = new ProjectDetails();
		details.setProjectDetail1("detail1");
		details.setProjectDetail2("detail2");
		details.setProject(p);
		p.setDetails(details);
		return p;
	}

	private Certification createCertification(String name) {
		Certification cert = new Certification();
		cert.setDateObtained(new Date());
		cert.setName(name);
		return cert;
	}

	@Override
	@Test
	public void find() {

	}

	@Override
	@Test
	public void findByNamedQuery() {
	}
}
