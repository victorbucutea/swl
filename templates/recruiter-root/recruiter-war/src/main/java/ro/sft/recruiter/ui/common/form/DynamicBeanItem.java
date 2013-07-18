package ro.sft.recruiter.ui.common.form;

import java.io.Serializable;
import java.util.Set;

import ro.sft.recruiter.base.model.dynamic.DynamicFieldEntity;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.MethodProperty;

public class DynamicBeanItem<BT extends DynamicFieldEntity> extends BeanItem<BT> {

	private static final long serialVersionUID = 3662884355634890142L;

	public DynamicBeanItem(final BT pojo) {
		super(pojo);

		Set<String> dynamicPropNames = pojo.getDynamicPropertyNames();
		for (final String dynaProp : dynamicPropNames) {
			Property beanProp = createPropertyProxy(pojo, dynaProp);
			addItemProperty(dynaProp, beanProp);
		}

	}

	/**
	 * 
	 * creates a proxy for a java.bean.Property which delegates the value set
	 * and get to the dynamic properties of our entity .
	 * 
	 * */
	@SuppressWarnings({ "serial", "unchecked" })
	private Property createPropertyProxy(final BT pojo, final String dynaProp) {

		Property beanProp = new MethodProperty<BT>(pojo, dynaProp) {
			@Override
			public Object getValue() {
				return pojo.getDynamicPropertyValue(dynaProp, pojo.getDynamicPropertyClass(dynaProp));
			}

			@Override
			public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
				pojo.addDynamicProperty(dynaProp, (Serializable) newValue);
			}

		};

		return beanProp;
	}

}
