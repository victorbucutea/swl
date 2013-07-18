package ro.sft.recruiter.interview.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;

import ro.sft.recruiter.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@NamedQuery(name = Education.ALL, query = "Select j from Education j")
@Table(name = "EDUCATION")
public class Education extends Entity {

	public static final String ALL = "Education.All";

	@Column(name = "HIGH_SCHOOL")
	private String highSchool;

	@Column(name = "UNIVERSITY")
	private String university;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "CV_ID")
	private CV cv;

	public String getHighSchool() {
		return highSchool;
	}

	public void setHighSchool(String highSchool) {
		this.highSchool = highSchool;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setCv(CV cv2) {
		this.cv = cv2;
	}

	@Override
	public String toString() {
		return "Education [highSchool=" + highSchool + ", university=" + university + ", id=" + getId() + "]";
	}

}
