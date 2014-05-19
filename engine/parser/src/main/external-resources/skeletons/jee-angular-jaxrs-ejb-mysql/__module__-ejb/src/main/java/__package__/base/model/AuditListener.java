#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.base.model;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditListener {

	@PrePersist
	public void setCreationDate(Auditable entity) {

		Audit audit = initAudit(entity);

		audit.setCreationDate(new Date());
		audit.setCreationUser(getCurrentUsername());
	}

	@PreUpdate
	public void setLastUpdateUser(Auditable entity) {

		Audit audit = initAudit(entity);

		audit.setLastUpdateDate(new Date());
		audit.setLastUpdateUser(getCurrentUsername());
	}

	private Audit initAudit(Auditable entity) {
		if (entity.getAudit() == null) {
			entity.setAudit(new Audit());
		}
		Audit audit = entity.getAudit();
		return audit;
	}

	public String getCurrentUsername() {
		// TODO implement way to get user name
		return "TO_IMPLEMENT";
	}

}
