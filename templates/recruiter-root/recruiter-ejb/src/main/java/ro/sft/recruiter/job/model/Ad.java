/**
 * 
 */
package ro.sft.recruiter.job.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.sft.recruiter.base.model.Entity;

/**
 */
@javax.persistence.Entity
@NamedQuery(name = Ad.ALL, query = "Select j from Ad j")
@Table(name = "AD")
public class Ad extends Entity {

	private static final long serialVersionUID = -5021571760902968764L;

	public static final String ALL = "Ad.all";

	@Column(name = "TITLE")
	private String title;

	@Column(name = "TEXT")
	private String text;

	/** 
	 */
	@Column(name = "CONFIRMATION_STATUS")
	private ConfirmationStatus confirmationStatus;

	/** 
	 */
	@Column(name = "ONLINE_STATUS")
	private OnlineStatus onlineStatus;

	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "JOB_ID")
	private Job job;

	public ConfirmationStatus getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}