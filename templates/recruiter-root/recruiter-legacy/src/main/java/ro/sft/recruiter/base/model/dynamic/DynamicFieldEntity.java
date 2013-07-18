package ro.sft.recruiter.base.model.dynamic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import ro.sft.recruiter.base.model.Audit;

/**
 * Adds 'dynamic' properties to an entity. 'Dynamic' properties can be added or
 * removed, and their state can always be persisted. So when a new prop will be
 * needed for an entity it can be added or removed programatically.
 * 
 * The properties are held in a Map which is serialized on PrePersist and
 * PreUpdate, and deserialized on PostLoad
 * 
 * @author VictorBucutea
 * 
 */
@MappedSuperclass
public class DynamicFieldEntity extends Audit {

	private static final long serialVersionUID = 4572473811940783769L;

	@Column(name = "DYNAMIC_PROPS")
	private byte[] serializedProps;

	@Transient
	private Map<String, DynamicProperty<Serializable>> dynamicPropsMap = new HashMap<String, DynamicProperty<Serializable>>();

	/**
	 * Adds String type dynamic property, replacing the old value if one exists
	 * 
	 * @param name
	 * @param value
	 */
	public void addDynamicStringProperty(String name, String value) {
		addDynamicProperty(name, new DynamicProperty<Serializable>(name, value));
	}

	/**
	 * Adds a byte[] type dynamic property, replacing the old value if one
	 * exists
	 * 
	 * @param name
	 * @param value
	 */
	public void addDynamicBinaryProperty(String name, byte[] value) {
		addDynamicProperty(name, new DynamicProperty<Serializable>(name, value));
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addDynamicProperty(String name, Serializable value) {
		dynamicPropsMap.put(name, new DynamicProperty<Serializable>(name, value));
		// instrument getter and setter
	}

	/**
	 * 
	 * @param propName
	 * @param cls
	 *            expected type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getDynamicPropertyValue(String propName, Class<T> cls) {
		Serializable value = dynamicPropsMap.get(propName).getValue();
		return (T) value;
	}

	/**
	 * 
	 * @param propName
	 * @return
	 */
	public String getDynamicStringPropertyValue(String propName) {
		return getDynamicPropertyValue(propName, String.class);
	}

	/**
	 * 
	 * @param propName
	 * @return
	 */
	public byte[] getDynamicBinaryPropertyValue(String propName) {
		Serializable value = dynamicPropsMap.get(propName).getValue();
		return (byte[]) value;
	}

	/**
	 * 
	 * @param propName
	 * @param cls
	 * @return
	 */
	public Class<Serializable> getDynamicPropertyClass(String propName) {
		Class<Serializable> propertyClass = dynamicPropsMap.get(propName).getPropertyClass();
		return propertyClass;
	}

	/**
	 * 
	 * @return the names of all dynamic properties held in this field.
	 */
	public Set<String> getDynamicPropertyNames() {
		return dynamicPropsMap.keySet();
	}

	@PrePersist
	@PreUpdate
	public void passivate() {
		//serializedProps = SerializationUtils.serialize((Serializable) dynamicPropsMap);
	}

	@SuppressWarnings("unchecked")
	@PostLoad
	public void activate() {
		//dynamicPropsMap = (Map<String, DynamicProperty<Serializable>>) SerializationUtils.deserialize(serializedProps);
	}

}
