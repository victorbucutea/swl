#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.recruiter.interview.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import ${package}.recruiter.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@NamedQuery(name = Project.ALL, query = "Select j from Project j")
@Table(name = "PROJECT")
public class Project extends Entity {

	public static final String ALL = "Project.All";

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ACTIVITY")
	private String activity;

	@Column(name = "ROLE")
	private String role;

	@OneToOne(cascade = { CascadeType.ALL })
	@JsonManagedReference
	private ProjectDetails details;

	@ManyToOne
	@JoinColumn(name = "EXPERIENCE_ID")
	@JsonBackReference
	private Experience experience;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Experience getExperience() {
		return experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

	public ProjectDetails getDetails() {
		return details;
	}

	public void setDetails(ProjectDetails details) {
		this.details = details;
		if (details != null) {
			details.setProject(this);
		}
	}

}
