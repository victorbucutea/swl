package ro.sft.recruiter.cv;

import java.util.List;

import javax.ejb.Local;

import ro.sft.recruiter.base.dao.SearchInfo;
import ro.sft.recruiter.interview.model.CV;

@Local
public interface CvService {

	public CV saveCV(CV cv);

	public List<CV> getAll();

	public CV find(Long id);

	public List<CV> search(SearchInfo searcher);

	public void sendCVForValidation(CV cv);

	public void feedbackFor(CV cv);

}