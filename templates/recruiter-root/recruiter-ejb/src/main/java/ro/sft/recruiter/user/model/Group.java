package ro.sft.recruiter.user.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import ro.sft.recruiter.base.model.AuditedEntity;
import ro.sft.recruiter.base.model.ser.NoLazyLoadingSerializer;

@SuppressWarnings("serial")
@javax.persistence.Entity
@Table(name = "`GROUP`")
public class Group extends AuditedEntity {

	@Column(name = "NAME")
	String name;

	@JsonSerialize(using = NoLazyLoadingSerializer.class)
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "ROLE_GROUP", joinColumns = { @JoinColumn(name = "GROUP_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "id") })
	Set<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addRoles(Role... roles) {
		if (roles == null) {
			return;
		}

		if (this.roles == null) {
			this.roles = new HashSet<Role>();
		}

		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	public Set<Role> getRoles() {
		return roles;
	}

}
