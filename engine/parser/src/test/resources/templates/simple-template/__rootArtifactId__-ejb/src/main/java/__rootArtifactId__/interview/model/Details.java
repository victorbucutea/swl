#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.interview.model;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

import ${package}.${rootArtifactId}.base.model.Entity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@Table(name = "DETAILS")
public class Details extends Entity {

	private String description;	

	private String description2;

	@OneToOne
	@JoinColumn(name = "CV_ID")
	@JsonBackReference
	private CV cv;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public void setCv(CV cv2) {
		this.cv = cv2;
	}

}
