#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.job;

import java.util.List;

import ${package}.${rootArtifactId}.job.model.Job;

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