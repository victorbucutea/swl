package ro.sft.recruiter.base.model.dynamic;

import java.io.Serializable;

/**
 * Adds 'dynamic' properties to an entity. 'Dynamic' properties can be added or
 * removed, and their state can always be persisted. So when a new prop will be
 * needed for an entity it can be added or removed programatically.
 * 
 * @author VictorBucutea
 * 
 */
public class DynamicProperty<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 8512832095576792759L;

	private String name;

	private T value;

	private DynamicPropertyMetaInf<T> metaInf;

	public DynamicProperty(String name) {
		this.name = name;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DynamicProperty(String name, T value) {
		this.name = name;
		this.value = value;
		metaInf = new DynamicPropertyMetaInf(value.getClass());
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}

	public Class<T> getPropertyClass() {
		return metaInf.getCls();
	}
}
