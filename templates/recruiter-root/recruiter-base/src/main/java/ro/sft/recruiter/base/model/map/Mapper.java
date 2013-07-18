package ro.sft.recruiter.base.model.map;


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
