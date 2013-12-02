#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.util;

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
