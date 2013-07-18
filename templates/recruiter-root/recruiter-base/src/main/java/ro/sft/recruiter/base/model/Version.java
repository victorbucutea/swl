package ro.sft.recruiter.base.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Version {

	@javax.persistence.Version
	@Column(name = "version")
	protected int version;

}
