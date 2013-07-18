package ro.sft.recruiter.cv;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ro.sft.recruiter.base.dao.CrudDao;
import ro.sft.recruiter.base.dao.SearchInfo;
import ro.sft.recruiter.interview.model.CV;

@Stateless
public class CvServiceBean implements CvService {

	@EJB
	private CrudDao<CV> dao;

	@Override
	public CV saveCV(CV cv) {
		/*
		 * Incoming entity is unmanaged so we cannot simply merge it into the
		 * context. Child entities which are not present will be assumed by the
		 * entity manager as 'deleted', and what happened in fact was just that
		 * the UI ( or whatever client ) did not have that information to start
		 * with.
		 * Management is done on reference level so the only solution to map an
		 * incoming information into a managed entity is to copy deserialized
		 * data into 'managed data'.
		 * 
		 * The copy operation has no effect in case the entity which was passed
		 * in is already a managed entity. It will simply copy the new values
		 * (if there were any updates) over the existing ones.
		 */
		CV managedCv = find(cv.getId());
		managedCv.merge(cv);
		return dao.save(managedCv);
	}

	@Override
	public List<CV> getAll() {
		return dao.findByNamedQuery(CV.ALL);
	}

	@Override
	public CV find(Long id) {
		return dao.find(id, CV.class);
	}

	@Override
	public List<CV> search(SearchInfo searcher) {
		return dao.findByNamedQuery(searcher.getSearcherId(), searcher.getParamMap());
	}

	@Override
	public void sendCVForValidation(CV cv) {
	}

	@Override
	public void feedbackFor(CV cv) {
	}

}
