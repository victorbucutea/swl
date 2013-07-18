package ro.sft.recruiter.base.util;

import org.hibernate.Hibernate;

public class PersistenceUtil {

	/**
	 * method for delegating specific JPA implementation details
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isinitialized(Object obj) {
		return Hibernate.isInitialized(obj);
	}
}
