#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.base.model.map;

import static java.lang.reflect.Modifier.isStatic;
import static ${basePackage}.base.util.ReflectionUtil.getAllFieldsAsMap;
import static ${basePackage}.base.util.ReflectionUtil.getFieldValue;
import static ${basePackage}.base.util.ReflectionUtil.setFieldValue;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonBackReference;

import ${basePackage}.base.model.EntityBase;

public class ReflectionEntityMapper implements Mapper {

	public void map(Object targetObj, Field targetField, Object sourceVal) {

		if (sourceVal == null) {
			setFieldValue(targetObj, targetField, sourceVal);
			return;
		}

		if (isStatic(targetField.getModifiers())) {
			return;
		}

		Class<?> cls = sourceVal.getClass();

		if (Character.class.isAssignableFrom(cls) || Boolean.class.isAssignableFrom(cls)
				|| Number.class.isAssignableFrom(cls) || String.class.isAssignableFrom(cls) || cls.isEnum()
				|| Date.class.isAssignableFrom(cls) || cls.isArray() || Calendar.class.isAssignableFrom(cls)) {
			setFieldValue(targetObj, targetField, sourceVal);
			return;
		}

		if (targetField.isAnnotationPresent(JsonBackReference.class)) {
			/*
			 * field is a reference to its parent. We do not need to set the
			 * value of this field because the managed entity (
			 * targetObj.targetField ) should have a reference to the proper
			 * relation. If the relation was deleted, the corresponding
			 * collection has been removed in the map(Collection,..) method.
			 */
			return;
		}

		map(getFieldValue(targetField, targetObj), sourceVal);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void map(Object managedEntity, Object deserializedEntity) {
		Map<String, Field> sourceFields = getAllFieldsAsMap(deserializedEntity);
		Map<String, Field> targetFields = getAllFieldsAsMap(managedEntity);

		for (String fieldName : sourceFields.keySet()) {
			Field sourceField = sourceFields.get(fieldName);
			Field targetField = targetFields.get(fieldName);
			Object sourceVal = getFieldValue(sourceField, deserializedEntity);

			if (Collection.class.isAssignableFrom(sourceField.getType())) {
				Object targetVal = getFieldValue(sourceField, managedEntity);
				map((Collection) targetVal, (Collection) sourceVal);
				continue;
			}
			map(managedEntity, targetField, sourceVal);
		}

	}

	public void map(Collection<EntityBase> managedCollection, Collection<EntityBase> deserializedCollection) {

		if ((deserializedCollection == null) || deserializedCollection.isEmpty()) {
			return;
		}

		/*
		 * 
		 * 1. Objects marked as deleted will be removed from the managed
		 * collection - there is no way to know if deleted an entity is present
		 * or not in the managed set, thus we look for the 'deleted' flag.
		 * 
		 * 2. Objects from managedCollection not present in deserialized
		 * collection --> do nothing
		 * 
		 * 3. Object from deserizedCollection not present in managed collection
		 * --> add them to the managed collection
		 * 
		 * 4. Objects present in both deserialized and managed collection, map
		 * all child attributes ( update all attributes )
		 */

		for (EntityBase deserializedObj : deserializedCollection) {

			if (deserializedObj.isDeleted()) {
				managedCollection.remove(deserializedObj);
				continue;
			}

			if (managedCollection.contains(deserializedObj)) {
				for (EntityBase managedObj : managedCollection) {
					if (deserializedObj.equals(managedObj)) {
						map(managedObj, deserializedObj);
					}
				}

			} else {
				// deserialized object not present in target collection (not managed, it is a new entity)
				managedCollection.add(deserializedObj);
			}
		}
	}

}
