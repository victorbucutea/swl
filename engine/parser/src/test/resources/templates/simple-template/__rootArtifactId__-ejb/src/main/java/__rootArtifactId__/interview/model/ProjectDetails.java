#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.interview.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

import ${package}.${rootArtifactId}.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@Table(name = "PROJECT_DETAILS")
public class ProjectDetails extends Entity {

	@Column(name = "DETAIL1")
	private String projectDetail1;

	@Column(name = "DETAIL2")
	private String projectDetail2;

	@OneToOne
	@JoinColumn(name = "CV_ID")
	@JsonBackReference
	private Project project;

	public String getProjectDetail1() {
		return projectDetail1;
	}

	public void setProjectDetail1(String projectDetail1) {
		this.projectDetail1 = projectDetail1;
	}

	public String getProjectDetail2() {
		return projectDetail2;
	}

	public void setProjectDetail2(String projectDetail2) {
		this.projectDetail2 = projectDetail2;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
