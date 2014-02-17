#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.user.model;

import javax.persistence.Column;
import javax.persistence.Table;

import ${package}.${rootArtifactId}.base.model.AuditedEntity;

@SuppressWarnings("serial")
@javax.persistence.Entity
@Table(name = "ROLE")
public class Role extends AuditedEntity {

	@Column(name = "NAME")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
