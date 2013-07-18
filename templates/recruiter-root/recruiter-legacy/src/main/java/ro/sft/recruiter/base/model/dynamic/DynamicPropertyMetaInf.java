package ro.sft.recruiter.base.model.dynamic;

import java.io.Serializable;

/**
 * Holds meta information for a dynamic property. Things like class, max size,
 * UI rendering properties,etc
 * 
 * @author VictorBucutea
 * 
 */
public class DynamicPropertyMetaInf<M extends Serializable> implements Serializable {

	private static final long serialVersionUID = -5235689574890268992L;

	private Class<M> cls;

	public DynamicPropertyMetaInf(Class<M> cls) {
		this.cls = cls;
	}

	public Class<M> getCls() {
		return cls;
	}

	public void setCls(Class<M> cls) {
		this.cls = cls;
	}

}
