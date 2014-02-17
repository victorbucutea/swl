#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.model;

public interface Auditable {

	public Audit getAudit();

	public void setAudit(Audit audit);
}
