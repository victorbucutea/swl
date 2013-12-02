#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.model.map;


/**
 * Implementation is responsible of mapping the UI deserialized/transient entity
 * into its managed counterpart
 * 
 * 
 * @author VictorBucutea
 * 
 */
public interface Mapper {

	public void map(Object managedEntity, Object deserializedEntity);

}
