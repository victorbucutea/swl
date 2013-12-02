#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.interview.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;

import ${package}.${rootArtifactId}.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@NamedQuery(name = Certification.ALL, query = "Select j from Certification j")
@Table(name = "CERTIFICATIONS")
public class Certification extends Entity {

	public static final String ALL = "Certification.All";

	@Column(name = "NAME")
	private String name;

	@Column(name = "DATE_OBTAINED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateObtained;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "CV_ID")
	private CV cv;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateObtained() {
		return dateObtained;
	}

	public void setDateObtained(Date dateObtained) {
		this.dateObtained = dateObtained;
	}

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	@Override
	public String toString() {
		return "Certification [name=" + name + ", id=" + getId() + "]";
	}

}
