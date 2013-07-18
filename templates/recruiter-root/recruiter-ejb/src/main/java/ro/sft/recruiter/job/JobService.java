package ro.sft.recruiter.job;

import java.util.List;

import ro.sft.recruiter.job.model.Job;

//@Local
public interface JobService {

	/**
	 * 
	 */
	public List<Job> getJobs();

	/**
	 */
	public void createJob(Job j);

	/**
	 * @return
	 */
	public List<Job> autosuggestSimilarJob(Job search);

	/**
	 */
	public void createAd();

	/**
	 */
	public void checkAllowedJobs();

	/**
	 */
	public void checkAllowedCreateJob();

	/**
	 */
	public void expireJob();

}