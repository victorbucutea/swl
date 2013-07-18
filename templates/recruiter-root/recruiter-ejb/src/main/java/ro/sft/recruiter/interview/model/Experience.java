package ro.sft.recruiter.interview.model;

import static ro.sft.recruiter.base.util.PersistenceUtil.isinitialized;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

import ro.sft.recruiter.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@NamedQuery(name = Experience.ALL, query = "Select j from Experience j")
@Table(name = "EXPERIENCE")
public class Experience extends Entity {

	public static final String ALL = "Experience.All";

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "COMPANY_TYPE")
	@Enumerated(EnumType.STRING)
	private CompanyType companyType;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@JsonManagedReference
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<Project> projects;

	@ManyToOne
	@JoinColumn(name = "CV_ID")
	@JsonBackReference
	private CV cv;

	public void addProject(Project p) {
		if (projects == null) {
			projects = new HashSet<Project>();
		}
		projects.add(p);
		p.setExperience(this);
	}

	public Set<Project> getProjects() {
		return projects;
	}

	@JsonProperty("projects")
	@JsonManagedReference
	public Set<Project> serializeProjects() {
		if (projects == null) {
			return new HashSet<Project>();
		}

		if (isinitialized(projects)) {
			return projects;
		}

		return new HashSet<Project>();
	}

	@JsonProperty("projects")
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
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

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "Experience [companyName=" + companyName + ", id =" + getId() + "]";
	}

}
