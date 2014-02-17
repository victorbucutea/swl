#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.test.job.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ${package}.${rootArtifactId}.base.dao.CrudDaoBean;
import ${package}.${rootArtifactId}.job.model.Ad;
import ${package}.${rootArtifactId}.job.model.ConfirmationStatus;
import ${package}.${rootArtifactId}.job.model.Job;
import ${package}.${rootArtifactId}.job.model.OnlineStatus;
import ${package}.${rootArtifactId}.test.base.DaoTest;
import ${package}.${rootArtifactId}.test.base.TestedObject;

public class CrudDaoTest extends DaoTest {

	@TestedObject
	private CrudDaoBean<Job> crudDao = new CrudDaoBean<Job>();

	@Override
	@Test
	public void delete() {
		Job j = new Job();
		j.setTitle("New job");
		crudDao.create(j);
		crudDao.delete(j);
		assertNull(crudDao.find(j.getId(), Job.class));
	}

	@Override
	@Test
	public void findByNamedQuery() {
		Job j = new Job();
		j.setTitle("New job");
		crudDao.create(j);
		Job j1 = new Job();
		j1.setTitle("New job 1");
		crudDao.create(j1);
		List<Job> jobs = crudDao.findByNamedQuery(Job.ALL);
		assertEquals(2, jobs.size());
	}

	@Override
	@Test
	public void save() {
		Job j = new Job();
		j.setTitle("New job");
		Ad ad1 = new Ad();
		ad1.setConfirmationStatus(ConfirmationStatus.MANAGER_CONFIRMED);
		ad1.setOnlineStatus(OnlineStatus.PUBLISHED);
		ad1.setJob(j);
		Set<Ad> ads = new HashSet<Ad>();
		ads.add(ad1);
		j.setAds(ads);

		crudDao.create(j);
		assertNotNull(crudDao.find(j.getId(), Job.class));
	}

	@Override
	@Test
	public void find() {
		// TODO Auto-generated method stub

	}
}
