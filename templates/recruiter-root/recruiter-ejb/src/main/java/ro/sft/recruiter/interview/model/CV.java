/**
 * 
 */
package ro.sft.recruiter.interview.model;

import static com.google.common.collect.Sets.newHashSet;
import static ro.sft.recruiter.base.util.PersistenceUtil.isinitialized;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

import ro.sft.recruiter.base.model.AuditedEntity;


@SuppressWarnings("serial")
@javax.persistence.Entity
// @formatter:off
@NamedQueries(value = {
		@NamedQuery(name = CV.ALL, query = "Select j from CV j"),
		@NamedQuery(name = CV.FIRST_NAME, query = "Select j from CV j where j.firstName = :firstName"),
		@NamedQuery(name = CV.EXP_EDU_CER_FIRNAM, query = "Select DISTINCT j from CV j " 
				+ "left join fetch j.experiences exp "
				+ "left join fetch exp.projects " 
				+ "left join fetch j.educations " 
				+ "left join fetch j.certifications "
				+ " where j.firstName = :firstName "),
		@NamedQuery(name = CV.EXP_CERT_FIRNAM, query = "Select DISTINCT j from CV j " 
				+ "left join fetch j.experiences exp "
				+ "left join fetch exp.projects " 
				+ "left join fetch j.certifications"
				+ " where j.firstName = :firstName") })
// @formatter:on
@Table(name = "CV")
public class CV extends AuditedEntity {

	public static final String ALL = "CV_All";

	public static final String FIRST_NAME = "CV_firstName";

	public static final String EXP_EDU_CER_FIRNAM = "CV_experiences.projects_educations_certifications_firstName";

	public static final String EXP_CERT_FIRNAM = "CV_experiences.projects_certifications_firstName";

	@Column(name = "RAW_FILE")
	@Lob
	private byte[] rawFile;

	@Column(name = "RAW_INTS")
	private int[] rawInts;

	@Column(name = "RAW_STRINGS")
	private String[] rawString;

	@Column(name = "RAW_DOUBLE")
	private Double[] rawDoubles;

	@Column(name = "RAW_CHARS")
	private Character[] rawChars;

	@Column(name = "CALENDAR")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar cal = Calendar.getInstance();

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private CVStatus status;

	@Transient
	public CVStatus[] statuses = CVStatus.values();

	@Column(name = "PREVIOUS_STATUS")
	@Enumerated(EnumType.STRING)
	private CVStatus previousStatus;

	@Transient
	public CVStatus[] previousStatuses = CVStatus.values();

	@Transient
	private String tempDescription;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	private Details details;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Experience> experiences;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Education> educations;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Certification> certifications;


	public void addCertification(Certification... certs) {
		if (certs == null) {
			return;
		}

		if (certifications == null) {
			certifications = newHashSet();
		}

		for (Certification cert : certs) {
			cert.setCv(this);
			certifications.add(cert);
		}
	}


	public void addExperience(Experience... exps) {
		if (exps == null) {
			return;
		}

		if (experiences == null) {
			experiences = newHashSet();
		}

		for (Experience experience : exps) {
			experience.setCv(this);
			experiences.add(experience);
		}
	}


	public void addEducation(Education... edus) {
		if (edus == null) {
			return;
		}

		if (educations == null) {
			educations = newHashSet();
		}
		for (Education education : edus) {
			educations.add(education);
			education.setCv(this);
		}
	}


	@JsonProperty("experiences")
	@JsonManagedReference
	public Set<Experience> serializeExperiences() {
		if (experiences == null) {
			return new HashSet<Experience>();
		}

		if (isinitialized(experiences)) {
			return experiences;
		}

		return new HashSet<Experience>();
	}


	@JsonProperty("experiences")
	public void setExperiences(Set<Experience> experiences) {
		this.experiences = experiences;
	}


	@JsonProperty("educations")
	@JsonManagedReference
	public Set<Education> serializeEducations() {
		if (educations == null) {
			return new HashSet<Education>();
		}

		if (isinitialized(educations)) {
			return educations;
		}

		return new HashSet<Education>();
	}


	@JsonProperty("educations")
	public void setEducations(Set<Education> educations) {
		this.educations = educations;
	}


	@JsonProperty("certifications")
	@JsonManagedReference
	public Set<Certification> serializeCertifications() {
		if (certifications == null) {
			return new HashSet<Certification>();
		}

		if (isinitialized(certifications)) {
			return certifications;
		}

		return new HashSet<Certification>();
	}


	@JsonProperty("certifications")
	public void setCertifications(Set<Certification> certs) {
		this.certifications = certs;
	}


	public byte[] getRawFile() {
		return rawFile;
	}


	public void setRawFile(byte[] rawFile) {
		this.rawFile = rawFile;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public CVStatus getStatus() {
		return status;
	}


	public void setStatus(CVStatus status) {
		this.status = status;
	}


	public CVStatus getPreviousStatus() {
		return previousStatus;
	}


	public void setPreviousStatus(CVStatus previousStatus) {
		this.previousStatus = previousStatus;
	}


	public String getTempDescription() {
		return tempDescription;
	}


	public void setTempDescription(String tempDescription) {
		this.tempDescription = tempDescription;
	}


	public Details getDetails() {
		return details;
	}


	public void setDetails(Details details) {
		this.details = details;
		if (details != null) {
			details.setCv(this);
		}
	}


	public int[] getRawInts() {
		return rawInts;
	}


	public void setRawInts(int[] rawInts) {
		this.rawInts = rawInts;
	}


	public String[] getRawString() {
		return rawString;
	}


	public void setRawString(String[] rawString) {
		this.rawString = rawString;
	}


	public Double[] getRawDoubles() {
		return rawDoubles;
	}


	public void setRawDoubles(Double[] rawDoubles) {
		this.rawDoubles = rawDoubles;
	}


	public Character[] getRawChars() {
		return rawChars;
	}


	public void setRawChars(Character[] rawChars) {
		this.rawChars = rawChars;
	}


	public Calendar getCal() {
		return cal;
	}


	public void setCal(Calendar cal) {
		this.cal = cal;
	}


	public Set<Certification> getCertifications() {
		return certifications;
	}


	public Set<Experience> getExperiences() {
		return experiences;
	}


	public Set<Education> getEducations() {
		return educations;
	}

}