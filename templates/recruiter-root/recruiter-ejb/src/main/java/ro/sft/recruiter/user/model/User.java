package ro.sft.recruiter.user.model;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ro.sft.recruiter.base.model.AuditedEntity;
import ro.sft.recruiter.base.model.ser.NoLazyLoadingSerializer;

@SuppressWarnings("serial")
@javax.persistence.Entity
@Table(name = "USER")
@NamedQueries(value = { @NamedQuery(name = User.ALL, query = "Select j from User j") })
public class User extends AuditedEntity {

	public static final String ALL = "User_ALL";

	@Column(name = "NAME")
	private String name;

	@Column(name = "PASSWORD")
	private String password;

	@JsonSerialize(using = NoLazyLoadingSerializer.class)
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "USER_GROUP", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", referencedColumnName = "id") })
	private Set<Group> groups;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addGroup(Group... groups) {
		if (groups == null) {
			return;
		}

		if (this.groups == null) {
			this.groups = new HashSet<Group>();
		}

		for (Group group : groups) {
			this.groups.add(group);
		}
	}

	public Set<Group> getGroups() {
		return groups;
	}

	@JsonProperty
	public Set<Role> getRoles() {
		Set<Role> roles = newHashSet();

		if (groups == null) {
			return roles;
		}

		for (Group g : groups) {
			roles.addAll(g.getRoles());
		}

		return roles;
	}

	public String getPassword() {
		return password;
	}

}
