#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package}.${rootArtifactId}.job.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/** 
 * 
 */
@SuppressWarnings("serial")
@Entity
@NamedQuery(name = Job.ALL, query = "Select j from Job j")
@Table(name = "JOB")
public class Job extends ${package}.${rootArtifactId}.base.model.Entity {

	public static final String ALL = "Job.all";

	@Column(name = "TITLE")
	private String title;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
	private Set<Ad> ads;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Ad> getAds() {
		return ads;
	}

	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

}